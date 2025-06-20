package com.example.backend;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.backend.model")
@EnableJpaRepositories("com.example.backend.repository")
public class BackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

  @Bean
  CommandLineRunner testDatabase(DataSource dataSource) {
    return args -> {
      try (Connection conn = dataSource.getConnection()) {
        System.out.println("数据库连接成功!");
        System.out.println("URL: " + conn.getMetaData().getURL());
        System.out.println("用户名: " + conn.getMetaData().getUserName());
      } catch (SQLException e) {
        System.err.println("数据库连接失败:");
        e.printStackTrace();
        System.exit(1);
      }
    };
  }

}
