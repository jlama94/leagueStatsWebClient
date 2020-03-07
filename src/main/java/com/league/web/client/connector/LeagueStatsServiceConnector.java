package com.league.web.client.connector;

import com.league.web.httpClient.riotResponse.RiotResponse;
import feign.Param;
import feign.RequestLine;

public interface LeagueStatsServiceConnector {

  /*
    Endpoint of stats service:

      //http://localhost:8080/matches/pTeemo

  */
  @RequestLine("GET /matches/{summonerName}")
  RiotResponse getMatchesFromLeagueServiceClient(@Param("summonerName") String summonerName);
}
