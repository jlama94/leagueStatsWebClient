package com.league.web.client;

import com.league.web.client.connector.LeagueStatsServiceConnector;
import com.league.web.configuration.YAMLConfig;
import com.league.web.httpClient.detailedResponse.DetailedMatchResponse;
import com.league.web.httpClient.riotResponse.RiotResponse;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// Talks to LeagueStatsService
// call the YAML object here and replace target
@Component
public class MatchClient {

  private YAMLConfig yamlConfig;

  @Autowired
  public MatchClient(YAMLConfig config) {
    yamlConfig = config;
  }


  public RiotResponse getMatchesByUserName(String userName, LocalDate startDate, LocalDate endDate) {
    LeagueStatsServiceConnector leagueStatsServiceConnector = Feign.builder()
      .decoder(new JacksonDecoder())
      .target(LeagueStatsServiceConnector.class, yamlConfig.getLolServiceApplicationTarget());

      //.target(LeagueStatsServiceConnector.class, "http://localhost:8080");

    RiotResponse riotResponse = leagueStatsServiceConnector.getMatchesFromLeagueServiceClient(userName,
      startDate, endDate);

    return riotResponse;
  }


  public DetailedMatchResponse getDetailedMatchResponse(String userName, LocalDate startDate, LocalDate endDate) {
    LeagueStatsServiceConnector leagueStatsServiceConnector = Feign.builder()
      .decoder(new JacksonDecoder())
      .target(LeagueStatsServiceConnector.class, yamlConfig.getLolServiceApplicationTarget());



//      .target(LeagueStatsServiceConnector.class, "http://localhost:8080");

    DetailedMatchResponse detailedMatchResponse = leagueStatsServiceConnector
      .getDetailedMatchResponse(userName, startDate, endDate);

    return detailedMatchResponse;
  }
}
