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
import java.util.stream.Collectors;


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

  private List<LocalDate> getRecentSevenDates() {
    LocalDate now = LocalDate.now(ZoneId.of("America/Chicago"));
    LocalDate sevenDaysBeforeNow = now.minusDays(6);
    LocalDate sevenDaysFromNow = sevenDaysBeforeNow.plusDays(7);

    List<LocalDate> localDateList = sevenDaysBeforeNow.datesUntil(sevenDaysFromNow).collect(Collectors.toList());

    Collections.sort(localDateList);
    return localDateList;
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

    // We get the current dates available from the list of matches
    List<LocalDate> datesList = new ArrayList<>();
    for (MiniMatch miniMatch : miniMatchList) {
      if (!datesList.contains(miniMatch.getTimestamp())) {
        datesList.add(miniMatch.getTimestamp());
      }
    }

    /*
        1) we get a list of
        2021-01-22
        2021-01-23
        2021-01-24
        2021-01-25
        2021-01-26
        2021-01-27
        2021-01-28
     */
    LocalDate now = LocalDate.now(ZoneId.of("America/Chicago"));
    LocalDate sevenDaysBeforeNow = now.minusDays(6);
    LocalDate sevenDaysFromNow = sevenDaysBeforeNow.plusDays(7);

    List<LocalDate> localDateList = sevenDaysBeforeNow.datesUntil(sevenDaysFromNow).collect(Collectors.toList());
    for (LocalDate date : localDateList) {
      if (!datesList.contains(date)) {
        datesList.add(date);
      }
    }
    // At this point the list has LocalDates but they aren't sorted
    Collections.sort(datesList);

    String[] dates = new String[datesList.size()];
    for (int i = 0; i < dates.length; i++) {
      dates[i] = datesList.get(i).toString();
    }

    return dates;
  }

  /*
      - An array of strings representing the dates (7 dates).
      - Array storing in each slot the number of times champion played per day.

      Map <String, Long[]>
          -----> { "champId", [12, 3, 4, 5] }



          1) set of championsId
          2) check with set while for-looping

  */
  @RequestMapping("/summonerLeagueWebV2/{userName}")
  //  Map<String, List<Integer>>
  //TreeMap<LocalDate, Map<Long, List<MiniMatch>>>
  private MatchUIResponse getSummonerDataV2(@PathVariable String userName) {
    RiotResponse riotResponse = getMatches(userName);
    MiniRiotResponse miniRiotResponse = new MiniRiotResponse(riotResponse);

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = getChampionMatchesByDatePlayed(miniRiotResponse);

    Set<Long> allUniqueChampionsIDs = getAllUniqueChampionsIDs(miniRiotResponse);

    Map<String, List<Integer>> championData = new LinkedHashMap<>();


    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayedEntry : championMatchesByDatePlayed.entrySet()) {

      Map<Long, List<MiniMatch>> matchesPlayedByChampionId = championMatchesByDatePlayedEntry.getValue();
      Set<Long> championsPlayedOnDate = matchesPlayedByChampionId.keySet();


      // If the keys aren't in this map, then the champion hasn't been played this day
      // and championData needs to be updated to have 0 for that championId

      // Checking allChampions with the championList on currentDate
      // if there is a champion not in championList on currentDate add to list
      List<Long> championsNotPlayedOnDate = new ArrayList<>();
      for (Long uniqueChampionsID : allUniqueChampionsIDs) {
        if (!championsPlayedOnDate.contains(uniqueChampionsID)) {
          championsNotPlayedOnDate.add(uniqueChampionsID);
        }
      }


      // this just adds zero
      int timesChampionHasBeenSeen = 0;
      for (Long championID : championsNotPlayedOnDate) {
        // gonna add zero for the first case which is the issue
        // then the following cases
        String championIdString = String.valueOf(championID);
        if (!championData.containsKey(championIdString)) {
          List<Integer> amountOfTimesPlayed2 = new ArrayList<>();
          amountOfTimesPlayed2.add(timesChampionHasBeenSeen);
          championData.put(championIdString, amountOfTimesPlayed2);
        }
        // this will still add a zero to the champion that has been seen but on a different date.
        // didnt play with it today, but maybe on a diff day I did
        else if (championData.containsKey(championIdString)) {
          List<Integer> currentList = championData.get(championIdString);
          currentList.add(0);
          championData.put(championIdString, currentList);
        }
      }


      for (Map.Entry<Long, List<MiniMatch>> matchesPlayedByChampionIdEntry : matchesPlayedByChampionId.entrySet()) {

        String currentChampionId = String.valueOf(matchesPlayedByChampionIdEntry.getKey());
        List<MiniMatch> matchList = matchesPlayedByChampionIdEntry.getValue();


        if (!championData.containsKey(currentChampionId)) {
          List<Integer> amountOfTimesPlayed = new ArrayList<>();
          amountOfTimesPlayed.add(matchList.size());
          championData.put(currentChampionId, amountOfTimesPlayed);
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

  private TreeMap<LocalDate, Map<Long, List<MiniMatch>>> getChampionMatchesByDatePlayed(MiniRiotResponse miniRiotResponse) {
    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = new TreeMap<>();

    List<MiniMatch> recentMatches = miniRiotResponse.getRecentMatches();

    for (MiniMatch currentMatch : recentMatches) {


      /*
        If map doesnt have entry Date
        Case 1: No currentDate, so no champId and no matches

         Jan 27 -> {
                          10: {Jan 27}

                    }

       */

      //Date does not exist, add date and champion match info
      if (!championMatchesByDatePlayed.containsKey(currentMatch.getTimestamp())) {
        Map<Long, List<MiniMatch>> matchesPlayedByChampionId = new LinkedHashMap<>();
        List<MiniMatch> valueList = new ArrayList<>();

        valueList.add(currentMatch);
        matchesPlayedByChampionId.put(currentMatch.getChampionId(), valueList);
        championMatchesByDatePlayed.put(currentMatch.getTimestamp(), matchesPlayedByChampionId);
      }
      //Else the date exists
            /*
        Case 2: Match with same date as outerMap but different champId

        Jan 27 -> {
                      10: { Jan 27 },
                      20: { Jan 27 }
                  }
       */
      else {
        Map<Long, List<MiniMatch>> matchesByChampionId = championMatchesByDatePlayed.get(currentMatch.getTimestamp());
        //And we want to see if the champion hasn't been played yet for this date and we want to create the map for the champion
        if (!matchesByChampionId.containsKey(currentMatch.getChampionId())) {


          List<MiniMatch> valueList = new ArrayList<>();
          valueList.add(currentMatch);
          matchesByChampionId.put(currentMatch.getChampionId(), valueList);


          // we get the map of currentDate to update it with the new map created
          championMatchesByDatePlayed.put(currentMatch.getTimestamp(), matchesByChampionId);
        }

        //Or else the champion has already been played for this date and we want to update the list
        else {

          List<MiniMatch> matchesPlayedByExistingChampionId = matchesByChampionId.get(currentMatch.getChampionId());
          matchesPlayedByExistingChampionId.add(currentMatch);

          // update outer map
          championMatchesByDatePlayed.put(currentMatch.getTimestamp(), matchesByChampionId);

        }
      }

    }

    /*
        If it doesnt have it, add Date and place an empty Map
     */
    List<LocalDate> allDates = this.getRecentSevenDates();
    for (LocalDate localDate : allDates) {
      if (!championMatchesByDatePlayed.containsKey(localDate)) {
        Map<Long, List<MiniMatch>> emptyData = new LinkedHashMap<>();
        emptyData.put(new Long(0), new ArrayList<>());
        championMatchesByDatePlayed.put(localDate, emptyData);
      }
    }

    return championMatchesByDatePlayed;
  }


  private Set<Long> getAllUniqueChampionsIDs(MiniRiotResponse riotResponse) {

    Set<Long> championsIDs = new HashSet<>();

    for (MiniMatch recentMatch : riotResponse.getRecentMatches()) {
      championsIDs.add(recentMatch.getChampionId());
    }

    return championsIDs;
  }


//  /*
//   * Checks if current timestamp is within range of seven days ago starting today.
//   */
//  private boolean isTimestampWithinRange(long epochSeconds) {
//    // current match from the loop from list of matches returned from riot
//    Instant instantOfCurrentMatch = Instant.ofEpochMilli(epochSeconds);
//    ZonedDateTime currentMatchDateTime = ZonedDateTime.ofInstant(instantOfCurrentMatch, ZoneOffset.UTC);
//
//    ZonedDateTime sevenDaysAgoDateTime = ZonedDateTime.now(ZoneOffset.UTC).minusDays(6)
//      .withHour(0)
//      .withMinute(0)
//      .withSecond(0)
//      .withNano(0);
//
//    return currentMatchDateTime.isAfter(sevenDaysAgoDateTime);
//  }
}
