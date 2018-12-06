package com.myProject.resource.v1;


import com.myProject.dao.PhotoDao;
import com.myProject.model.PostRequest;
import com.myProject.service.v1.PhotoAlbumSvc;
import com.myProject.view.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Api("PhotoAlbum")
@Path("/v1/photoAlbum")
@Produces("application/json")
public class PhotoAlbum {
  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoAlbum.class);

  public void PhotoAlbum(){
  }

  @Autowired
  PhotoAlbumSvc photoAlbumSvc;

  @GET
  @Path("/user/albums")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 1, value = "List of user albums", notes = "List of user albums", response = UserView.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of user albums"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })

  public UserView userAlbums(
      @ApiParam(value = "un")
      @QueryParam("un") String username
  ){
    LOGGER.info(PhotoAlbum.class.getSimpleName());
    return photoAlbumSvc.getAlbumsV2(username);
  }


  @GET
  @Path("/user/{userId}/albums/{albumId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 1, value = "List of user album photos", notes = "List of user album photos",
      response = PhotoDao.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of user album photos"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public List<PhotoDao> userAlbumPhotos(
      @PathParam("userId") Integer userId,
      @PathParam("albumId") Integer albumId
  ){
    LOGGER.info(PhotoAlbum.class.getSimpleName());
    return photoAlbumSvc.getUserAlbumPhotos(userId, albumId);
  }


  @POST
  @Path("/create")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 1, value = "Create user album photos", notes = "Create user album photos", response = UserView.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Create user album photos"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public UserView createUserAlbumPhoto(
      @ApiParam(value = "data") PostRequest postRequest
  ){
    LOGGER.info(PhotoAlbum.class.getSimpleName());
    return photoAlbumSvc.addAllEntities(postRequest);
  }
}
