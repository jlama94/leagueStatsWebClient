package com.league.web.controller;

import com.league.web.httpClient.model.*;
import com.league.web.httpClient.ui.MatchUIResponse;
import com.league.web.service.MatchService;
import com.league.web.viewMapper.ChampionUsageViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.util.*;


// https://howtodoinjava.com/spring5/webmvc/spring-mvc-cors-configuration/
// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/CrossOrigin.html#allowedHeaders--
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
public class ChampionUsageController {

  private MatchService matchService;
  private ChampionUsageViewMapper championUsageViewMapper;


  @Autowired
  public ChampionUsageController(MatchService matchService, ChampionUsageViewMapper championViewMapper) {
    this.matchService = matchService;
    championUsageViewMapper = championViewMapper;
  }

  
  @RequestMapping("/summonerLeagueWebV2/{userName}")
  public MatchUIResponse getSummonerDataV2(@PathVariable String userName) {


    LocalDate today = LocalDate.now().minusDays(1);
    LocalDate sevenDaysAgoFromToday = today.minusDays(6);

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = matchService.getMatchesForGraph(userName,
      sevenDaysAgoFromToday, today);

    return championUsageViewMapper.buildMatchUIResponse(championMatchesByDatePlayed);
  }
}
