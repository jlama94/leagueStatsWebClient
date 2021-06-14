package com.league.web.controller;

import com.league.web.httpClient.detailedResponse.DetailedMatchResponse;
import com.league.web.httpClient.detailedResponse.Match;
import com.league.web.httpClient.model.*;
import com.league.web.httpClient.testingObjectModel.ChampionsInformationResponse;
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

    ResponseObject: {

          Long[] championIds = [11, 21, 31, 41]
          Map<String, List<Int> > wins;
                      - List<Int> = [7, 8, 9, 1, 0]  <- where each number represents the total number of wins for
                                                        different champions

          Map<String, List<Int>> losses;
      }
   */
  @RequestMapping("/matches/v2/{userName}")
  public ChampionsInformationResponse getDetailedMatchResponse(@PathVariable String userName) {

    LocalDate today = LocalDate.now().minusDays(1);
    LocalDate sevenDaysAgoFromToday = today.minusDays(6);


    DetailedMatchResponse response = matchService
      .getDetailedMatchResponse(userName, sevenDaysAgoFromToday, today);


    ChampionsInformationResponse result = new ChampionsInformationResponse();


    Map<Long, Map<String, Integer>> championInformation = new LinkedHashMap<>();
    for (Match match : response.getMatches()) {

      // 1st case: first entry is a win
      boolean isWin = match.isWin();

      if (isWin) {
        // no champs yet
        if (!championInformation.containsKey(match.getChampion())) {

          Map<String, Integer> winsAndLosses = new LinkedHashMap<>();

          winsAndLosses.put("wins", 1);

          // update the loss record to 0
          winsAndLosses.put("losses", 0);

          // put first entry on outer map
          championInformation.put(match.getChampion(), winsAndLosses);

        }

        // 2 case: it is a win and we have seen this champion already
        else {
          Map<String, Integer> matchHistory = championInformation.get(match.getChampion());
          // update the counter of that match history
          Integer winsSoFar = matchHistory.get("wins");
          int updatedWins = winsSoFar + 1;
          matchHistory.put("wins", updatedWins);

          championInformation.put(match.getChampion(), matchHistory);

        }
      }
//       else 2nd case: it is a loss
      else {
        // it's a loss and we haven't seen the champion yet
        if (!championInformation.containsKey(match.getChampion())) {
          Map<String, Integer> winsAndLosses = new LinkedHashMap<>();
          winsAndLosses.put("losses", 1);
          winsAndLosses.put("wins", 0);
          championInformation.put(match.getChampion(), winsAndLosses);
        } else {
          Map<String, Integer> matchHistory = championInformation.get(match.getChampion());
          Integer lossesCounter = matchHistory.get("losses");
          int updatedLosses = lossesCounter + 1;

          matchHistory.put("losses", updatedLosses);

          championInformation.put(match.getChampion(), matchHistory);

        }
      }

      /*
          Pass current information from DetailedInformation to SuperDetailedInfo
      "267": {
        "wins": 4,
        "losses": 2
    },
    "350": {
        "losses": 4,
        "wins": 3
    }
   */


      /*
          1) an Array of of champions Ids
       */


      List<String> championIds = new ArrayList<>();

      Map<String, List<Integer>> wins = new LinkedHashMap<>();
      Map<String, List<Integer>> losses = new LinkedHashMap<>();


      List<Integer> winsList = new ArrayList<>();
      List<Integer> lossList = new ArrayList<>();

      for (Map.Entry<Long, Map<String, Integer>> entry : championInformation.entrySet()) {
        long champion = entry.getKey();
        // adding all champions to champions list
        if (!championIds.contains(String.valueOf(champion))) {
          championIds.add(String.valueOf(champion));
        }

        // wins or loss entries
        Map<String, Integer> winsOrLossesEntry = entry.getValue();
        if (winsOrLossesEntry.containsKey("wins")) {
          winsList.add(winsOrLossesEntry.get("wins")); // add the values for wins
        }
        if (winsOrLossesEntry.containsKey("losses")) {
          lossList.add(winsOrLossesEntry.get("losses")); // add the values for losses
        }
      }

      // response
      wins.put("wins", winsList);
      result.setWins(wins);

      losses.put("losses", lossList);
      result.setLosses(losses);

      result.setChampionIds(championIds);
    }

    return result;
  }
}
