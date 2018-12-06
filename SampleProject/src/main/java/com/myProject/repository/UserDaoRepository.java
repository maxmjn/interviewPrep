package com.myProject.repository;

import com.myProject.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDaoRepository extends JpaRepository<UserDao, Integer> {
  UserDao findByUsername(String username);
}
