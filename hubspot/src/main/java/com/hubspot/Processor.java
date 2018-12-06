package com.hubspot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hubspot.dao.Country;
import com.hubspot.dao.Partner;
import com.hubspot.dao.PartnerDetails;
import com.hubspot.dao.PostRequest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Processor {
  HttpHandler getEndpoint;
  HttpHandler postEndpoint;

  public Processor(HttpHandler getEndpoint, HttpHandler postEndpoint) {
    this.getEndpoint = getEndpoint;
    this.postEndpoint = postEndpoint;
  }


  /**
   * Makes GET call, passes GET response to POST call.
   * Returns POST response
   *
   * @return
   */
  public String process(){
    GsonBuilder builder = new GsonBuilder();
    builder
        .setPrettyPrinting()
        .serializeNulls();
    Gson gson = builder.create();

    String response=null;
    try {
      String s = this.getEndpoint.doGet();
      Partner partner = gson.fromJson(s, Partner.class);

      Map<String, String> params = buildPostRequest(partner, gson);
      response = this.postEndpoint.doPost(params);
    }catch (Exception e){
      e.printStackTrace();
    }

    return response;
  }

  /**
   *
   * @param partner
   * @param gson
   * @return
   */
  private Map<String, String> buildPostRequest(Partner partner, Gson gson) {

    Map<String, List<PartnerDetails>> mapPartners =
        partner.getPartners()
            .stream()
            .collect(
                Collectors.toMap(
                    rec -> rec.getCountry(), //key
                    rec-> Collections.singletonList(rec), //value
                    (rec1, rec2) -> { //avoids IllegalStateException: Duplicate key if 2 or more keys match
                      List<PartnerDetails> list = new LinkedList<>();
                      list.addAll(rec1);
                      list.addAll(rec2);
                      return list;
                    }
                )
            );
    PostRequest postRequest = new PostRequest();
    List<Country> countryList = postRequest.getCountries();
    for(Map.Entry<String, List<PartnerDetails>> entry : mapPartners.entrySet()){
      Country country = getCountryDetails(entry.getKey(), entry.getValue());
      countryList.add(country);
    }

    String payload = gson.toJson(postRequest);
    Map<String, String> params = new HashMap<>();
    params.put(UtilHelper.PAYLOAD, payload);
    return params;
  }

  private Country getCountryDetails(String key, List<PartnerDetails> values) {
    Country country = new Country();
    country.setName(key);
    country.setAttendees(new LinkedList<>());
    country.setAttendeeCount(0);
    country.setStartDate(null);
    Map<String, List<String>> mapStartDtEmails = new TreeMap<>();// to maintain start date order
    for(PartnerDetails pd: values){
      String email = pd.getEmail();
      for(String dt: pd.getAvailableDates()) {
        if (mapStartDtEmails.containsKey(dt)) {
          List<String> list = mapStartDtEmails.get(dt);
          list.add(email);
          mapStartDtEmails.put(dt, list);
        } else {
          List<String> emailList = new LinkedList<>();
          emailList.add(email);
          mapStartDtEmails.put(dt, emailList);
        }
      }
    }

    Set<String> dateSet = mapStartDtEmails.keySet();
    List<String> dateList = new LinkedList<>(dateSet);
    String startDt="";
    int partners = 0;
    List<String> emailList=new LinkedList<>();
    for (int i = 0; i < dateList.size(); i++) {
      String currDt = dateList.get(i);
      LocalDate ldt = LocalDate.parse(currDt);
      List<String> currDtEmailList = mapStartDtEmails.get(currDt);
      int currDtEmailListSize = currDtEmailList.size();
      if(i+1 < dateList.size()){
        LocalDate nextDt = ldt.plusDays(1);
        String nextDtStr = nextDt.toString();
        if(dateList.get(i+1).equalsIgnoreCase(nextDtStr)){
          List<String> nextDtEmailList = mapStartDtEmails.get(nextDtStr);
          if(currDtEmailListSize == nextDtEmailList.size() && currDtEmailListSize > partners){
            startDt = dateList.get(i);
            partners = currDtEmailListSize;
            emailList = currDtEmailList;
          }
        }
      }
    }
    if(StringUtils.isNotEmpty(startDt)){
      country.setStartDate(startDt);
    }
    if(emailList!=null && emailList.size()>0){
      country.setAttendees(emailList);
      country.setAttendeeCount(emailList.size());
    }
    return country;
  }

}
