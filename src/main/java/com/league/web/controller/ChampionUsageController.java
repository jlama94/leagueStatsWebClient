package com.league.web.controller;

import com.league.web.httpClient.model.Match;
import com.league.web.httpClient.model.MatchResponse;
import com.league.web.httpClientUsage.MatchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChampionUsageController {
  private MatchClient matchClient;

  @Autowired
  public ChampionUsageController(MatchClient matchClient) {
    this.matchClient = matchClient;
  }

  @RequestMapping("/matches/{userName}")
  public MatchResponse getMatches(@PathVariable String userName) {
    MatchResponse matches = matchClient.getMatchesByUserName(userName);
    return matches;
  }
}
