package com.myProject.dao;

import com.myProject.model.Album;
import com.myProject.repository.AlbumDaoRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AlbumDaoImpl {

  @Autowired private AlbumDaoRepository albumDaoRepository;
  @PersistenceContext private EntityManager entityManager;

  public static List<AlbumDao> getUserAlbumDao(List<Album> albums){
    List<AlbumDao> albumDaoList = new LinkedList<>();
    for(Album ua: albums){
      AlbumDao albumDao = new AlbumDao();
      BeanUtils.copyProperties(ua, albumDao, "");
      albumDaoList.add(albumDao);
    }
    return albumDaoList;
  }

  @Transactional
  public int getMaxId(){
    //to handle multiple threads
    synchronized (AlbumDaoImpl.class) {
      List<Integer> max = entityManager.createNativeQuery("select NVL(max(id), 0)+1 from album")
          .getResultList();
      return max.get(0);
    }
  }

  public long count(){
    return albumDaoRepository.count();
  }

  public long count(Example<AlbumDao> example){
    return albumDaoRepository.count(example);
  }

  @Transactional
  public AlbumDao save(AlbumDao albumDao){
    return albumDaoRepository.save(albumDao);
  }

  @Transactional
  public List<AlbumDao> saveAll(List<AlbumDao> albumDaoList){
    return albumDaoRepository.saveAll(albumDaoList);
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public AlbumDao findById(Integer id){
    Optional<AlbumDao> albumDao = albumDaoRepository.findById(id);
    if(albumDao.isPresent()){
      return albumDao.get();
    }
    return null;
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public List<AlbumDao> findAll(Example<AlbumDao> example){
    return albumDaoRepository.findAll(example);
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public List<AlbumDao> findAll(){
    return albumDaoRepository.findAll();
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public void deleteById(Integer id){
    albumDaoRepository.deleteById(id);
  }
}
