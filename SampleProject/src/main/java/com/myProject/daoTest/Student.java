package com.myProject.daoTest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Student {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String passportNumber;
}
