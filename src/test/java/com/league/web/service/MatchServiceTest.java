package com.league.web.service;

import com.league.web.client.MatchClient;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class MatchServiceTest {

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
