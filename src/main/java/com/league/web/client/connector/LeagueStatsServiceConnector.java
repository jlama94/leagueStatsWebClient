package com.league.web.client.connector;

import com.league.web.httpClient.riotResponse.RiotResponse;
import feign.Param;
import feign.RequestLine;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public interface LeagueStatsServiceConnector {

  /*
    Endpoint of stats service:

      //http://localhost:8080/matches/PTeemo
      startDate=2021-02-06&endDate=2021-02-13
  */
//  @RequestLine("GET /matches/{summonerName}")
//  RiotResponse getMatchesFromLeagueServiceClient(@Param("summonerName") String summonerName);



  @RequestLine("GET /matches/{summonerName}?startDate={startDate}&endDate={endDate}")
  RiotResponse getMatchesFromLeagueServiceClient(@Param("summonerName") String summonerName,
                                                 @Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                 @Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate);


}
