package com.league.web.controller;

import com.league.web.httpClient.model.Match;
import com.league.web.httpClient.model.MatchResponse;
import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import com.league.web.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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

    SummonerData:
 {
      String userName;
      List<Long> timeStamp;

     ChampionInformation: {
        String championName;
        Long[] timesPlayed;
      }
   }






   FrontEnd:

   chartData = [

      {
        data: SummonerData.ChampionInformation.timesPlayed, label: SummonerData.userName
      }
   ]

   */




  @RequestMapping("/summonerLeagueWeb/{userName}")
  public MatchResponse getSummonerData(@PathVariable String userName) {
    RiotResponse riotResponse = getMatches(userName);

    MatchResponse matchResponse = new MatchResponse();

    List<Match> matches = new ArrayList<>();
    String dateFormatString;
    Match match;
    for (RiotMatch matchRiotResponse : riotResponse.getMatches()){
      System.out.println(matchRiotResponse.getChampion() + "\"" +
        matchRiotResponse.getTimestamp());

      if (isTimestampWithinRange(matchRiotResponse.getTimestamp()))
      {

        dateFormatString = ZonedDateTime.ofInstant(Instant.ofEpochMilli(matchRiotResponse.getTimestamp()),
          ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyy-MM-dd"));


        match = new Match(matchRiotResponse.getChampion(), matchRiotResponse.getRole(), dateFormatString);

        matches.add(match);

      }
    }
    matchResponse.setUsername(userName);
    matchResponse.setMatches(matches);
    return matchResponse;
  }




  /*
   * Checks if current timestamp is within range of seven days ago starting today.
   * @param epochSeconds
   * @return true if the timestamp is within range.
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
