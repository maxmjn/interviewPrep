package com.myProject.view;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserView {
  private Integer id;
  private String name;
  private String username;
  private String email;
  private List<AlbumView> albumViewList;
}
