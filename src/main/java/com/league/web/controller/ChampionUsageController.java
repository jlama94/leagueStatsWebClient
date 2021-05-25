package com.league.web.controller;

import com.league.web.httpClient.detailedResponse.DetailedMatchResponse;
import com.league.web.httpClient.detailedResponse.Match;
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
    this.championUsageViewMapper = championViewMapper;
  }


  @RequestMapping("/summonerLeagueWebV2/{userName}")
  public MatchUIResponse getSummonerDataV2(@PathVariable String userName) {


    LocalDate today = LocalDate.now().minusDays(1);
    LocalDate sevenDaysAgoFromToday = today.minusDays(6);

    Map<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = matchService.getMatchesForGraph(userName,
      sevenDaysAgoFromToday, today);

    return championUsageViewMapper.buildMatchUIResponse(championMatchesByDatePlayed);
  }

  /*
      response: {
          champions: ['A', 'B', 'C'],

          WinsObject: {
            name: 'Wins',
            winsCountPerChampion: [3,  1, 7]
          },

          LossObject: {
            name: 'Losses',
            lossCountPerChampion: [1, 4, 2]
          }
       }
   */

  @RequestMapping("/fakeData")
  public MatchHistoryTracker getStackedBarChartData() {
    // building losses
    List<Integer> lossesCounter = new ArrayList<>();
    lossesCounter.add(1);
    lossesCounter.add(4);
    lossesCounter.add(2);

    Loss losses = Loss.builder()
      .losses(lossesCounter)
      .build();


    // building wins
    List<Integer> winsCounter = new ArrayList<>();
    winsCounter.add(3);
    winsCounter.add(1);
    winsCounter.add(7);

    Win wins = Win.builder()
      .wins(winsCounter)
      .build();


    MatchHistoryTracker matchHistoryTracker = MatchHistoryTracker
      .builder()
      .loss(losses)
      .win(wins)
      .build();

    return matchHistoryTracker;
  }


  @RequestMapping("/matches/v2/{userName}")
  public DetailedMatchResponse getDetailedMatchResponse(@PathVariable String userName) {

    LocalDate today = LocalDate.now().minusDays(1);
    LocalDate sevenDaysAgoFromToday = today.minusDays(6);


    DetailedMatchResponse response = matchService
      .getDetailedMatchResponse(userName, sevenDaysAgoFromToday, today);


    /*
      How do i count the number of wins and losses?
      - Make two lists, where each list is of wins and losses?
      - Then I just count the number of matches within each list of wins and losses?



        	Map<Long, Map<String, Long>>


              Champion Id - 1
                Wins
                   2
                Loss
                   8



     */

    Map<Long, Map<String, Integer>> detailedInformation = new LinkedHashMap<>();

    /*
      Start again
      Case when first entry is a win, update the loss counter
      Case when first entry is a loss, update the win counter
     */
    for (Match match : response.getMatches()) {
      // 1st case: first entry is a win
      boolean isWin = match.isWin();

      if (isWin) {
        // no champs yet
        if (!detailedInformation.containsKey(match.getChampion())) {

          Map<String, Integer> winsAndLosses = new LinkedHashMap<>();

          winsAndLosses.put("wins", 1);

          // update the loss record to 0
          winsAndLosses.put("losses", 0);

          // put first entry on outer map
          detailedInformation.put(match.getChampion(), winsAndLosses);
        }

        // 2 case: it is a win and we have seen this champion already
        else {
          Map<String, Integer> matchHistory = detailedInformation.get(match.getChampion());
          // update the counter of that match history
          Integer winsSoFar = matchHistory.get("wins");
          int updatedWins = winsSoFar + 1;
          matchHistory.put("wins", updatedWins);

          detailedInformation.put(match.getChampion(), matchHistory);
        }
      }
//       else 2nd case: it is a loss
      else {
        // it's a loss and we havent seen the champion yet?
        if (!detailedInformation.containsKey(match.getChampion())) {
          Map<String, Integer> winsAndLosses = new LinkedHashMap<>();
          winsAndLosses.put("losses", 1);
          winsAndLosses.put("wins", 0);
          detailedInformation.put(match.getChampion(), winsAndLosses);
        } else {
          Map<String, Integer> matchHistory = detailedInformation.get(match.getChampion());
          Integer lossesCounter = matchHistory.get("losses");
          int updatedLosses = lossesCounter + 1;

          matchHistory.put("losses", updatedLosses);

          detailedInformation.put(match.getChampion(), matchHistory);

        }
      }
    }

    return matchService.getDetailedMatchResponse(userName, sevenDaysAgoFromToday, today);
  }
}
