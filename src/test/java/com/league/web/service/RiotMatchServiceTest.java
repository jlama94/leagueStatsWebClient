package com.league.web.service;

import com.league.web.client.MatchClient;
import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



import java.util.Arrays;
import java.util.List;


class RiotMatchServiceTest {

  @Mock private MatchClient matchClient;
  @Mock private MatchService matchService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);

    matchService = new MatchService(matchClient);
  }

  /*
  @Test
  void getMatchesSuccessfully() {
    String userName = "pTeemo";
    RiotMatch match = new RiotMatch();
    match.setDate(new Long(12345));
    match.setChampion(new Long(17171717));

    List<RiotMatch> matchList = Arrays.asList(match);

    RiotResponse dummyResponse = matchClient.getMatchesByUserName(userName);

    dummyResponse.setMatchData(matchList);
    dummyResponse.setUsername(userName);

    Mockito.when(matchClient.getMatchesByUserName(userName)).thenReturn(dummyResponse);

    RiotResponse actualResponse = matchService.getMatches(userName);


    Assert.assertEquals(dummyResponse, actualResponse);
  }

   */
}
