package com.myProject.configuration;

import com.myProject.configuration.common.StartServerIT;
import com.myProject.dao.PhotoDao;
import com.myProject.model.User;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.service.v1.AdminSvc;
import com.myProject.service.v1.PhotoAlbumSvc;
import com.myProject.view.UserView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PhotoITest extends StartServerIT {

  @Autowired private PhotoDaoRepository photoDaoRepository;
  @Autowired private PhotoAlbumSvc photoAlbumSvc;

  @Autowired private AdminSvc adminSvc;

  private static int USER_ID = 4;
  private static int ALBUM_ID = 31;

  //can't use @Before because it needs static
  //so loading and deleting for each test
  @Before
  public void loadDB(){
    //each @Test runs parallel against same h2 DB so clear data, load test data
    String urlUsers = String.format("https://jsonplaceholder.typicode.com/users?id=%d", USER_ID);
    String urlAlbums = String.format("https://jsonplaceholder.typicode.com/albums?userId=%d&id=%d", USER_ID, ALBUM_ID);
    String urlPhotos = String.format("https://jsonplaceholder.typicode.com/photos?albumId=%d", ALBUM_ID);
    System.out.println(adminSvc.retrieve_LoadDB(urlUsers, urlAlbums, urlPhotos));
  }

  @After
  public void unloadDB(){
  }


//  @Ignore
  @Test
  public void testPhotosByAlbumId() {
    List<PhotoDao> photoDaoList = photoDaoRepository.findByAlbumDaoId(ALBUM_ID);
    System.out.println(photoDaoList);
    Assert.assertTrue("PhotoByAlbumId failed",
          photoDaoList.size() > 0
        );
  }

//  @Ignore
  @Test
  public void testAlbumPhotos() {
    List<User> userList = photoAlbumSvc.getUsers(USER_ID);
    UserView userView = photoAlbumSvc.getAlbums(userList.get(0).getUsername());
    System.out.println(userView);
    Assert.assertTrue("AlbumPhotos failed",
        userView.getId() == USER_ID
    );
  }

  @Test
  public void testHttp_PhotosById() throws Exception{

    String url = createURLWithPort("/api/v1/photoAlbum/user/{userId}/albums/{albumId}");
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//        .queryParam("un", "Karianne")
        ;
    Map<String,Integer> pathParams = new HashMap<>();
    pathParams.put("userId", USER_ID);
    pathParams.put("albumId", ALBUM_ID);
    UriComponents urlWithPathParams = builder.buildAndExpand(pathParams);

    System.out.println("Target Url:" + urlWithPathParams);

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<List<PhotoDao>> response;
    List<PhotoDao> photoDaoList = null;
    try {
      response = restTemplate.exchange(
          urlWithPathParams.encode().toUri(),
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<List<PhotoDao>>() {
          });
      if(response.getStatusCode().is4xxClientError()){
        Assert.fail("App returned HTTP " + response.getStatusCode().value() + " please check your configuration");
      }
      photoDaoList = response.getBody();
      System.out.println(gson.toJson(photoDaoList));
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    Assert.assertTrue("PhotosById failed",
          photoDaoList.size() > 0
        );
  }
}
