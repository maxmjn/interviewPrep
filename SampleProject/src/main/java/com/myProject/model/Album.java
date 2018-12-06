package com.myProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myProject.error.RestTemplateError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Album extends RestTemplateError {

  private Integer id;
  private Integer userId;
  private String title;

}
