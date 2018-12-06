package com.myProject.util;

import com.myProject.dao.AlbumDao;
import com.myProject.dao.PhotoDao;
import com.myProject.dao.PhotoDaoImpl;
import com.myProject.dao.UserDao;
import com.myProject.model.Album;
import com.myProject.model.AlbumEntity;
import com.myProject.model.Photo;
import com.myProject.model.PhotoEntity;
import com.myProject.model.User;
import com.myProject.model.UserEntity;
import com.myProject.view.AlbumView;
import com.myProject.view.PhotoView;
import com.myProject.view.UserView;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class EntityHelper {

  /**
   *
   * @param userDao
   * @param albumDao
   * @param photoDao
   * @return
   */
  public static UserView buildUserView(UserDao userDao, AlbumDao albumDao, PhotoDao photoDao){
    UserView userView = new UserView();
    if(userDao!=null) {
      BeanUtils.copyProperties(userDao, userView, "albumDaos");
    }
    List<AlbumView> albumViewList = userView.getAlbumViewList();
    if(albumViewList==null){
      albumViewList = new LinkedList<>();
      userView.setAlbumViewList(albumViewList);
    }
    AlbumView albumView = new AlbumView();
    Integer albumId = -1;
    if(albumDao!=null) {
      BeanUtils.copyProperties(albumDao, albumView, "userDao, photoDaos");
      albumViewList.add(albumView);
      albumId = albumDao.getId();
    }
    if(photoDao!=null){
      List<PhotoView> photoViewList = albumView.getPhotoViewList();
      if(photoViewList==null){
        photoViewList = new LinkedList<>();
        albumView.setPhotoViewList(photoViewList);
      }
      PhotoView photoView = new PhotoView();
      photoView.setAlbumId(albumId);
      BeanUtils.copyProperties(photoDao, photoView, "albumDao");
      photoViewList.add(photoView);
    }
    return userView;
  }
  /**
   *
   * @param userDao
   * @param albumDaoList
   * @return
   */
  public static UserView buildUserView(UserDao userDao, List<AlbumDao> albumDaoList, boolean getPhotos, PhotoDaoImpl photoDaoImpl){
    UserView userView = new UserView();
    BeanUtils.copyProperties(userDao, userView, "albumDaos");
    List<AlbumView> albumViewList = userView.getAlbumViewList();
    if(albumViewList==null){
      albumViewList = new LinkedList<>();
      userView.setAlbumViewList(albumViewList);
    }
    for(AlbumDao ad: albumDaoList) {
      AlbumView albumView = new AlbumView();
      BeanUtils.copyProperties(ad, albumView, "userDao, photoDaos");
      albumViewList.add(albumView);

      //find photos by albumId
      if(getPhotos && photoDaoImpl!=null) {
        Integer albumId = ad.getId();
        List<PhotoDao> photoDaoList = photoDaoImpl.findByAlbumId(albumId);
        List<PhotoView> photoViewList = albumView.getPhotoViewList();
        if(photoViewList==null){
          photoViewList = new LinkedList<>();
          albumView.setPhotoViewList(photoViewList);
        }
        for (PhotoDao pd : photoDaoList) {
          PhotoView photoView = new PhotoView();
          photoView.setAlbumId(albumId);
          BeanUtils.copyProperties(pd, photoView, "albumDao");
          photoViewList.add(photoView);
        }//for-PhotoDao
      }//if-getPhotos

    }//for-AlbumDao
    return userView;
  }

  /**
   *
   * @param userDaoList
   * @return
   */
  public static List<User> getUserList(List<UserDao> userDaoList){
    List<User> userList = new LinkedList<>();
    for(UserDao userDao:userDaoList){
      User user = new User();
      BeanUtils.copyProperties(userDao, user, "");
      userList.add(user);
    }
    return userList;
  }

  /**
   *
   * @param albumDaoList
   * @return
   */
  public static List<Album> getAlbumList(List<AlbumDao> albumDaoList){
    List<Album> albumList = new LinkedList<>();
    for(AlbumDao albumDao:albumDaoList){
      Album album = new Album();
      BeanUtils.copyProperties(albumDao, album, "");
      albumList.add(album);
    }
    return albumList;
  }

  /**
   *
   * @param photoDaoList
   * @return
   */
  public static List<Photo> getPhotoList(List<PhotoDao> photoDaoList){
    List<Photo> photoList = new LinkedList<>();
    for(PhotoDao photoDao:photoDaoList){
      Photo photo = new Photo();
      BeanUtils.copyProperties(photoDao, photo, "");
      photoList.add(photo);
    }
    return photoList;
  }

  /**
   *
   * @param users
   * @return
   */
  public static List<UserEntity> getUserEntity(List<User> users){
    List<UserEntity> userEntityList = new LinkedList<>();
    for(User u:users){
      UserEntity userEntity = new UserEntity();
      BeanUtils.copyProperties(u, userEntity, "");
      userEntityList.add(userEntity);
    }
    return userEntityList;
  }

  /**
   *
   * @param albums
   * @return
   */
  public static List<AlbumEntity> getAlbumEntity(List<Album> albums){
    List<AlbumEntity> albumEntityList = new LinkedList<>();
    for(Album album:albums){
      AlbumEntity albumEntity = new AlbumEntity();
      BeanUtils.copyProperties(album, albumEntity, "");
      albumEntityList.add(albumEntity);
    }
    return albumEntityList;
  }

  /**
   *
   * @param photos
   * @return
   */
  public static List<PhotoEntity> getPhotoEntity(List<Photo> photos){
    List<PhotoEntity> photoEntityList = new LinkedList<>();
    for(Photo photo:photos){
      PhotoEntity photoEntity = new PhotoEntity();
      BeanUtils.copyProperties(photo, photoEntity, "");
      photoEntityList.add(photoEntity);
    }
    return photoEntityList;
  }

  public static List<AlbumDao> getAlbumDao(List<AlbumEntity> albumEntityList){
    List<AlbumDao> albumDaoList = new LinkedList<>();
    for(AlbumEntity album:albumEntityList){
      AlbumDao albumDao = new AlbumDao();
      BeanUtils.copyProperties(album, albumDao, "");
      albumDaoList.add(albumDao);
    }
    return albumDaoList;
  }

  public static List<PhotoDao> getPhotoDao(List<PhotoEntity> photoEntityList){
    List<PhotoDao> photoDaoList = new LinkedList<>();
    for(PhotoEntity photoEntity:photoEntityList){
      PhotoDao photoDao = new PhotoDao();
      BeanUtils.copyProperties(photoEntity, photoEntity, "");
      photoDaoList.add(photoDao);
    }
    return photoDaoList;
  }
}
