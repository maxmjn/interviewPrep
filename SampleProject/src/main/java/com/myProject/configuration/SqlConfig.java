package com.myProject.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:sql/Query.xml"})
@Getter
public class SqlConfig {

  @Value("${albumUsers}") private String albumUsers;
  @Value("${albumUser_ByName}") private String albumUser_ByName;
}
