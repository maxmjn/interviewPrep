package com.myProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostRequest {

  private String name;
  private String username;
  private String email;
  private String albumTitle;
  private String photoTitle;
  private String url;
  private String thumbnailUrl;
}
