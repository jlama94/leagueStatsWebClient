package com.league.web;

import com.league.web.configuration.YAMLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class LeagueWebClientApplication implements CommandLineRunner {

  // yaml object with configuration
  @Autowired
  private YAMLConfig yamlConfig;


  public static void main(String[] args) {
    SpringApplication.run(LeagueWebClientApplication.class, args);
  }

  /**
   * Callback used to run the bean.
   *
   * @param args incoming main method arguments
   * @throws Exception on error
   */
  @Override
  public void run(String... args) throws Exception {
    System.out.println("Profile: " + yamlConfig.getName());
    System.out.println("Environment:" + yamlConfig.getEnvironment());
    System.out.println("Target/server: " + yamlConfig.getLolServiceApplicationTarget());
  }
}
