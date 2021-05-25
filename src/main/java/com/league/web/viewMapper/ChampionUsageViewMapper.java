package com.league.web.viewMapper;

import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.ui.MatchUI;
import com.league.web.httpClient.ui.MatchUIResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ChampionUsageViewMapper {

  public ChampionUsageViewMapper() {}


  public MatchUIResponse buildMatchUIResponse(Map<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed) {


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
      List<Long> championsNotPlayedOnDate = getChampionIdsNotPlayedOnDate(allUniqueChampionsIDs, championsPlayedOnDate);

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

  private List<Long> getChampionIdsNotPlayedOnDate(Set<Long> allUniqueChampionsIDs, Set<Long> championsPlayedOnDate) {
    List<Long> championsNotPlayedOnDate = new ArrayList<>();
    for (Long uniqueChampionsID : allUniqueChampionsIDs) {
      if (!championsPlayedOnDate.contains(uniqueChampionsID)) {
        championsNotPlayedOnDate.add(uniqueChampionsID);
      }
    }
    return championsNotPlayedOnDate;
  }

  private void championNotSeenOnDate(Map<String, List<Integer>> championData, List<Long> championsNotPlayedOnDate) {
    int timesChampionHasBeenSeen = 0;
    for (Long championID : championsNotPlayedOnDate) {
      // gonna add zero for the first case which is the issue
      // then the following cases
      String championIdString = String.valueOf(championID);
      if (!championData.containsKey(championIdString)) {
        List<Integer> amountOfTimesPlayed = new ArrayList<>();
        amountOfTimesPlayed.add(timesChampionHasBeenSeen);
        championData.put(championIdString, amountOfTimesPlayed);
      }
      // this will still add a zero to the champion that has been seen but on a different date.
      // didnt play with it today, but maybe on a diff day I did
      else  {
        List<Integer> currentList = championData.get(championIdString);
        currentList.add(0);
        championData.put(championIdString, currentList);
      }
    }
  }

  private MatchUIResponse buildUIobject(Map<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed,
                                        Map<String, List<Integer>> championData) {
    MatchUIResponse matchUIResponse = new MatchUIResponse();

    //TODO Make this into a stream Julio
//    List<String> dateLabels = new ArrayList<>();
//    // set Data labels
//    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> localDateMapEntry : championMatchesByDatePlayed.entrySet()) {
//      dateLabels.add(localDateMapEntry.getKey().toString());
//    }

    List<String> dateLabels = championMatchesByDatePlayed.keySet().stream()
      .map(LocalDate::toString)
      .collect(Collectors.toList());

//    championMatchesByDatePlayed.forEach((key, value) -> dateLabels.add(key.toString()));




    String[] labelsArray = new String[dateLabels.size()];
    dateLabels.toArray(labelsArray);

    matchUIResponse.setDateLabels(labelsArray);

    List<MatchUI> temp_match_UI_list = new ArrayList<>();

    for (Map.Entry<String, List<Integer>> entry : championData.entrySet()) {
      Integer[] data = new Integer[entry.getValue().size()];

      MatchUI matchUI = MatchUI.builder().label(entry.getKey()).data(entry.getValue().toArray(data)).build();


      if (!temp_match_UI_list.contains(matchUI)) {
        temp_match_UI_list.add(matchUI);
      }
    }

    MatchUI[] array_of_matchesUI = new MatchUI[temp_match_UI_list.size()];

    array_of_matchesUI = temp_match_UI_list.toArray(array_of_matchesUI);

    matchUIResponse.setResponse(array_of_matchesUI);

    return matchUIResponse;
  }

  private Set<Long> getUniqueChampionIds(Map<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed) {
    // recent matches from championMatchesByDatePlayed:

    //TODO Use a Stream to make this map
    List<MiniMatch> recentMatches = new ArrayList<>();
    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> localDateMapEntry : championMatchesByDatePlayed.entrySet()) {

      Map<Long, List<MiniMatch>> entryValue = localDateMapEntry.getValue();

      for (Map.Entry<Long, List<MiniMatch>> longListMapEntry : entryValue.entrySet()) {
        recentMatches.addAll(longListMapEntry.getValue());
      }
    }

    //Unique champion ids
    Set<Long> allUniqueChampionsIDs = recentMatches.stream()
      .map(MiniMatch::getChampionId)
      .collect(Collectors.toSet());

    return allUniqueChampionsIDs;
  }
}
