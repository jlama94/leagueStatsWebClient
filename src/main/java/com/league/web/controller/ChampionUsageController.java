package com.league.web.controller;

import com.league.web.httpClient.model.MatchResponse;
import com.league.web.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// https://howtodoinjava.com/spring5/webmvc/spring-mvc-cors-configuration/
// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/CrossOrigin.html#allowedHeaders--
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
public class ChampionUsageController {

  private MatchService matchService;

  @Autowired
  public ChampionUsageController(MatchService matchService) {
    this.matchService = matchService;
  }

  @RequestMapping("/matches/{userName}")
  public MatchResponse getMatches(@PathVariable String userName) {
    return matchService.getMatches(userName);
  }
}
