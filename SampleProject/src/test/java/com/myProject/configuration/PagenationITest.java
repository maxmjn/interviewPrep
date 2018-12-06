package com.myProject.configuration;

import com.myProject.dao.PhotoDao;
import com.myProject.dao.PhotoDaoImpl;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.service.v1.AdminSvc;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagenationITest {


  @Autowired private PhotoDaoImpl photoDaoImpl;

  @Autowired private PhotoDaoRepository photoDaoRepository;

  @Autowired private AdminSvc adminSvc;

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

  @Ignore
  @Test
  public void testPagination() {
    List<PhotoDao> list = photoDaoImpl.findByAlbumId(2);
    Pageable pp1 = new PP();
    ((PP) pp1).setPageNum(0);
    ((PP) pp1).setSize(5);
    ((PP) pp1).setSort(Sort.by("id"));
    Page<PhotoDao> photoDaoPage = photoDaoRepository.findByAlbumDaoId(2, pp1);
    System.out.println(photoDaoPage.getContent());
  }

  @Getter
  @Setter
  class PP implements Pageable{

    int pageNum;
    int size;
    Sort sort;

    @Override
    public int getPageNumber() {
      return pageNum;
    }

    @Override
    public int getPageSize() {
      return size;
    }

    @Override
    public long getOffset() {
      return 0;
    }

    @Override
    public Sort getSort() {
      return sort;
    }

    @Override
    public Pageable next() {
      this.pageNum++;
      return this;
    }

    @Override
    public Pageable previousOrFirst() {
      return null;
    }

    @Override
    public Pageable first() {
      return null;
    }

    @Override
    public boolean hasPrevious() {
      return false;
    }
  }
}
