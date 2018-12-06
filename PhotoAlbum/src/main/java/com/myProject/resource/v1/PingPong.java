package com.myProject.resource.v1;


import com.myProject.service.v1.PingPongSvc;
import com.myProject.model.PingPongModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/v1/")
@Produces("application/json")
@Api("HealthCheck")
public class PingPong {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingPong.class);

    public PingPong() {
    }

    @Autowired
    PingPongSvc pingPongSvc;

    @GET
    @Path("/health/pingpong")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Server health check", notes = "Check if server is up", response = PingPongModel.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Server up"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 403, message = "Not permitted to access this API"),
        @ApiResponse(code = 500, message = "Unexpected error")
    })
    public PingPongModel pong(
        @ApiParam(value = "value")
        @QueryParam("value") String value
    ){
      LOGGER.info(PingPong.class.getSimpleName());
      return pingPongSvc.pingPong(value);
    }
}
