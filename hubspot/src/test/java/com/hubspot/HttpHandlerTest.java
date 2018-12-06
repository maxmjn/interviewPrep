package com.hubspot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hubspot.dao.Country;
import com.hubspot.dao.Partner;
import com.hubspot.dao.PartnerDetails;
import com.hubspot.dao.PostRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testPartners() throws Exception{
        HttpHandler httpHandler = new HttpHandler(
            "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=6812db25b5283edd31fe7683baf6"
        );
        String s = httpHandler.doGet();
        GsonBuilder builder = new GsonBuilder();
        builder
            .setPrettyPrinting()
            .serializeNulls();

        Gson gson = builder.create();
        Partner partner = gson.fromJson(s, Partner.class);
        partner.getPartners().get(0).getAvailableDates().stream().forEach(date -> {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDate ldt = LocalDate.parse(date);
            System.out.println(ldt);
        });
        System.out.println(gson.toJson(partner.getPartners().get(0)));
    }

    @Test
    public void testPost() throws Exception{
        //"https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=6812db25b5283edd31fe7683baf6"
        //Post.name = Partner.country
        //Post.attendees = Partner.email
        //Post.attendeeCount = attendees.length
        //Post.startDate =

        HttpHandler httpHandler = new HttpHandler(
            "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=6812db25b5283edd31fe7683baf6"
        );
        String s = httpHandler.doGet();
        GsonBuilder builder = new GsonBuilder();
        builder
            .setPrettyPrinting()
            .serializeNulls();

        Gson gson = builder.create();
        Partner partner = gson.fromJson(s, Partner.class);
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
//            Country country = getCountryDetails(entry.getKey(), entry.getValue());
//            countryList.add(country);
        }

        String s1 = gson.toJson(postRequest);
        System.out.println(s1);
        HttpHandler httpHandler2 = new HttpHandler("https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=6812db25b5283edd31fe7683baf6");
        Map<String, String> params = new HashMap<>();
        params.put(UtilHelper.PAYLOAD, s1);
        String response = httpHandler2.doPost(params);
        System.out.println(response);
    }
}