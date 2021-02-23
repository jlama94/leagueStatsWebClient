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


  // this is not being used anywhere atm
//  private List<LocalDate> getRecentSevenDates() {
//    LocalDate now = LocalDate.now(ZoneId.of("America/Chicago"));
//    LocalDate sevenDaysBeforeNow = now.minusDays(6);
//    LocalDate sevenDaysFromNow = sevenDaysBeforeNow.plusDays(7);
//
//    List<LocalDate> localDateList = sevenDaysBeforeNow.datesUntil(sevenDaysFromNow).collect(Collectors.toList());
//
//    Collections.sort(localDateList);
//    return localDateList;
//  }

  /*
      - An array of strings representing the dates (7 dates).
      - Array storing in each slot the number of times champion played per day.

      Map <String, Long[]>
          -----> { "champId", [12, 3, 4, 5] }
  */
  @RequestMapping("/summonerLeagueWebV2/{userName}")
  private MatchUIResponse getSummonerDataV2(@PathVariable String userName) {


    LocalDate today = LocalDate.now().minusDays(1);
    LocalDate sevenDaysAgoFromToday = today.minusDays(6);

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = matchService.getMatchesForGraph(userName,
      sevenDaysAgoFromToday, today);

    return championUsageViewMapper.buildMatchUIResponse(championMatchesByDatePlayed, userName, sevenDaysAgoFromToday, today);
  }
}
