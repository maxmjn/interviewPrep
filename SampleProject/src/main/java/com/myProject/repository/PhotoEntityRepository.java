package com.myProject.repository;

import com.myProject.model.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoEntityRepository extends JpaRepository<PhotoEntity, Integer> {
}
