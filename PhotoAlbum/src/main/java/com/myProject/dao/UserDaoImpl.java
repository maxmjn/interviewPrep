package com.myProject.dao;

import com.myProject.model.User;
import com.myProject.repository.UserDaoRepository;
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
import org.springframework.transaction.annotation.Propagation;

@Service
public class UserDaoImpl {

  @Autowired private UserDaoRepository userDaoRepository;

  @PersistenceContext private EntityManager entityManager;

  public static List<UserDao> getUserDao(List<User> users){
    List<UserDao> userDaoList = new LinkedList<>();
    for(User u: users){
      UserDao userDao = new UserDao();
      userDao.setId(u.getId());
      BeanUtils.copyProperties(u, userDao, "");
      userDaoList.add(userDao);
    }
    return userDaoList;
  }

  @Transactional
  public int getMaxId(){
    //to handle multiple threads
    synchronized (UserDaoImpl.class) {
      List<Integer> max = entityManager.createNativeQuery("select NVL(max(id), 0)+1 from users")
          .getResultList();
      return max.get(0);
    }
  }

  public long count(){
    return userDaoRepository.count();
  }

  public long count(Example<UserDao> example){
    return userDaoRepository.count(example);
  }

  public boolean exists(Example<UserDao> example){
    return userDaoRepository.exists(example);
  }

  @Transactional
  public UserDao save(UserDao userDao){

    return userDaoRepository.save(userDao);
  }

  @Transactional
  public List<UserDao> saveAll(List<UserDao> userDaoList){

    return userDaoRepository.saveAll(userDaoList);
  }

  /**
   * Without @Transactional you will run into failed to lazily initialize a collection of role: com.myProject.dao.UserDao.albumDaos, could not initialize proxy - no Session (through reference chain: java.util.ArrayList[0]->com.myProject.dao.UserDao["albumDaos"])
   * Reason is JPA controller session is ended once you make userDaoRepository.find* call
   * @param id
   * @return
   */
  @Transactional
//  @org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public UserDao findById(Integer id){
    Optional<UserDao> userDao = userDaoRepository.findById(id);
    if(userDao.isPresent()){
      return userDao.get();
    }
    return null;
  }

  @Transactional
//  @org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public UserDao find(Example<UserDao> example){
    Optional<UserDao> userDao = userDaoRepository.findOne(example);
    if(userDao.isPresent()){
      return userDao.get();
    }
    return null;
  }

  @Transactional
//  @org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public List<UserDao> findAll(){
    return userDaoRepository.findAll();
  }

  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public void deleteById(Integer id){
    userDaoRepository.deleteById(id);
  }
  @Transactional
  //@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
  public void delete(UserDao userDao){
    userDaoRepository.delete(userDao);
  }
}
