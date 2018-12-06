package com.myProject.component;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.myProject.configuration.AppConfig;
import com.myProject.resource.v1.Admin;
import com.myProject.resource.v1.EntityCRUD;
import com.myProject.resource.v1.PhotoAlbum;
import com.myProject.resource.v1.PingPong;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JerseyConfig.class);
  @Context
  ServletContext servletContext;
  @Autowired
  AppConfig applicationConfig;

  public JerseyConfig() {
    // Register endpoints, providers, ...
    this.registerEndpoints();
  }

  @PostConstruct
  public void init() {
    // Register component where DI is needed
    this.configureSwagger();
  }

  private void registerEndpoints() {
    this.register(Admin.class);
    this.register(PingPong.class);
    this.register(PhotoAlbum.class);
    this.register(EntityCRUD.class);

    this.register(new JacksonJaxbJsonProvider() {
      @Override
      public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations,
          MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
          InputStream entityStream) throws IOException {
        try {
          return super.readFrom(type, genericType, annotations, mediaType, httpHeaders,
              entityStream);
        } catch (InvalidFormatException e) {
          LOGGER.error("Error de-serializing JSON entity", e);
          throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
              .entity(Entity.entity(e.getMessage(), MediaType.TEXT_PLAIN)).build());
        }
      }
    });
  }

  private void configureSwagger() {
    this.register(ApiListingResource.class);
    this.register(SwaggerSerializers.class);

    BeanConfig config = new BeanConfig();
    config.setConfigId(applicationConfig.getConfigId());
    config.setTitle(applicationConfig.getAppTitle());
    config.setVersion(applicationConfig.getApiVersion());
    config.setContact(applicationConfig.getAppContact());
    config.setSchemes(new String[] {applicationConfig.getScheme()});
    config.setBasePath(applicationConfig.getApiPath());
    config.setResourcePackage("com.myProject.resource");
    config.setPrettyPrint(true);
    config.setScan(true);
  }
}
