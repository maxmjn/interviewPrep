package com.myProject.service.v1;

import com.myProject.dao.AlbumDao;
import com.myProject.dao.DBTasksImpl;
import com.myProject.dao.PhotoDao;
import com.myProject.dao.UserDao;
import com.myProject.model.Album;
import com.myProject.model.Photo;
import com.myProject.model.User;
import com.myProject.repository.AlbumRepository;
import com.myProject.repository.PhotoRepository;
import com.myProject.repository.UserRepository;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AdminSvc {

  /**
   * External API returns foreign keys example album.userId, photo.albumId; to insert API responses
   * AS-IS into DB using separate repo(User/Album/Photo) vs. UserDao/AlbumDao/PhotoDao which requires
   * building all DAO properties+relationship and finally UserDao.save()
   * like com.myProject.service.v1.PhotoAlbumSvc#createNewEntities(com.myProject.model.PostRequest)
   * So to load external data(which already have foreign keys) AS-IS using separate repo(User/Album/Photo)
   */
  @Autowired private UserRepository userRepository;
  @Autowired private AlbumRepository albumRepository;
  @Autowired private PhotoRepository photoRepository;
  @Autowired private DBTasksImpl dbTasks;

  /**
   *
   * @return
   */
  public List<String> retrieve_LoadDB(String urlUsers, String urlAlbums, String urlPhotos){

    List<String> loadStatus = new LinkedList<>();
    long u = userRepository.count();
    long a = albumRepository.count();
    long p = photoRepository.count();

    if(u > 0 && a >0 && p >0){
      loadStatus.add("User count:" + u);
      loadStatus.add("Album count:" + a);
      loadStatus.add("Photo count:" + p);
      return loadStatus;
    }

    int retrieved=0;
    int inserted=0;

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    RestTemplate restTemplate = new RestTemplate();

    List<User> userList = null;
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlUsers);
    try {
      ResponseEntity<List<User>> response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<List<User>>() {
          });
      userList = response.getBody();
    }catch (Exception e){
      loadStatus.add("Users: load failed - " + e.getMessage());
    }
    if(CollectionUtils.isNotEmpty(userList)){
      retrieved = userList.size();
      loadStatus.add("Users: retrieved - " + retrieved);
      inserted = userRepository.saveAll(userList).size();
      loadStatus.add("Users: inserted - " + inserted);
    }

    List<Album> albumList = null;
    builder = UriComponentsBuilder.fromHttpUrl(urlAlbums);
    try {
      ResponseEntity<List<Album>> response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<List<Album>>() {
          });
      albumList = response.getBody();
    }catch (Exception e){
      loadStatus.add("Albums: load failed - " + e.getMessage());
    }
    if(CollectionUtils.isNotEmpty(albumList)){
      retrieved = albumList.size();
      loadStatus.add("Albums: retrieved - " + retrieved);
      //for debug
//      for(Album album:albumList){
//        System.out.println(userRepository.findAll());
//        System.out.println("Try********** ME:" + albumDao);
//        albumRepository.save(albumDao);
//      }
      inserted = albumRepository.saveAll(albumList).size();
      loadStatus.add("Albums: inserted - " + inserted);
    }

    List<Photo> photoList = null;
    builder = UriComponentsBuilder.fromHttpUrl(urlPhotos);
    try {
      ResponseEntity<List<Photo>> response = restTemplate.exchange(
          builder.build().encode().toUri(),
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<List<Photo>>() {
          });
      photoList = response.getBody();
    }catch (Exception e){
      loadStatus.add("Photos: load failed - " + e.getMessage());
    }
    if(CollectionUtils.isNotEmpty(photoList)){
      retrieved = photoList.size();
      loadStatus.add("Photos: retrieved - " + retrieved);
      inserted = photoRepository.saveAll(photoList).size();
      loadStatus.add("Photos: inserted - " + inserted);
      //TODO: after each successful load update sequence
    }

    return loadStatus;
  }

  /**
   *
   * @return
   */
  public List<String> resetAll() {
    List<String> loadStatus = new LinkedList<>();

    try{
      dbTasks.clearAllByEntity();
      dbTasks.restartSequences();
    } catch (Exception e){
      e.printStackTrace();
    }

    long u = userRepository.count();
    long a = albumRepository.count();
    long p = photoRepository.count();

    loadStatus.add("User count:" + u);
    loadStatus.add("Album count:" + a);
    loadStatus.add("Photo count:" + p);
    return loadStatus;


  }
}
