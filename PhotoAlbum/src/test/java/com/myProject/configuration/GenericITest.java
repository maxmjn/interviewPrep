package com.myProject.configuration;

import com.myProject.dao.UserDao;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GenericITest {

//  @Ignore
  @Test
  public void testHttp() throws Exception{
    String url = "https://jsonplaceholder.typicode.com/users";
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                                  //.queryParam("", "")
    ;

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<List<UserDao>> response;
    try {
      response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<List<UserDao>>() {
          });
      if(response.getStatusCode().is4xxClientError()){
        Assert.fail("App returned HTTP " + response.getStatusCode().value() + " please check your configuration");
      }
      List<UserDao> userDaoList = response.getBody();
      System.out.println(userDaoList);
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }
}
