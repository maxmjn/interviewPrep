package com.myProject.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PhotoView {
  private Integer id;
  private Integer albumId;
  private String title;
  private String url;
  private String thumbnailUrl;
}
