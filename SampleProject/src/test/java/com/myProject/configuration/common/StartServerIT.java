package com.myProject.configuration.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myProject.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 *  @RunWith(SpringJUnit4ClassRunner.class) + @SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 *  will start up servlet container on a "random" port
 *  @LocalServerPort retrieves random port
 *  To make HTTP calls using either RestTemplate or RestAssured
 *  @SpringBootTest initially loads Spring app context using myProject/resource/application-default.yml but replaces
 *  all properties using "spring.profiles.active" mentioned in myProject/resource/application-default.yml for example
 *      - if myProject/resource/application-default.yml ES search pointed at dev, but spring.profiles.active=qa
 *          - then testcases will be executed against properties in main/resource/application-qa.yml
 *      - myProject/resource/application-qa.yml is being used to overcome jasypt token, OC auth filter
 *  DEBUG you can set break points in your IDE to debug myProject cases locally
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StartServerIT {

  protected TestRestTemplate restTemplate = new TestRestTemplate();

  protected Gson gson = new GsonBuilder().setPrettyPrinting().create();

  protected HttpHeaders headers = new HttpHeaders();
  //@LocalServerPort to be used with SpringBootTest webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
  @LocalServerPort
  //@Value("${server.port}")
  private int port;

  protected String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
