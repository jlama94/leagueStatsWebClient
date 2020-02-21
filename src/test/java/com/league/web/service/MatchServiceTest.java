package com.league.web.service;

import com.league.web.httpClient.model.Match;
import com.league.web.httpClient.model.MatchResponse;
import com.league.web.httpClientUsage.MatchClient;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



import java.util.Arrays;
import java.util.List;


class MatchServiceTest {

  @Mock private MatchClient matchClient;
  @Mock private MatchService matchService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);

    matchService = new MatchService(matchClient);
  }

  @Test
  void getMatchesSuccessfully() {
    String userName = "pTeemo";
    Match match = new Match();
    match.setChampionId(new Long(12345));
    match.setTimestamp(new Long(171717171));

    List<Match> matchList = Arrays.asList(match);

    MatchResponse dummyResponse = matchClient.getMatchesByUserName(userName);

    dummyResponse.setMatches(matchList);
    dummyResponse.setSummonerName(userName);

    Mockito.when(matchClient.getMatchesByUserName(userName)).thenReturn(dummyResponse);

    MatchResponse actualResponse = matchService.getMatches(userName);


    Assert.assertEquals(dummyResponse, actualResponse);
  }
}
