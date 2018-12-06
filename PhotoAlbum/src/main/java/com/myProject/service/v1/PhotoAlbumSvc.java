package com.myProject.service.v1;

import com.myProject.dao.AlbumDao;
import com.myProject.dao.AlbumDaoImpl;
import com.myProject.dao.DBTasksImpl;
import com.myProject.dao.PhotoDao;
import com.myProject.dao.PhotoDaoImpl;
import com.myProject.dao.UserDao;
import com.myProject.dao.UserDaoImpl;
import com.myProject.model.Album;
import com.myProject.model.Photo;
import com.myProject.model.PostRequest;
import com.myProject.model.User;
import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.UserDaoRepository;
import com.myProject.util.EntityHelper;
import com.myProject.view.UserView;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class PhotoAlbumSvc {

  @Autowired private UserDaoImpl userDaoImpl;
  @Autowired private AlbumDaoImpl albumDaoImpl;
  @Autowired private PhotoDaoImpl photoDaoImpl;

  @Autowired private UserDaoRepository userDaoRepository;

  /**
   *
   * @param username
   */
  public UserView getAlbums(String username){
    UserDao userDao = getUser(username);
    if(userDao!=null){
      //find albums by userId
      AlbumDao albumDao = new AlbumDao();
      albumDao.setUserDao(userDao);
      Example<AlbumDao> albumDaoExample = Example.of(albumDao);
      List<AlbumDao> albumDaoList = albumDaoImpl.findAll(albumDaoExample);

      //copy into view
      return EntityHelper.buildUserView(userDao, albumDaoList, false, photoDaoImpl);
    }
    return null;
  }

  /**
   *
   * @param username
   * @param albumId
   */
  public UserView getAlbumPhotos(String username, Integer albumId){
    UserDao userDao = getUser(username);
    if(userDao!=null){
      //find albums by userId
      AlbumDao albumDao = new AlbumDao();
      albumDao.setUserDao(userDao);
      Example<AlbumDao> albumDaoExample = Example.of(albumDao);
      List<AlbumDao> albumDaoList = albumDaoImpl.findAll(albumDaoExample);

      //copy into view
      return EntityHelper.buildUserView(userDao, albumDaoList, false, photoDaoImpl);
    }
    return null;
  }

  /**
   *
   * @param username
   * @return
   */
  private UserDao getUser(String username){
    UserDao userDao = new UserDao();
    userDao.setUsername(username);
    Example<UserDao> example = Example.of(userDao);
    return userDaoImpl.find(example);
  }

  /**
   *
   * @param userId
   * @return
   */
  public List<User> getUser(Integer userId){
    if(userId!=null && userId > 0){
      Optional<UserDao> userDao = userDaoRepository.findById(userId);
      if(userDao.isPresent()) {
        return EntityHelper.getUserList(Arrays.asList(userDao.get()));
      }
      return Collections.emptyList();
    }
    return EntityHelper.getUserList(userDaoImpl.findAll());
  }

  /**
   *
   * @param userId
   * @return
   */
  public List<User> getUsers(Integer userId){
    if(userId!=null && userId > 0){
      UserDao userDao = userDaoImpl.findById(userId);
      if(userDao != null) {
        return EntityHelper.getUserList(Arrays.asList(userDao));
      }
      return Collections.emptyList();
    }
    return EntityHelper.getUserList(userDaoImpl.findAll());
  }

  /**
   *
   * @param name
   * @param email
   * @param username
   * @return
   */
  public User addUser(String name, String email, String username){
    UserDao toUpdate = new UserDao();
    toUpdate.setName(name);
    toUpdate.setEmail(email);
    toUpdate.setUsername(username);
    toUpdate = userDaoImpl.save(toUpdate);
    return EntityHelper.getUserList(Arrays.asList(toUpdate)).get(0);
  }

  /**
   *
   * @param name
   * @param username
   * @param email
   * @return
   */
  public User updUser(Integer userId, String name, String username, String email) {
    List<User> list = getUsers(userId);
    if(list!=null && list.size()==1) {
      UserDao toUpdate = new UserDao();
      toUpdate.setId(userId);
      toUpdate.setName(name);
      toUpdate.setEmail(email);
      toUpdate.setUsername(username);
      toUpdate = userDaoImpl.save(toUpdate);
      return EntityHelper.getUserList(Arrays.asList(toUpdate)).get(0);
    }
    return null; //TODO: exception
  }

  /**
   *
   * @param userId
   * @return
   */
  public Boolean delUser(Integer userId) {
    userDaoImpl.deleteById(userId);
    return true;
  }

  /**
   *
   * @param albumId
   * @param userId
   * @return
   */
  public List<Album> getAlbums(Integer albumId, Integer userId){

    if(albumDaoImpl.count() <= 0){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Album userAlbum = new Album();
      userAlbum.setErrorMsg("No Album Data");
      return Arrays.asList(userAlbum);
    }
    if((albumId!=null && albumId > 0) && (userId!=null && userId > 0)){
      List<AlbumDao> albumDaoList = albumDaoImpl.findAll();
      List<AlbumDao> filteredAlbums =
          albumDaoList.stream()
              .filter(userAlbum ->  userAlbum.getId() == albumId && userAlbum.getUserDao().getId()==userId
              )
              .collect(Collectors.toList())
          ;
      return EntityHelper.getAlbumList(filteredAlbums);
    } else {
      //empty ids, or only user id not supported
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Album userAlbum = new Album();
      userAlbum.setErrorMsg("Unsupported operation: Required albumId+userId");
      return Arrays.asList(userAlbum);
    }
  }

  /**
   *
   * @param userId
   * @param title
   * @return
   */
  public Album addAlbum(Integer userId, String title){
    AlbumDao albumDao = new AlbumDao();
    UserDao userDao = new UserDao();
    userDao.setId(userId);
    albumDao.setUserDao(userDao);
    albumDao.setTitle(title);
    return EntityHelper.getAlbumList(Arrays.asList(albumDaoImpl.save(albumDao))).get(0);
  }

  /**
   *
   * @param albumId
   * @param userId
   * @param title
   * @return
   */
  public Album updateAlbum(Integer albumId, Integer userId, String title){
    if(albumDaoImpl.count() <= 0){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Album userAlbum = new Album();
      userAlbum.setErrorMsg("No Album Data");
      return userAlbum;
    }
    AlbumDao albumDao = new AlbumDao();
    albumDao.setId(albumId);
    UserDao userDao = new UserDao();
    userDao.setId(userId);
    albumDao.setUserDao(userDao);
    Example<AlbumDao> example = Example.of(albumDao);
    if(albumDaoImpl.count(example) <=0 ){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Album userAlbum = new Album();
      userAlbum.setErrorMsg("Not Found, Album=" + albumId + ", User=" + userId);
      return userAlbum;
    }
    albumDao.setTitle(title);
    return EntityHelper.getAlbumList(Arrays.asList(albumDaoImpl.save(albumDao))).get(0);
  }

  /**
   *
   * @param albumId
   * @param userId
   * @return
   */
  public Boolean delAlbum(Integer albumId, Integer userId){
    if(albumDaoImpl.count() <= 0){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      return false;
    }
    AlbumDao albumDao = new AlbumDao();
    albumDao.setId(albumId);
    UserDao userDao = new UserDao();
    userDao.setId(userId);
    albumDao.setUserDao(userDao);
    Example<AlbumDao> example = Example.of(albumDao);
    if(albumDaoImpl.count(example) <=0 ){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      return false;
    }
    albumDaoImpl.deleteById(albumId);
    return true;
  }

  /**
   *
   * @param photoId
   * @param albumId
   * @return
   */
  public List<Photo> getPhotos(Integer photoId, Integer albumId){
    if(photoDaoImpl.count() <= 0){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Photo photo = new Photo();
      photo.setErrorMsg("No Album Data");
      return Arrays.asList(photo);
    }
    if((albumId!=null && albumId > 0) && (photoId!=null && photoId > 0)){
      List<PhotoDao> photoDaoList = photoDaoImpl.findAll();
      List<PhotoDao> filteredPhotos =
          photoDaoList.stream()
              .filter(photoDao ->  photoDao.getId() == photoId && photoDao.getAlbumDao().getId()==albumId)
              .collect(Collectors.toList())
          ;
      return EntityHelper.getPhotoList(filteredPhotos);
    } else {
      //empty ids, or only user id not supported
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Photo photo = new Photo();
      photo.setErrorMsg("Unsupported operation: Required albumId+userId");
      return Arrays.asList(photo);
    }

  }

  /**
   *
   * @param albumId
   * @param title
   * @param url
   * @param thumbnailUrl
   * @return
   */
  public Photo addPhoto(Integer albumId, String title, String url, String thumbnailUrl){
    PhotoDao photoDao = new PhotoDao();
    photoDao.setTitle(title);
    photoDao.setUrl(url);
    photoDao.setThumbnailUrl(thumbnailUrl);

    AlbumDao albumDao = new AlbumDao();
    albumDao.setId(albumId);
    photoDao.setAlbumDao(albumDao);

    return EntityHelper.getPhotoList(Arrays.asList(photoDaoImpl.save(photoDao))).get(0);
  }

  /**
   *
   * @param photoId
   * @param albumId
   * @param title
   * @param url
   * @param thumbnailUrl
   * @return
   */
  public Photo updatePhoto(Integer photoId, Integer albumId, String title, String url, String thumbnailUrl){
    if(photoDaoImpl.count() <= 0){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Photo photo = new Photo();
      photo.setErrorMsg("No Album Data");
      return photo;
    }
    PhotoDao photoDao = new PhotoDao();
    photoDao.setId(photoId);
    AlbumDao albumDao = new AlbumDao();
    albumDao.setId(albumId);
    photoDao.setAlbumDao(albumDao);
    Example<PhotoDao> example = Example.of(photoDao);
    if(photoDaoImpl.count(example) <=0 ){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      Photo photo = new Photo();
      photo.setErrorMsg("Not Found, Album=" + albumId + ", PhotoID=" + photoId);
      return photo;
    }
    photoDao.setTitle(title);
    photoDao.setThumbnailUrl(thumbnailUrl);
    photoDao.setUrl(url);
    return EntityHelper.getPhotoList(Arrays.asList(photoDaoImpl.save(photoDao))).get(0);
  }

  /**
   *
   * @param photoId
   * @param albumId
   * @return
   */
  public Boolean delPhoto(Integer photoId, Integer albumId){
    if(photoDaoImpl.count() <= 0){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      return false;
    }
    PhotoDao photoDao = new PhotoDao();
    photoDao.setId(photoId);
    AlbumDao albumDao = new AlbumDao();
    albumDao.setId(albumId);
    photoDao.setAlbumDao(albumDao);
    Example<PhotoDao> example = Example.of(photoDao);
    if(photoDaoImpl.count(example) <=0 ){
      //throw new Exception("Unsupported operation: Required albumId+userId");
      return false;
    }
    photoDaoImpl.deleteById(photoId);
    return true;
  }

  @Autowired private AlbumDaoRepository albumDaoRepository;
  public UserView getAlbumsV2(String username) {
    UserDao userDao = userDaoRepository.findByUsername(username);
    List<AlbumDao> albumDaoList = albumDaoRepository.findByUserDaoUsername(username);
    if(CollectionUtils.isEmpty(albumDaoList)){
      return new UserView();
    }
    return EntityHelper.buildUserView(userDao, albumDaoList, false, photoDaoImpl);
  }


  @Autowired private PhotoDaoRepository photoDaoRepository;
  public List<PhotoDao> getUserAlbumPhotos(Integer userId, Integer albumId) {
    Optional<UserDao> optionalUserDao = userDaoRepository.findById(userId);
    if(optionalUserDao.isPresent()) {
      UserDao userDao = optionalUserDao.get();
      return photoDaoRepository.findByAlbumDaoId(albumId);
    }
    return null;
  }

  @Autowired private DBTasksImpl dbTasks;
  /**
   *
   * @param postRequest
   * @return
   */
  public UserView createNewEntities(PostRequest postRequest) {
    try {

      PhotoDao photoDao = new PhotoDao();
      photoDao.setUrl(postRequest.getUrl());
      photoDao.setThumbnailUrl(postRequest.getThumbnailUrl());
      photoDao.setTitle(postRequest.getPhotoTitle());
      photoDao.setId(dbTasks.getNextPhotoId());

      AlbumDao albumDao = new AlbumDao();
      albumDao.setTitle(postRequest.getAlbumTitle());
      albumDao.setId(dbTasks.getNextAlbumId());

      UserDao userDao = new UserDao();
      userDao.setUsername(postRequest.getUsername());
      userDao.setEmail(postRequest.getEmail());
      userDao.setName(postRequest.getName());
      userDao.setId(dbTasks.getNextUserId());

      userDao.setAlbumDaos(Arrays.asList(albumDao));
      albumDao.setUserDao(userDao);
      albumDao.setPhotoDaos(Arrays.asList(photoDao));
      photoDao.setAlbumDao(albumDao);

      UserDao response = userDaoRepository.save(userDao);
      return EntityHelper.buildUserView(userDao, albumDao, photoDao);
    }catch (Exception e){
      e.printStackTrace();
    }

    return null;
  }
}
