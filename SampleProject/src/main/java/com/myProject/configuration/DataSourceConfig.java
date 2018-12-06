package com.myProject.configuration;


import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.hikari.username}") private String username;
  @Value("${spring.datasource.hikari.password}") private String password;
  @Value("${spring.datasource.hikari.url}") private String url;
  @Value("${spring.datasource.hikari.max-lifetime}") private long maxLifetime;
  @Value("${spring.datasource.hikari.leak-detection-threshold}") private long leakDetectionThreshold;
  @Value("${spring.datasource.hikari.pool-name}") private String poolName;

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean(destroyMethod = "close")
  @Primary
  public DataSource primaryDataSource() {
    final HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setPoolName(poolName);
    dataSource.setMaxLifetime(maxLifetime);
    dataSource.setLeakDetectionThreshold(leakDetectionThreshold);

    //For SSL
//    Properties prop = new Properties();
//    prop.setProperty("oracle.net.ssl_cipher_suites", "(SSL_RSA_WITH_3DES_EDE_CBC_SHA, SSL_RSA_WITH_RC4_128_SHA,SSL_RSA_WITH_RC4_128_MD5,SSL_RSA_WITH_DES_CBC_SHA,SSL_DH_anon_WITH_3DES_EDE_CBC_SHA,SSL_DH_anon_WITH_RC4_128_MD5,SSL_DH_anon_WITH_DES_CBC_SHA,SSL_RSA_EXPORT_WITH_RC4_40_MD5,SSL_RSA_EXPORT_WITH_DES40_CBC_SHA,TLS_RSA_WITH_AES_128_CBC_SHA)");
//    dataSource.setDataSourceProperties(prop);
//    Security.addProvider(new oracle.security.pki.OraclePKIProvider());

    return dataSource;
  }
}
