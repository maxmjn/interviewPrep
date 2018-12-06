package com.myProject.repository;

import com.myProject.dao.PhotoDao;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoDaoRepository extends JpaRepository<PhotoDao, Integer> {
  Page<PhotoDao> findByAlbumDaoId(Integer albumId, Pageable pageable);
  List<PhotoDao> findByAlbumDaoId(Integer albumId);
}
