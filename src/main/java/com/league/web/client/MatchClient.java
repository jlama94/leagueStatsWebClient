package com.league.web.client;

import com.league.web.client.connector.LeagueStatsServiceConnector;
import com.league.web.httpClient.model.MatchResponse;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.stereotype.Component;

@Component
public class MatchClient {


  public MatchResponse getMatchesByUserName(String userName) {
    LeagueStatsServiceConnector leagueStatsServiceConnector = Feign.builder()
      .decoder(new JacksonDecoder())
      .target(LeagueStatsServiceConnector.class, "http://localhost:8080");

    MatchResponse matchResponse = leagueStatsServiceConnector.getMatchesFromLeagueServiceClient(userName);

    return matchResponse;
  }
}
