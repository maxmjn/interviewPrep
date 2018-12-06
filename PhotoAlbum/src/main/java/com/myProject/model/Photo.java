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
@Table(name = "photo")//matches PhotoDao
public class Photo extends RestTemplateError {
  @Id //@Entity requires at least @Id to avoid error No identifier specified for entity
  @SequenceGenerator(name = "seq_photo", sequenceName = "seq_photo", allocationSize = 1) //allocationSize=Sequence increment
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_photo")
  private Integer id;
  private Integer albumId;
  private String title;
  private String url;
  private String thumbnailUrl;

}
