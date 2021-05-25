package com.league.web.service;

import com.league.web.client.MatchClient;
import com.league.web.httpClient.detailedResponse.DetailedMatchResponse;
import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MatchService {

  private MatchClient matchClient;

  @Autowired
  public MatchService(MatchClient matchClient) {
    this.matchClient = matchClient;
  }



  public DetailedMatchResponse getDetailedMatchResponse(String userName, LocalDate startDate, LocalDate endDate) {
    return matchClient.getDetailedMatchResponse(userName, startDate, endDate);
  }





  /*
        Hardcoded dates from: "ChampionUsageController"
        endDate -> LocalDate today = LocalDate.now();
        startDate -> LocalDate sevenDaysAgoFromToday = today.minusDays(7);
   */
  public TreeMap<LocalDate, Map<Long, List<MiniMatch>>> getMatchesForGraph(String userName,
                                                                           LocalDate startDate,
                                                                           LocalDate endDate) {

    RiotResponse riotResponse = matchClient.getMatchesByUserName(userName, startDate, endDate);

    List<MiniMatch> miniMatches = buildMiniMatches(riotResponse.getMatches());

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = new TreeMap<>();

    for (MiniMatch currentMatch : miniMatches) {

      //Date does not exist, add date and champion match info
      if (!championMatchesByDatePlayed.containsKey(currentMatch.getMatchDate())) {
        Map<Long, List<MiniMatch>> matchesPlayedByChampionId = new LinkedHashMap<>();

        List<MiniMatch> valueList = new ArrayList<>();
        valueList.add(currentMatch);

        matchesPlayedByChampionId.put(currentMatch.getChampionId(), valueList);
        championMatchesByDatePlayed.put(currentMatch.getMatchDate(), matchesPlayedByChampionId);
      }
      //Else the date exists
      else {
        Map<Long, List<MiniMatch>> matchesPlayedByChampionId = championMatchesByDatePlayed.get(currentMatch.getMatchDate());
        //And we want to see if the champion hasn't been played yet for this date and we want to create the map for the champion
        if (!matchesPlayedByChampionId.containsKey(currentMatch.getChampionId())) {

          List<MiniMatch> valueList = new ArrayList<>();
          valueList.add(currentMatch);

          matchesPlayedByChampionId.put(currentMatch.getChampionId(), valueList);

          // we get the map of currentDate to update it with the new map created
          championMatchesByDatePlayed.put(currentMatch.getMatchDate(), matchesPlayedByChampionId);
        }

        //Or else the champion has already been played for this date and we want to update the list
        else {

          List<MiniMatch> matchesPlayedByExistingChampionId = matchesPlayedByChampionId.get(currentMatch.getChampionId());
          matchesPlayedByExistingChampionId.add(currentMatch);

          // update outer map
          championMatchesByDatePlayed.put(currentMatch.getMatchDate(), matchesPlayedByChampionId);

        }
      }
    }

    LocalDate endThreshold = endDate.plusDays(1);
    List<LocalDate> localDateList = startDate.datesUntil(endThreshold).sorted().collect(Collectors.toList());

      /*
        If it doesnt have it, add Date and place an empty Map
     */
    populateDatesWithNoGamesPlayed(championMatchesByDatePlayed, localDateList);

    return championMatchesByDatePlayed;

  }

  private void populateDatesWithNoGamesPlayed(TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed,
                                              List<LocalDate> localDateList) {
    for (LocalDate localDate : localDateList) {
      if (!championMatchesByDatePlayed.containsKey(localDate)) {
        Map<Long, List<MiniMatch>> emptyData = new LinkedHashMap<>();
        championMatchesByDatePlayed.put(localDate, emptyData);
      }
    }
  }

  private List<MiniMatch> buildMiniMatches(List<RiotMatch> riotMatches) {

    return riotMatches.stream().map(riotMatch -> {

      return buildMiniMatch(riotMatch);
    }).collect(Collectors.toList());

  }

  private MiniMatch buildMiniMatch(RiotMatch riotMatch) {

    LocalDateTime matchDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(riotMatch.getTimestamp()), ZoneId.of("UTC"));
    LocalDate date = matchDate.toLocalDate();

    return MiniMatch.builder()
      .championId(riotMatch.getChampion())
      .matchDate(date)
      .build();
  }

}
