package com.myProject.resource.v1;


import com.myProject.dao.PhotoDao;
import com.myProject.model.Album;
import com.myProject.model.Photo;
import com.myProject.model.User;
import com.myProject.service.v1.PhotoAlbumSvc;
import com.myProject.view.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Api("EntityCRUD")
@Path("/v1/photoAlbum")
@Produces("application/json")
public class EntityCRUD {
  private static final Logger LOGGER = LoggerFactory.getLogger(EntityCRUD.class);

  public void EntityCRUD(){
  }

  @Autowired
  PhotoAlbumSvc photoAlbumSvc;

  @GET
  @Path("/users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 10, value = "List of users", notes = "List of users", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of users"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public List<User> users(
      @ApiParam(value = "userId")
      @QueryParam("userId") Integer userId
  ){
    LOGGER.info(EntityCRUD.class.getSimpleName());
    return photoAlbumSvc.getUsers(userId);
  }

  @POST
  @Path("/users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 11, value = "Create user", notes = "Create user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Create user"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public User addUser(
      @ApiParam(value = "name")
      @QueryParam("name") String name,
      @ApiParam(value = "email")
      @QueryParam("email") String email,
      @ApiParam(value = "username")
      @QueryParam("username") String username
  ){
    LOGGER.info(EntityCRUD.class.getSimpleName());
    return photoAlbumSvc.addUser(name, username, email);
  }

  @PUT
  @Path("/users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 12, value = "Update user", notes = "Update user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Update user"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public User updUser(
      @ApiParam(value = "name")
      @QueryParam("name") String name,
      @ApiParam(value = "email")
      @QueryParam("email") String email,
      @ApiParam(value = "username")
      @QueryParam("username") String username,
      @ApiParam(value = "userId")
      @QueryParam("userId") Integer userId
  ){
    LOGGER.info(EntityCRUD.class.getSimpleName());
    return photoAlbumSvc.updUser(userId, name, username, email);
  }

  @DELETE
  @Path("/users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 13, value = "Delete User", notes = "Delete User", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Delete User"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public Boolean delUser(
      @ApiParam(value = "userId")
      @QueryParam("userId") Integer userId
  ){
    LOGGER.info(EntityCRUD.class.getSimpleName());
    return photoAlbumSvc.delUser(userId);
  }

  @GET
  @Path("/albums")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 20, value = "List of albums", notes = "List of albums", response = Album.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of albums"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public List<Album> albums(
      @ApiParam(value = "userId")
      @QueryParam("userId") Integer userId,
      @ApiParam(value = "albumId")
      @QueryParam("albumId") Integer albumId
  ){
    LOGGER.info(EntityCRUD.class.getSimpleName());
    return photoAlbumSvc.getAlbums(albumId, userId);
  }

  @GET
  @Path("/photos")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(position = 30, value = "List of photos", notes = "List of userAlbum's user photos", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of userAlbum's user photos"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public List<Photo> photos(
      @ApiParam(value = "albumId")
      @QueryParam("albumId") Integer albumId,
      @ApiParam(value = "photoId")
      @QueryParam("photoId") Integer photoId
  ){
    LOGGER.info(EntityCRUD.class.getSimpleName());
    return photoAlbumSvc.getPhotos(photoId, albumId);
  }
}
