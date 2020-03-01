package com.league.web.client.connector;

import com.league.web.httpClient.model.MatchResponse;
import feign.Param;
import feign.RequestLine;

public interface LeagueStatsServiceConnector {

  /*
    Endpoint of stats service:

      //http://localhost:8080/matches/pTeemo

  */
  @RequestLine("GET /matches/{summonerName}")
  MatchResponse getMatchesFromLeagueServiceClient(@Param("summonerName") String summonerName);
}
