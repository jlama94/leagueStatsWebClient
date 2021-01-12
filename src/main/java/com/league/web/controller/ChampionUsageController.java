package com.league.web.controller;

import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.model.MiniRiotResponse;
import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import com.league.web.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


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


  @RequestMapping("/matchesLeagueWeb/{userName}")
  private RiotResponse getMatches(@PathVariable String userName) {
    return matchService.getMatches(userName);
  }

  /**
   * 7 most recent matches
   */
  private List<RiotMatch> getSevenMatches(RiotResponse riotResponse) {
    List<RiotMatch> riotMatchList = new ArrayList<>();
    for (int i = 0; i < riotResponse.getMatches().size(); i++) {
      RiotMatch currentMatch = riotResponse.getMatches().get(i);
      // unique matches
      if (!riotMatchList.contains(currentMatch)) {
        riotMatchList.add(currentMatch);
      }
    }
    // leave 7
    for (int i = riotMatchList.size() - 1; i >= 7; i--) {
      riotMatchList.remove(riotMatchList.get(i));
    }
    return riotMatchList;
  }

  /**
   * return a list of strings/dates 7
   */
  private List<String> getSevenDates(RiotResponse riotResponse) {
        /*
        1) Get a list of dates (7 dates) for the graph. For the "ChartLabels"
     */
    List<String> datesList = new ArrayList<>();
    String dateFormatStr;

    for (RiotMatch riotMatch : riotResponse.getMatches()) {
      dateFormatStr = ZonedDateTime.ofInstant(Instant.ofEpochMilli(riotMatch.getTimestamp()),
        ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyy-MM-dd"));

      if (!datesList.contains(dateFormatStr)) {
        datesList.add(dateFormatStr);
      }
    }

    //  7 most recent dates
    for (int i = datesList.size() - 1; i >= 7; i--) {
      datesList.remove(datesList.get(i));
    }

    return datesList;
  }




/*
        - An array of strings representing the dates (7 dates).
        - Array storing in each slot the number of times champion played per day.

        Map <String, String[]>
            -----> { "champId", [12, 3, 4, 5] }
 */

  @RequestMapping("/summonerLeagueWeb/{userName}")
  public Map<LocalDate, Map<Long, List<MiniMatch>>> getSummonerData(@PathVariable String userName) {

    RiotResponse riotResponse = getMatches(userName);
    MiniRiotResponse miniRiotResponse = new MiniRiotResponse(riotResponse);

    Map<LocalDate, Map<Long, List<MiniMatch>>> outerMap = new LinkedHashMap<>();

    for (int i = 0; i < miniRiotResponse.getAllMatches().size(); i++) {

      Map<Long, List<MiniMatch>> innerMap = new LinkedHashMap<>();
      List<MiniMatch> valueList = new ArrayList<>();

      MiniMatch miniMatch = miniRiotResponse.getAllMatches().get(i);

      /*
          If map doesnt have an entry
       */
      if (!outerMap.containsKey(miniMatch.getTimestamp())) {
        valueList.add(miniMatch); // add the current match to the List value
        innerMap.put(miniMatch.getChampionId(), valueList); // place the key and value to the inner map
        outerMap.put(miniMatch.getTimestamp(), innerMap); // put the current timestamp as the outer key and the inner map as val
      }
      /*
          Match with same date as OuterMap but different champID.
       */
      if (outerMap.containsKey(miniMatch.getTimestamp()) && !innerMap.containsKey(miniMatch.getChampionId())) {
        // quiero crear otra entry en el innerMap
        valueList.add(miniMatch);
        innerMap.put(miniMatch.getChampionId(), valueList);

        outerMap.get(miniMatch.getTimestamp()).put(miniMatch.getChampionId(), valueList);
      }
      /*
          Same champID that has already been added to the innerMap entry
       */
      else {
        innerMap.get(miniMatch.getChampionId()).add(miniMatch);
      }

    }
    return outerMap;

  }


  /*
   * Checks if current timestamp is within range of seven days ago starting today.
   */
  private boolean isTimestampWithinRange(long epochSeconds) {
    // current match from the loop from list of matches returned from riot
    Instant instantOfCurrentMatch = Instant.ofEpochMilli(epochSeconds);
    ZonedDateTime currentMatchDateTime = ZonedDateTime.ofInstant(instantOfCurrentMatch, ZoneOffset.UTC);

    ZonedDateTime sevenDaysAgoDateTime = ZonedDateTime.now(ZoneOffset.UTC).minusDays(6)
      .withHour(0)
      .withMinute(0)
      .withSecond(0)
      .withNano(0);

    return currentMatchDateTime.isAfter(sevenDaysAgoDateTime);
  }
}
