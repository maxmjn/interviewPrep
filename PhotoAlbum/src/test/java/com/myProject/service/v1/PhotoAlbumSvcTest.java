package com.myProject.service.v1;

import com.myProject.configuration.common.StartServerIT;
import com.myProject.dao.AlbumDaoImpl;
import com.myProject.dao.DBTasksImpl;
import com.myProject.dao.PhotoDaoImpl;
import com.myProject.dao.UserDaoImpl;
import com.myProject.model.PostRequest;
import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.UserDaoRepository;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class PhotoAlbumSvcTest extends StartServerIT {

  @Autowired private UserDaoRepository userDaoRepository;
  @Autowired private AlbumDaoRepository albumDaoRepository;
  @Autowired private PhotoDaoRepository photoDaoRepository;
  @Autowired private DBTasksImpl dbTasks;
  @Autowired private AdminSvc adminSvc;

  @Autowired private UserDaoImpl userDaoImpl;
  @Autowired private AlbumDaoImpl albumDaoImpl;
  @Autowired private PhotoDaoImpl photoDaoImpl;

  private static int USER_ID = 1;
  private static int ALBUM_ID = 10;
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

  @Test
  public void createNewEntities_EMPTYDB() {
    adminSvc.resetAll();

    PostRequest postRequest = new PostRequest();
    postRequest.setName("Black");
    postRequest.setUsername("Black");
    postRequest.setEmail("test@black.com");
    postRequest.setAlbumTitle("Black Magic");
    postRequest.setPhotoTitle("Black-1");
    postRequest.setThumbnailUrl("black.com/1/small");
    postRequest.setUrl("black.com/1/original");

    String url = createURLWithPort("/api/v1/photoAlbum/create");
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//        .queryParam("un", "Karianne")
        ;

    System.out.println("Target Url:" + builder.toUriString());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String requestEntity = gson.toJson(postRequest);
    HttpEntity<String> entity = new HttpEntity<>(requestEntity, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserView> response;
    UserView userView = new UserView();
    try {
      response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.POST,
          entity,
          new ParameterizedTypeReference<UserView>() {
          });
      if(response.getStatusCode().is4xxClientError()){
        Assert.fail("App returned HTTP " + response.getStatusCode().value() + " please check your configuration");
      }
      userView = response.getBody();
      System.out.println(gson.toJson(userView));
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    System.out.println("Users maxId:" + userDaoImpl.count() +
        ", Album maxId:" + albumDaoImpl.getMaxId() +
        ", Photo maxId:" + photoDaoImpl.getMaxId()
    );
    Assert.assertTrue("CREATE failed", userView.getEmail().equalsIgnoreCase("test@black.com"));
  }

  @Test
  public void createNewEntities_nonEMPTYDB() {
    PostRequest postRequest = new PostRequest();
    postRequest.setName("Black");
    postRequest.setUsername("Black");
    postRequest.setEmail("test@black.com");
    postRequest.setAlbumTitle("Black Magic");
    postRequest.setPhotoTitle("Black-1");
    postRequest.setThumbnailUrl("black.com/1/small");
    postRequest.setUrl("black.com/1/original");

    String url = createURLWithPort("/api/v1/photoAlbum/create");
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//        .queryParam("un", "Karianne")
        ;

    System.out.println("Target Url:" + builder.toUriString());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String requestEntity = gson.toJson(postRequest);
    HttpEntity<String> entity = new HttpEntity<>(requestEntity, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserView> response;
    UserView userView = new UserView();
    try {
      response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.POST,
          entity,
          new ParameterizedTypeReference<UserView>() {
          });
      if(response.getStatusCode().is4xxClientError()){
        Assert.fail("App returned HTTP " + response.getStatusCode().value() + " please check your configuration");
      }
      userView = response.getBody();
      System.out.println(gson.toJson(userView));
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    System.out.println("Users maxId:" + userDaoImpl.count() +
        ", Album maxId:" + albumDaoImpl.getMaxId() +
        ", Photo maxId:" + photoDaoImpl.getMaxId()
    );
    Assert.assertTrue("CREATE failed", userView.getEmail().equalsIgnoreCase("test@black.com"));
  }
}