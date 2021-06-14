package com.league.web.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*
https://stackoverflow.com/questions/50345888/prefix-for-nested-configuration-properties-in-spring
 */

@Configuration
@ConfigurationProperties(prefix = "environment") // access these variables on the YAML file  using "environment.name, environment.enabled"
public class YAMLConfig {
  private String name;
  private String environment;
  private boolean enabled;
  private String lolServiceApplicationTarget;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getLolServiceApplicationTarget() {
    return lolServiceApplicationTarget;
  }

  public void setLolServiceApplicationTarget(String lolServiceApplicationTarget) {
    this.lolServiceApplicationTarget = lolServiceApplicationTarget;
  }
}
