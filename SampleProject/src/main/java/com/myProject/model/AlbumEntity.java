package com.myProject.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Facilitate JSON to DB. This is allows to fetch each API User/Albums/Photos separately and load into DB
 * without checking for referential integrity. To apply referential integrity there's a separate AlbumDao
 */
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "album")
public class AlbumEntity {

  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer userId;
  private String title;
}
