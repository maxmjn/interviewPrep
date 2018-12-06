package com.myProject.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "album")//matches model Album
public class AlbumDao {

  private Integer id;
  private String title;
  private Integer userId;

  @JsonIgnore
  private UserDao userDao;

  @JsonIgnore
  private List<PhotoDao> photoDaos;

  @Override
  public String toString() {
    return "AlbumDao{" +
        "id=" + id +
        ", title='" + title + '\'' +
        '}';
  }

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = UserDao.class)
  @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false)//referential integrity foreign key column in table album
  @OnDelete(action = OnDeleteAction.CASCADE)
  public UserDao getUserDao() {
    return userDao;
  }
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

//  This is required for bidirectional one-to-many association is to allow you to keep a collection of child entities in the parent
  @OneToMany(mappedBy = "albumDao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = PhotoDao.class)
  public List<PhotoDao> getPhotoDaos() {
    return photoDaos;
  }
  public void setPhotoDaos(List<PhotoDao> photoDaos) {
    this.photoDaos = photoDaos;
  }


  @Id
  @SequenceGenerator(name = "seq_album", sequenceName = "seq_album", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_album")
  //@GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_album")
  //either strategy TABLE or SEQUENCE works
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
}
