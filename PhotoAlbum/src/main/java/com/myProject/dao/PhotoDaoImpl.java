package com.myProject.dao;

import com.myProject.model.Photo;
import com.myProject.repository.PhotoDaoRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class PhotoDaoImpl {

  @Autowired private PhotoDaoRepository photoDaoRepository;
  @PersistenceContext private  EntityManager entityManager;

  public static List<PhotoDao> getUserAlbumDao(List<Photo> photos){
    List<PhotoDao> photoDaoList = new LinkedList<>();
    for(Photo up: photos){
      PhotoDao photoDao = new PhotoDao();
      BeanUtils.copyProperties(up, photoDao, "");
      photoDaoList.add(photoDao);
    }
    return photoDaoList;
  }

  @Transactional
  public int getMaxId(){
    //to handle multiple threads
    synchronized (PhotoDaoImpl.class) {
      List<Integer> max = entityManager.createNativeQuery("select NVL(max(id), 0)+1 from photo")
          .getResultList();
      return max.get(0);
    }
  }

  public long count(){
    return photoDaoRepository.count();
  }

  public long count(Example<PhotoDao> example){
    return photoDaoRepository.count(example);
  }

  @Transactional
  public List<PhotoDao> saveAll(List<PhotoDao> userList){
    return photoDaoRepository.saveAll(userList);
  }
  @Transactional
  public PhotoDao save(PhotoDao photoDao){

    return photoDaoRepository.save(photoDao);
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public PhotoDao findById(Integer id){
    Optional<PhotoDao> photoDao = photoDaoRepository.findById(id);
    if(photoDao.isPresent()){
      return photoDao.get();
    }
    return null;
  }
  @PersistenceContext
  private EntityManager em;

  @Transactional
  public List<PhotoDao> findByAlbumId(Integer albumId){
    return em.createQuery("SELECT p FROM PhotoDao p WHERE p.albumDao.id = :albumId", PhotoDao.class)
        .setParameter( "albumId", albumId )
        .getResultList();
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public List<PhotoDao> findAll(){
    return photoDaoRepository.findAll();
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public List<PhotoDao> findAll(Example<PhotoDao> example){
    return photoDaoRepository.findAll(example);
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public void deleteById(Integer id){
    photoDaoRepository.deleteById(id);
  }
}
