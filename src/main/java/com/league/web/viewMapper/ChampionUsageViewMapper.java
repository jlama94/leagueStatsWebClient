package com.league.web.viewMapper;

import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.ui.MatchUI;
import com.league.web.httpClient.ui.MatchUIResponse;
import com.league.web.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class ChampionUsageViewMapper {

  private MatchService matchService;

  @Autowired
  public ChampionUsageViewMapper(MatchService matchService) {
    this.matchService = matchService;
  }


  public MatchUIResponse buildMatchUIResponse(TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed) {


    Set<Long> allUniqueChampionsIDs = getUniqueChampionIds(championMatchesByDatePlayed);


    Map<String, List<Integer>> championData = new LinkedHashMap<>();


    //building map of games played by champion for the seven days including 0s
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


      // this just adds zero for cases when champion has not been seen on a date.
      championNotSeenOnDate(championData, championsNotPlayedOnDate);


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
    //building our UI object
    return buildUIobject(championMatchesByDatePlayed, championData);
  }

  private void championNotSeenOnDate(Map<String, List<Integer>> championData, List<Long> championsNotPlayedOnDate) {
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
  }

  private MatchUIResponse buildUIobject(TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed, Map<String, List<Integer>> championData) {
    MatchUIResponse matchUIResponse = new MatchUIResponse();

    List<MatchUI> temp_match_UI_list = new ArrayList<>();
    MatchUI matchUI;
    Integer[] data;

    List<String> dateLabels = new ArrayList<>();
    // set Data labels
    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> localDateMapEntry : championMatchesByDatePlayed.entrySet()) {
      dateLabels.add(localDateMapEntry.getKey().toString());
    }


    String[] labelsArray = new String[dateLabels.size()];
    dateLabels.toArray(labelsArray);

    matchUIResponse.setDateLabels(labelsArray);


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

  private Set<Long> getUniqueChampionIds(TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed) {
    // recent matches from championMatchesByDatePlayed:
    List<MiniMatch> recentMatches = new ArrayList<>();
    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> localDateMapEntry : championMatchesByDatePlayed.entrySet()) {

      Map<Long, List<MiniMatch>> entryValue = localDateMapEntry.getValue();

      for (Map.Entry<Long, List<MiniMatch>> longListMapEntry : entryValue.entrySet()) {
        recentMatches.addAll(longListMapEntry.getValue());
      }
    }

    //Unique champion ids
    Set<Long> allUniqueChampionsIDs = new HashSet<>();
    for (MiniMatch recentMatch : recentMatches) {
      allUniqueChampionsIDs.add(recentMatch.getChampionId());
    }
    return allUniqueChampionsIDs;
  }
}
