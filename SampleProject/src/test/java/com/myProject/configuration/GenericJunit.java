package com.myProject.configuration;

import com.myProject.configuration.common.StartServerIT;
import com.myProject.dao.AlbumDao;
import com.myProject.dao.AlbumDaoImpl;
import com.myProject.dao.PhotoDao;
import com.myProject.dao.PhotoDaoImpl;
import com.myProject.dao.UserDao;
import com.myProject.dao.UserDaoImpl;
import com.myProject.model.User;
import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.UserDaoRepository;
import com.myProject.service.v1.AdminSvc;
import com.myProject.service.v1.PhotoAlbumSvc;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.SqlConfig;

public class GenericJunit extends StartServerIT {

  @Autowired private SqlConfig sqlConfig;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired private AdminSvc adminSvc;
  @Autowired private PhotoAlbumSvc photoAlbumSvc;

  @Autowired private UserDaoImpl userDaoImpl;
  @Autowired private AlbumDaoImpl albumDaoImpl;
  @Autowired private PhotoDaoImpl photoDaoImpl;

  @Autowired private AlbumDaoRepository albumDaoRepository;
  @Autowired private UserDaoRepository userDaoRepository;
  @Autowired
  PhotoDaoRepository photoDaoRepository;

  //can't use @Before because it needs static
  //so loading and deleting for each test
  @Before
  public void loadDB(){
    String urlUsers = "https://jsonplaceholder.typicode.com/users?id=1";
    String urlAlbums = "https://jsonplaceholder.typicode.com/albums?userId=1&id=1";
    String urlPhotos = "https://jsonplaceholder.typicode.com/photos?albumId=1";
    List<String> loadDBStatus = adminSvc.retrieve_LoadDB(urlUsers, urlAlbums, urlPhotos);
    System.out.println(loadDBStatus);
  }

  @After
  public void unloadDB(){
    List<String> loadDBStatus = adminSvc.removeAll();
    System.out.println(loadDBStatus);
  }

//  @Ignore
  @Test
  public void testCRUD_byJPA() throws Exception{

    Optional<UserDao> optionalAlbumUser;
    UserDao sample = new UserDao();
    sample.setUsername("test");
    sample.setEmail("test");
    sample.setName("test");

    Example<UserDao> example = Example.<UserDao>of(sample);
    boolean exists = userDaoRepository.exists(example);
    if(exists==false) {

      //Create
      System.out.println("Before Insert count:" + userDaoRepository.count());
      sample = userDaoRepository.save(sample);
      Assert.assertTrue("Create failed", sample.getName().equalsIgnoreCase("TEST"));
      optionalAlbumUser = userDaoRepository.findOne(example);
      if(optionalAlbumUser.isPresent()){
        //Update
        sample.setName("Update");
        sample = userDaoRepository.save(sample);
        optionalAlbumUser = userDaoRepository.findOne(example);
        if (optionalAlbumUser.isPresent()) {
          UserDao userDao = optionalAlbumUser.get();
          Assert.assertTrue("Update Failed", userDao.getName().equalsIgnoreCase("UPDATE"));
        }

        //Delete
        userDaoRepository.delete(sample);
        System.out.println("After delete count:" + userDaoRepository.count());
        optionalAlbumUser = userDaoRepository.findOne(example);
        Assert.assertTrue("Delete failed", optionalAlbumUser.isPresent()==false);
      }
    }
  }

//  @Ignore
  @Test
  public void testCRUDUser(){
    List<User> userList = photoAlbumSvc.getUsers(null);
    System.out.println("USERS:" + userList);
    userList = photoAlbumSvc.getUsers(1000);
    System.out.println("EMPTY USER -->" + userList);
    userList = photoAlbumSvc.getUsers(4);
    System.out.println("A USER -->" + userList);
    Assert.assertTrue("GET USER failed", userList.get(0).getId()==4);

    int id = userList.get(0).getId();
    User updated = photoAlbumSvc.updUser(id, "test", "test", "test");
    System.out.println("UPDATE USER <-->" + updated);
    Assert.assertTrue("UPDATE USER failed", updated.getName()=="test");

    String sqlAlbumCnt = "select count(1) from album";
    Integer albumCnt = jdbcTemplate.queryForObject(sqlAlbumCnt, Integer.class);
    String sqlPhotoCnt = "select count(1) from photo";
    Integer photoCnt = jdbcTemplate.queryForObject(sqlPhotoCnt, Integer.class);
    System.out.println("BEFORE DELETE: Album count="+ albumCnt + ", Photo count=" + photoCnt);
    Assert.assertTrue("DELETE failed", photoAlbumSvc.delUser(id));
    Integer newAlbumCnt = jdbcTemplate.queryForObject(sqlAlbumCnt, Integer.class);
    Integer newPhotoCnt = jdbcTemplate.queryForObject(sqlPhotoCnt, Integer.class);
    System.out.println("AFTER DELETE: Album count="+ newAlbumCnt + ", Photo count=" + newPhotoCnt);
    Assert.assertTrue("DELETE USER count failed", albumCnt > newAlbumCnt && photoCnt > newPhotoCnt);
  }

//  @Ignore
  @Test
  public void testCreateAll() {
    PhotoDao photoDao = new PhotoDao();
    photoDao.setUrl("black");
    photoDao.setThumbnailUrl("black");
    photoDao.setTitle("Black");

    AlbumDao albumDao = new AlbumDao();
    albumDao.setTitle("Black Magic");

    UserDao userDao = new UserDao();
    userDao.setUsername("test");
    userDao.setEmail("test");
    userDao.setName("test");

    userDao.setAlbumDaos(Arrays.asList(albumDao));
    albumDao.setUserDao(userDao);
    albumDao.setPhotoDaos(Arrays.asList(photoDao));
    photoDao.setAlbumDao(albumDao);

    UserDao response1 = userDaoImpl.save(userDao);
    String sqlAlbumCnt = "select count(1) from album";
    Integer albumCnt = jdbcTemplate.queryForObject(sqlAlbumCnt, Integer.class);
    String sqlPhotoCnt = "select count(1) from photo";
    Integer photoCnt = jdbcTemplate.queryForObject(sqlPhotoCnt, Integer.class);
    System.out.println("BEFORE DELETE: Album count="+ albumCnt + ", Photo count=" + photoCnt);
  }
}
