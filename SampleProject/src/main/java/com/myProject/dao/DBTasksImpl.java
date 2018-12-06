package com.myProject.dao;

import com.myProject.repository.AlbumDaoRepository;
import com.myProject.repository.PhotoDaoRepository;
import com.myProject.repository.UserDaoRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    entityManager.createNativeQuery("commit ").executeUpdate();
  }
}
