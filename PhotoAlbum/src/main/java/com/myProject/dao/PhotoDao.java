package com.myProject.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "photo")//matches model Photo
public class PhotoDao {

  private Integer id;
  private Integer albumId;
  private String title;
  private String url;
  private String thumbnailUrl;

  @JsonIgnore
  private AlbumDao albumDao;

  @Override
  public String toString() {
    return "PhotoDao{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", url='" + url + '\'' +
        ", thumbnailUrl='" + thumbnailUrl + '\'' +
        '}';
  }

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = AlbumDao.class)
  @JoinColumn(name = "albumId", nullable = false,insertable = false, updatable = false)//referential integrity foreign key column in table photo
  @OnDelete(action = OnDeleteAction.CASCADE)
  public AlbumDao getAlbumDao() {
    return albumDao;
  }
  public void setAlbumDao(AlbumDao albumDao) {
    this.albumDao = albumDao;
  }

  @Id
  @SequenceGenerator(name = "seq_photo", sequenceName = "seq_photo", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_photo")
  //@GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_photo")
  //either strategy TABLE or SEQUENCE works
  public Integer getId() {
    return id;
  }

  @GeneratedValue(strategy= GenerationType.IDENTITY)
  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public Integer getAlbumId() {
    return albumId;
  }

  public void setAlbumId(Integer albumId) {
    this.albumId = albumId;
  }
}
