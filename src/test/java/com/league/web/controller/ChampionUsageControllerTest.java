package com.league.web.controller;

import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.ui.MatchUIResponse;
import com.league.web.service.MatchService;
import com.league.web.viewMapper.ChampionUsageViewMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ChampionUsageControllerTest {

  @Mock private ChampionUsageViewMapper championUsageViewMapper;
  @Mock private MatchService matchService;

  private ChampionUsageController championUsageController;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    championUsageController = new ChampionUsageController(matchService, championUsageViewMapper);
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

  @Test
  public void getSummonerDataV2ReturnsSuccessfully() {
    //Given - These set of parameters
    String userName = "pteemo";

    MatchUIResponse expected = new MatchUIResponse();

    LocalDate today = LocalDate.now().minusDays(1);
    LocalDate sevenDaysAgoFromToday = today.minusDays(6);

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed = new TreeMap<>();

    //When -- Things that should happen
    Mockito.when(matchService.getMatchesForGraph(userName, today, sevenDaysAgoFromToday)).thenReturn(championMatchesByDatePlayed);

    Mockito.when(championUsageViewMapper.buildMatchUIResponse(championMatchesByDatePlayed)).thenReturn(expected);

    //Then - Assert that things equal
    MatchUIResponse actual = championUsageController.getSummonerDataV2(userName);

    Assert.assertEquals(expected, actual);
  }
}
