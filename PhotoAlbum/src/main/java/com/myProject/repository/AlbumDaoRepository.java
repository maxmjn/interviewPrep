package com.myProject.repository;

import com.myProject.dao.AlbumDao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumDaoRepository extends JpaRepository<AlbumDao, Integer> {
  List<AlbumDao> findByUserDaoId(Integer userId);//userDaoId is property of UserDao
  List<AlbumDao> findByUserDaoUsername(String username); //userDao.userName is property of UserDao
}
