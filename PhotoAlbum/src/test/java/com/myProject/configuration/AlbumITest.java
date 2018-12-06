package com.myProject.configuration;

import com.myProject.configuration.common.StartServerIT;
import com.myProject.dao.AlbumDao;
import com.myProject.repository.AlbumDaoRepository;
import com.myProject.service.v1.AdminSvc;
import com.myProject.view.UserView;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class AlbumITest extends StartServerIT {

  @Autowired private AlbumDaoRepository albumDaoRepository;

  @Autowired private AdminSvc adminSvc;

  private static int USER_ID = 1;
  private static int ALBUM_ID = 1;
  private static String urlUsers = String.format("https://jsonplaceholder.typicode.com/users?id=%d", USER_ID);
  private static String urlAlbums = String.format("https://jsonplaceholder.typicode.com/albums?userId=%d&id=%d", USER_ID, ALBUM_ID);
  private static String urlPhotos = String.format("https://jsonplaceholder.typicode.com/photos?albumId=%d", ALBUM_ID);

  //can't use @Before because it needs static
  //so loading and deleting for each test
  @Before
  public void loadDB(){
    List<String> loadDBStatus = adminSvc.retrieve_LoadDB(urlUsers, urlAlbums, urlPhotos);
    System.out.println(loadDBStatus);
  }

  @After
  public void unloadDB(){
    List<String> loadDBStatus = adminSvc.resetAll();
    System.out.println(loadDBStatus);
  }

  //@Ignore
  @Test
  public void testAlbumByUserId(){
    List<AlbumDao> albumDaoList = albumDaoRepository.findByUserDaoId(4);
    System.out.println(albumDaoList);
  }

//  @Ignore
  @Test
  public void testAlbumByUsername(){
    List<AlbumDao> albumDaoList = albumDaoRepository.findByUserDaoUsername("Karianne");
    System.out.println(albumDaoList);
  }

  @Test
  public void testHttp_AlbumByUsername() throws Exception{

    String url = createURLWithPort("/api/v1/photoAlbum/user/albums");
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("un", "Karianne")
        ;
    System.out.println("Target Url:" + builder.toUriString());

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserView> response;
    try {
      response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<UserView>() {
          });
      if(response.getStatusCode().is4xxClientError()){
        Assert.fail("App returned HTTP " + response.getStatusCode().value() + " please check your configuration");
      }
      UserView list = response.getBody();
      System.out.println(gson.toJson(list));
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

}
