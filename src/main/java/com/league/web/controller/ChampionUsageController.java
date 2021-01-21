package com.league.web.controller;

import com.league.web.httpClient.model.*;
import com.league.web.httpClient.riotResponse.RiotResponse;
import com.league.web.httpClient.ui.MatchUI;
import com.league.web.httpClient.ui.MatchUIResponse;
import com.league.web.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.util.*;

// todo: Pass the data to the frontend


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


  /*
      Returns the 7 recent dates of that user
   */
  @RequestMapping("/recentDates/{userName}")
  public String[] getSevenDates(@PathVariable String userName) {
        /*
        1) Get a list of dates (7 dates) for the graph. For the "ChartLabels"
     */
    RiotResponse riotResponse = getMatches(userName);
    MiniRiotResponse miniRiotResponse = new MiniRiotResponse(riotResponse);

    List<MiniMatch> miniMatchList = miniRiotResponse.getRecentMatches();


    List<String> datesList = new ArrayList<>();
    for (MiniMatch miniMatch : miniMatchList) {
      if (!datesList.contains(miniMatch.getTimestamp().toString())) {
        datesList.add(miniMatch.getTimestamp().toString());
      }
    }

    String[] result = new String[datesList.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = datesList.get(i);
    }

    return result;
  }


  /*
  Map the data map so that can match what the front end wants.

      - An array of strings representing the dates (7 dates).
      - Array storing in each slot the number of times champion played per day.

      Map <String, Long[]>
          -----> { "champId", [12, 3, 4, 5] }
 */
  @RequestMapping("/summonerLeagueWebV2/{userName}")
  //  Map<String, List<Integer>>
  //TreeMap<LocalDate, Map<Long, List<MiniMatch>>>
  private MatchUIResponse getSummonerDataV2(@PathVariable String userName) {
    RiotResponse riotResponse = getMatches(userName);
    MiniRiotResponse miniRiotResponse = new MiniRiotResponse(riotResponse);

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> outerMap = new TreeMap<>();


    for (MiniMatch currentMatch : miniRiotResponse.getRecentMatches()) {
      Map<Long, List<MiniMatch>> innerMap = new LinkedHashMap<>();
      List<MiniMatch> valueList = new ArrayList<>();

      /*
        If map doesnt have entry
       */
      if (!outerMap.containsKey(currentMatch.getTimestamp())) {
        valueList.add(currentMatch);
        innerMap.put(currentMatch.getChampionId(), valueList);
        outerMap.put(currentMatch.getTimestamp(), innerMap);
      }

      /*
        Match with same date as outerMap but different champId
       */

      if (outerMap.containsKey(currentMatch.getTimestamp()) && !innerMap.containsKey(currentMatch.getChampionId())) {
        valueList.add(currentMatch);
        innerMap.put(currentMatch.getChampionId(), valueList);
        outerMap.get(currentMatch.getTimestamp()).put(currentMatch.getChampionId(), valueList);
      } else {
        innerMap.get(currentMatch.getChampionId()).add(currentMatch);
      }
    }

    Map<String, List<Integer>> championData = new LinkedHashMap<>();
    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> outerMapEntry : outerMap.entrySet()) {
      for (Map.Entry<Long, List<MiniMatch>> innerMapEntry : outerMapEntry.getValue().entrySet()) {

        String currentChampionId = String.valueOf(innerMapEntry.getKey());
        List<MiniMatch> matchList = innerMapEntry.getValue();

        List<Integer> amount_of_times_played = new ArrayList<>();

        if (!championData.containsKey(currentChampionId)) {
          amount_of_times_played.add(matchList.size());
          championData.put(currentChampionId, amount_of_times_played);
        } else if (championData.containsKey(currentChampionId)) {
          List<Integer> currentList = championData.get(currentChampionId);
          currentList.add(matchList.size());
          championData.put(currentChampionId, currentList);
        }
      }
    }

    MatchUIResponse matchUIResponse = new MatchUIResponse();
    List<MatchUI> temp_match_UI_list = new ArrayList<>();
    MatchUI matchUI;
    Integer[] data;
    for (Map.Entry<String, List<Integer>> entry : championData.entrySet()) {
      matchUI = new MatchUI();
      matchUI.setLabel(entry.getKey());

      data = new Integer[entry.getValue().size()];

      matchUI.setData(entry.getValue().toArray(data));


      if (!temp_match_UI_list.contains(matchUI)) {
        temp_match_UI_list.add(matchUI);
      }
    }

    MatchUI[] array_of_matchesUI = new MatchUI[temp_match_UI_list.size()];

    array_of_matchesUI = temp_match_UI_list.toArray(array_of_matchesUI);

    matchUIResponse.setResponse(array_of_matchesUI);

    return matchUIResponse;
  }


////////////////////////// older version ////////////////////////// ////////////////////////// //////////////////////////
  /*
    Map the data map so that can match what the front end wants.

        - An array of strings representing the dates (7 dates).
        - Array storing in each slot the number of times champion played per day.

        Map <String, Long[]>
            -----> { "champId", [12, 3, 4, 5] }
   */
//  @RequestMapping("/summonerLeagueWeb/{userName}")
//  // Map<LocalDate, Map<Long, List<MiniMatch>>>
//  private Map<LocalDate,Map<Long, List<MiniMatch>>> getSummonerData(@PathVariable String userName) {
//
//    RiotResponse riotResponse = getMatches(userName);
//    MiniRiotResponse miniRiotResponse = new MiniRiotResponse(riotResponse);
//
//    Map<LocalDate, Map<Long, List<MiniMatch>>> outerMap = new LinkedHashMap<>();
//
//    for (int i = 0; i < miniRiotResponse.getAllMatches().size(); i++)
//    {
//
//      Map<Long, List<MiniMatch>> innerMap = new LinkedHashMap<>();
//      List<MiniMatch> valueList = new ArrayList<>();
//
//      MiniMatch miniMatch = miniRiotResponse.getAllMatches().get(i);
//
//      /*
//          If map doesnt have an entry
//       */
//      if (!outerMap.containsKey(miniMatch.getTimestamp())) {
//        valueList.add(miniMatch); // add the current match to the List value
//        innerMap.put(miniMatch.getChampionId(), valueList); // place the key and value to the inner map
//        outerMap.put(miniMatch.getTimestamp(), innerMap); // put the current timestamp as the outer key and the inner map as val
//      }
//
//      /*
//          Match with same date as OuterMap but different champID.
//       */
//      if (outerMap.containsKey(miniMatch.getTimestamp()) && !innerMap.containsKey(miniMatch.getChampionId())) {
//        // quiero crear otra entry en el innerMap
//        valueList.add(miniMatch);
//        innerMap.put(miniMatch.getChampionId(), valueList);
//
//        outerMap.get(miniMatch.getTimestamp()).put(miniMatch.getChampionId(), valueList);
//      }
//
//      /*
//          Same champID that has already been added to the innerMap entry
//       */
//      else {
//        innerMap.get(miniMatch.getChampionId()).add(miniMatch);
//      }
//    }
//    return outerMap;
//  }

////////////////////////// OLDER VERSION ////////////////////////// ////////////////////////// //////////////////////////


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
