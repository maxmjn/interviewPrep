package com.myProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myProject.error.RestTemplateError;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "users")//matches UserDao
public class User extends RestTemplateError {
  @Id //@Entity requires at least @Id to avoid error No identifier specified for entity
  @SequenceGenerator(name = "seq_users", sequenceName = "seq_users", allocationSize = 1) //allocationSize=Sequence increment
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users")
  private Integer id;
  private String name;
  private String username;
  private String email;

}
