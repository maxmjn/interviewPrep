package com.myProject.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
  @Value("${spring.jersey.application-path}") private String apiPath;
  @Value("${jersey.scheme}") private String scheme;
  @Value("${jersey.config-id}") private String configId;
  @Value("${jersey.version}") private String apiVersion;
  @Value("${jersey.contact}") private String appContact;
  @Value("${jersey.title}") private String appTitle;
}
