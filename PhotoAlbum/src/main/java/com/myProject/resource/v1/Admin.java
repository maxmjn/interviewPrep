package com.myProject.resource.v1;

import com.myProject.service.v1.AdminSvc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/v1/photoAlbum")
@Produces("application/json")
@Api("Admin")
public class Admin {

  private static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);

  public void Admin(){
  }

  @Autowired
  AdminSvc adminSvc;

  @GET
  @Path("/loadAll")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
      value = "Loads users,albums,photos", notes = "Loads users,albums,photos",
      response = String.class, responseContainer = "List"
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Loads users,albums,photos"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public List<String> loadAll(
      @ApiParam("urlUsers") @QueryParam("urlUsers") @DefaultValue("https://jsonplaceholder.typicode.com/users")
      String urlUsers,
      @ApiParam("urlAlbums") @QueryParam("urlAlbums") @DefaultValue("https://jsonplaceholder.typicode.com/albums")
          String urlAlbums,
      @ApiParam("urlPhotos") @QueryParam("urlPhotos") @DefaultValue("https://jsonplaceholder.typicode.com/photos")
          String urlPhotos
  ){
    LOGGER.info(PhotoAlbum.class.getSimpleName());
    return adminSvc.retrieve_LoadDB(urlUsers, urlAlbums, urlPhotos);
  }

  @DELETE
  @Path("/resetAll")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
      value = "Removes users,albums,photos", notes = "Removes users,albums,photos",
      response = String.class, responseContainer = "List"
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Removes users,albums,photos"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 403, message = "Not permitted to access this API"),
      @ApiResponse(code = 500, message = "Unexpected error")
  })
  public List<String> removeAll(){
    LOGGER.info(PhotoAlbum.class.getSimpleName());
    return adminSvc.resetAll();
  }
}
