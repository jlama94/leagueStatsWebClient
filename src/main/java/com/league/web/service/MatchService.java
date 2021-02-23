package com.league.web.service;

import com.league.web.client.MatchClient;
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


  // this is not being used atm
//  public RiotResponse getMatches(String userName) {
//    return this.matchClient.getMatchesByUserName(userName);
//  }

  public List<MiniMatch> getRecentMatches(String userName, LocalDate startDate, LocalDate endDate) {
    RiotResponse riotResponse = this.matchClient.getMatchesByUserName(userName,
      startDate, endDate);

    List<MiniMatch> miniMatchList = new ArrayList<>();
    MiniMatch miniMatch;
    List<RiotMatch> riotResponseMatches = riotResponse.getMatches();

    //Convert timestamps to dates
    for (RiotMatch match : riotResponseMatches) {
      miniMatch = new MiniMatch();
      miniMatch.setChampionId(match.getChampion());

      LocalDateTime tmpDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(match.getTimestamp()), ZoneId.of("UTC"));

      LocalDate date = tmpDate.toLocalDate();

      miniMatch.setTimestamp(date);

      miniMatchList.add(miniMatch);
    }


//    Filtering Down to 7 Days, but the service client is already doing it? so no filtering?
    List<MiniMatch> recentMatches = new ArrayList<>();

//    LocalDate now = LocalDate.now(ZoneId.of("America/Chicago"));
//    LocalDate sevenDaysBeforeNow = now.minusDays(7);
//
//    for (MiniMatch match : miniMatchList) {
//      if (match.getTimestamp().isAfter(sevenDaysBeforeNow)) {
//        recentMatches.add(match);
//      }
//    }

    recentMatches.addAll(miniMatchList);

    // 77 matches for a week -> 7 days?
    return recentMatches;
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


    List<MiniMatch> miniMatchList = new ArrayList<>();
    MiniMatch miniMatch;


    //Convert timestamps to dates
    List<RiotMatch> riotResponseMatches = riotResponse.getMatches();
    for (RiotMatch match : riotResponseMatches) {
      miniMatch = new MiniMatch();
      miniMatch.setChampionId(match.getChampion());

      LocalDateTime tmpDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(match.getTimestamp()), ZoneId.of("UTC"));

      LocalDate date = tmpDate.toLocalDate();

      miniMatch.setTimestamp(date);

      miniMatchList.add(miniMatch);
    }



    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = new TreeMap<>();

    for (MiniMatch currentMatch : miniMatchList) {


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

    LocalDate endThreshold = endDate.plusDays(1);
    List<LocalDate> localDateList = startDate.datesUntil(endThreshold).sorted().collect(Collectors.toList());



      /*
        If it doesnt have it, add Date and place an empty Map
     */


    for (LocalDate localDate : localDateList) {
      if (!championMatchesByDatePlayed.containsKey(localDate)) {
        Map<Long, List<MiniMatch>> emptyData = new LinkedHashMap<>();
        emptyData.put(new Long(0), new ArrayList<>());
        championMatchesByDatePlayed.put(localDate, emptyData);
      }
    }

    return championMatchesByDatePlayed;

  }
}
