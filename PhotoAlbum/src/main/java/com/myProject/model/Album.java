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
@Table(name = "album") //matches AlbumDao
public class Album extends RestTemplateError {
  @Id //@Entity requires at least @Id to avoid error No identifier specified for entity
  @SequenceGenerator(name = "seq_album", sequenceName = "seq_album", allocationSize = 1) //allocationSize=Sequence increment
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_album")
  private Integer id;
  private Integer userId;
  private String title;

}
