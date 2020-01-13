package com.league.web.httpClient.connector;

import com.league.web.httpClient.model.MatchResponse;
import feign.Param;
import feign.RequestLine;

public interface LeagueStatsServiceConnector {

  /*
    Endpoint of stats service:

      //http://localhost:8080/v2/matches/pTeemo

  */
  @RequestLine("GET /v2/matches/{summonerName}")
  MatchResponse getMatchesFromLeagueServiceClient(@Param("summonerName") String summonerName);
}
