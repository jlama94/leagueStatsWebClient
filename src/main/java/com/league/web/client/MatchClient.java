package com.league.web.client;

import com.league.web.client.connector.LeagueStatsServiceConnector;
import com.league.web.httpClient.riotResponse.RiotResponse;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// Talks to LeagueStatsService
@Component
public class MatchClient {


  public RiotResponse getMatchesByUserName(String userName, LocalDate startDate, LocalDate endDate) {
    LeagueStatsServiceConnector leagueStatsServiceConnector = Feign.builder()
      .decoder(new JacksonDecoder())
      .target(LeagueStatsServiceConnector.class, "http://localhost:8081");

    RiotResponse riotResponse = leagueStatsServiceConnector.getMatchesFromLeagueServiceClient(userName,
      startDate, endDate);

    return riotResponse;
  }
}
