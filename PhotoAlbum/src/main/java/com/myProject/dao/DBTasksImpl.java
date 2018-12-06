package com.myProject.dao;

import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.UserDaoRepository;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
public class DBTasksImpl {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired private UserDaoRepository userDaoRepository;
  @Autowired private AlbumDaoRepository albumDaoRepository;
  @Autowired private PhotoDaoRepository photoDaoRepository;

  //PROPAGATION_REQUIRED - will start a new transaction if the caller has not already started a transaction
  //PROPAGATION_REQUIRES_NEW - will always start a new transaction even if the caller already has a transaction
  @org.springframework.transaction.annotation.Transactional(
      propagation= Propagation.REQUIRED,
      readOnly = true,
      noRollbackFor = EmptyResultDataAccessException.class
  )
  public void clearAllByEntity(){
    userDaoRepository.deleteAll();
    userDaoRepository.flush();

    albumDaoRepository.deleteAll();
    albumDaoRepository.flush();

    photoDaoRepository.deleteAll();
    photoDaoRepository.flush();
    //to handle delete followed by immediate data reload - call adminSvc.removall() then adminSvc.retrieve_LoadDB()
    //entityManager.createNativeQuery("commit ").executeUpdate();
  }

  @Transactional
  public void deleteAll(){
    String[] list = {
        "DELETE FROM USERS",
        "DELETE FROM ALBUM",
        "DELETE FROM PHOTO"
    };
    for(String s: list) {
      entityManager.createNativeQuery(s).executeUpdate();
    }
  }

  @Transactional
  public void restartSequences(){
    String[] list = {
        "ALTER SEQUENCE seq_users restart with 1",
        "ALTER SEQUENCE seq_album restart with 1",
        "ALTER SEQUENCE seq_photo restart with 1"
    };
    for(String s: list) {
      entityManager.createNativeQuery(s).executeUpdate();
    }
  }

  @Transactional
  public int getNextUserId(){
    List<BigInteger> val = entityManager.createNativeQuery("select seq_users.nextVal ").getResultList();
    return val.get(0).intValue();
  }

  @Transactional
  public int getNextAlbumId(){
    List<BigInteger> val = entityManager.createNativeQuery("select seq_album.nextVal ").getResultList();
    return val.get(0).intValue();
  }

  @Transactional
  public int getNextPhotoId(){
    List<BigInteger> val = entityManager.createNativeQuery("select seq_photo.nextVal ").getResultList();
    return val.get(0).intValue();
  }
}
