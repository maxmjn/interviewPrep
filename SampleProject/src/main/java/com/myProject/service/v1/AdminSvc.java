package com.myProject.service.v1;

import com.myProject.dao.AlbumDao;
import com.myProject.dao.DBTasksImpl;
import com.myProject.dao.PhotoDao;
import com.myProject.model.Album;
import com.myProject.model.AlbumEntity;
import com.myProject.model.Photo;
import com.myProject.model.PhotoEntity;
import com.myProject.model.User;
import com.myProject.model.UserEntity;
import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.PhotoEntityRepository;
import com.myProject.repository.UserEntityRepository;
import com.myProject.repository.AlbumEntityRepository;
import com.myProject.util.EntityHelper;
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

  @Autowired private UserEntityRepository userEntityRepository;
  @Autowired private AlbumEntityRepository albumEntityRepository;
  @Autowired private PhotoEntityRepository photoEntityRepository;

  @Autowired private AlbumDaoRepository albumDaoRepository;
  @Autowired private PhotoDaoRepository photoDaoRepository;

  /**
   *
   * @return
   */
  public List<String> retrieve_LoadDB(String urlUsers, String urlAlbums, String urlPhotos){

    List<String> loadStatus = new LinkedList<>();
    long u = userEntityRepository.count();
    long a = albumEntityRepository.count();
    long p = photoEntityRepository.count();

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
      List<UserEntity> userEntityList = EntityHelper.getUserEntity(userList);
      userEntityList = userEntityRepository.saveAll(userEntityList);
      inserted = userEntityList.size();
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
      List<AlbumEntity> albumEntityList = EntityHelper.getAlbumEntity(albumList);
      try {
        albumEntityList = albumEntityRepository.saveAll(albumEntityList);
        inserted = albumEntityList.size();
      }catch (Exception e){
        //initial load is thru AlbumEntityRepo to load quickly data we get from web without any dependency check
        // after "delete" foreign key mapping still exist so load thru AlbumEnityRepo fails...
        // tried "truncate table" vs. delete did not work
        List<AlbumDao> albumDaoList = EntityHelper.getAlbumDao(albumEntityList);
        albumDaoList = albumDaoRepository.saveAll(albumDaoList);
        inserted = albumDaoList.size();
      }
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
      List<PhotoEntity> photoEntityList = EntityHelper.getPhotoEntity(photoList);
      try {
        photoEntityList = photoEntityRepository.saveAll(photoEntityList);
        inserted = photoEntityList.size();
      }catch (Exception e){
        //initial load is thru PhotoEntityRepo to load quickly data we get from web without any dependency check
        // after "delete" foreign key mapping still exist so load thru PhotoEntityRepo fails...
        // tried "truncate table" vs. delete did not work
        List<PhotoDao> photoDaoList = EntityHelper.getPhotoDao(photoEntityList);
        photoDaoList = photoDaoRepository.saveAll(photoDaoList);
        inserted = photoDaoList.size();
      }

      loadStatus.add("Photos: inserted - " + inserted);
    }

    return loadStatus;
  }

  @Autowired private DBTasksImpl dbTasks;

  /**
   *
   * @return
   */
  public List<String> removeAll() {
    List<String> loadStatus = new LinkedList<>();

    try{
      dbTasks.clearAllByEntity();
    } catch (Exception e){
      e.printStackTrace();
    }

    long u = userEntityRepository.count();
    long a = albumEntityRepository.count();
    long p = photoEntityRepository.count();

    loadStatus.add("User count:" + u);
    loadStatus.add("Album count:" + a);
    loadStatus.add("Photo count:" + p);
    return loadStatus;


  }
}
