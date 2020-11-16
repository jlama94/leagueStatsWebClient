package com.league.web.client;

import com.league.web.client.connector.LeagueStatsServiceConnector;
import com.league.web.httpClient.riotResponse.RiotResponse;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.stereotype.Component;

// Talks to LeagueStatsService
@Component
public class MatchClient {

  public RiotResponse getMatchesByUserName(String userName) {
    LeagueStatsServiceConnector leagueStatsServiceConnector = Feign.builder()
      .decoder(new JacksonDecoder())
      .target(LeagueStatsServiceConnector.class, "http://localhost:8080");

    RiotResponse riotResponse = leagueStatsServiceConnector.getMatchesFromLeagueServiceClient(userName);

    return riotResponse;
  }
}
