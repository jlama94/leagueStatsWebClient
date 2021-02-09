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


  public RiotResponse getMatches(String userName) {
    return this.matchClient.getMatchesByUserName(userName);
  }

  public List<MiniMatch> getRecentMatches(String userName) {
    RiotResponse riotResponse = this.matchClient.getMatchesByUserName(userName);

    List<MiniMatch> miniMatchList = new ArrayList<>();
    MiniMatch miniMatch;
    List<RiotMatch> riotResponseMatches = riotResponse.getMatches();

    //Convert timestamps to dates
    for (RiotMatch match : riotResponseMatches) {
      miniMatch = new MiniMatch();
      miniMatch.setChampionId(match.getChampion());

      LocalDateTime tmpDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(match.getTimestamp()), ZoneId.of("America/Chicago"));

      LocalDate date = tmpDate.toLocalDate();

      miniMatch.setTimestamp(date);

      miniMatchList.add(miniMatch);
    }


//    Filtering Down to 7 Days
    List<MiniMatch> recentMatches = new ArrayList<>();
    LocalDate now = LocalDate.now(ZoneId.of("America/Chicago"));
    LocalDate sevenDaysBeforeNow = now.minusDays(7);

    for (MiniMatch match : miniMatchList) {
      if (match.getTimestamp().isAfter(sevenDaysBeforeNow)) {
        recentMatches.add(match);
      }
    }

    return recentMatches;
  }



  public TreeMap<LocalDate, Map<Long, List<MiniMatch>>> getMatchesForGraph(String userName) {
    RiotResponse riotResponse = matchClient.getMatchesByUserName(userName);


    List<MiniMatch> miniMatchList = new ArrayList<>();
    MiniMatch miniMatch;


    //Convert timestamps to dates
    List<RiotMatch> riotResponseMatches = riotResponse.getMatches();
    for (RiotMatch match : riotResponseMatches) {
      miniMatch = new MiniMatch();
      miniMatch.setChampionId(match.getChampion());

      LocalDateTime tmpDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(match.getTimestamp()), ZoneId.of("America/Chicago"));

      LocalDate date = tmpDate.toLocalDate();

      miniMatch.setTimestamp(date);

      miniMatchList.add(miniMatch);
    }


    //Filtering Down to 7 Days
    // .getRecentMatches()
    List<MiniMatch> recentMatches = new ArrayList<>();
    LocalDate now = LocalDate.now(ZoneId.of("America/Chicago"));
    LocalDate sevenDaysBeforeNow = now.minusDays(7);

    for (MiniMatch match : miniMatchList) {
      if (match.getTimestamp().isAfter(sevenDaysBeforeNow)) {
        recentMatches.add(match);
      }
    }


    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = new TreeMap<>();

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


    LocalDate now1 = LocalDate.now(ZoneId.of("America/Chicago"));
    LocalDate sevenDaysBeforeNow1 = now1.minusDays(6);
    LocalDate sevenDaysFromNow1 = sevenDaysBeforeNow1.plusDays(7);

    List<LocalDate> localDateList = sevenDaysBeforeNow1.datesUntil(sevenDaysFromNow1).collect(Collectors.toList());

    Collections.sort(localDateList);



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
