package com.tusur.cargo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {

  private String host;
  private int port;
  private String protocol;
  private String user;
  private String password;
  private String from;
  private String auth;
  private String starttls;
  private String debug;
}
