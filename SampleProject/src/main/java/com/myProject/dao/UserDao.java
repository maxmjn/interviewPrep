package com.myProject.dao;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "users")
public class UserDao {

  private Integer id;
  private String name;
  private String username;
  private String email;

  private List<AlbumDao> albumDaos = new LinkedList<>();

  @Override
  public String toString() {
    return "UserDao{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        '}';
  }

//  This is required for bidirectional one-to-many association is to allow you to keep a collection of child entities in the parent
  @OneToMany(mappedBy = "userDao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  public List<AlbumDao> getAlbumDaos() {
    return albumDaos;
  }
  public void setAlbumDaos(List<AlbumDao> albumDaos) {
    this.albumDaos = albumDaos;
  }

  @Id
  @GeneratedValue//(strategy= GenerationType.IDENTITY)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
