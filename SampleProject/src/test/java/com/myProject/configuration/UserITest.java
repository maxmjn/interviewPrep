package com.myProject.configuration;

import com.myProject.configuration.common.StartServerIT;
import com.myProject.dao.DBTasksImpl;
import com.myProject.dao.UserDao;
import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.UserDaoRepository;
import com.myProject.service.v1.AdminSvc;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserITest extends StartServerIT {

  @Autowired private UserDaoRepository userDaoRepository;
  @Autowired private AlbumDaoRepository albumDaoRepository;
  @Autowired private PhotoDaoRepository photoDaoRepository;
  @Autowired private DBTasksImpl dbTasks;
  @Autowired private AdminSvc adminSvc;

  private static String USERNAME = "Bret";
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
    List<String> loadDBStatus = adminSvc.removeAll();
    System.out.println(loadDBStatus);
  }

  //@Ignore
  @Test
  public void testGetByUsername(){
    UserDao userDao = userDaoRepository.findByUsername(USERNAME);
    System.out.println(userDao);
    Assert.assertTrue("GetByUsername failed",
          userDao.getUsername().equalsIgnoreCase(USERNAME) &&
              userDao.getId() > 0
        );
  }

  //@Ignore
  @Test
  public void testDeleteAll_Reload(){
    dbTasks.clearAllByEntity();
    System.out.println(adminSvc.retrieve_LoadDB(urlUsers, urlAlbums, urlPhotos));
    System.out.println(
        "Users:"+ userDaoRepository.count() +
        ",Albums:"+albumDaoRepository.count() +
            ",Photos:"+photoDaoRepository.count());
    Assert.assertTrue("DeleteAll - Reload failed",
        userDaoRepository.count() > 0 &&
            albumDaoRepository.count( ) > 0 &&
        photoDaoRepository.count() > 0
    );
  }

}
