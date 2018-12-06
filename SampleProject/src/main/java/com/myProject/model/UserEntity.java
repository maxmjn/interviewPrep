package com.myProject.model;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * Facilitate JSON to DB. This is allows to fetch each API User/Albums/Photos separately and load into DB
 * without checking for referential integrity. To apply referential integrity there's a separate UserDao
 */
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String username;
  private String email;

}
