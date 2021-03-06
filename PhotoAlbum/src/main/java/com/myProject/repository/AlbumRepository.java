package com.myProject.repository;

import com.myProject.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//External api that provides data also provides foreign keys so without this @Repository we have 2 problems
//AlbumDaoRepository is used for Create api to add to DB and use @GeneratedValue for auto-generation of Id keys
//For Initial load and resetAll data use AlbumRepository
@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
