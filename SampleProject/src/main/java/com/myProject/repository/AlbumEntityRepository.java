package com.myProject.repository;

import com.myProject.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumEntityRepository extends JpaRepository<AlbumEntity, Integer> {

}
