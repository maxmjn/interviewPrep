package com.myProject.view;


import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumView {
  private Integer id;
  private String title;
  private List<PhotoView> photoViewList;
}
