package com.league.web.service;

import com.league.web.client.MatchClient;
import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


public class MatchServiceTest {

  @Mock private MatchClient matchClient;
  private MatchService matchService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    matchService = new MatchService(matchClient);
  }


  // Case map not empty?
  @Test
  public void getMatchesForGraphSuccessfullyDateNotPresentInMap() {
    //Given
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now();
    String username = "testName";

    RiotMatch riotMatch = new RiotMatch();
    Long riotTimestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    riotMatch.setTimestamp(riotTimestamp);
    riotMatch.setChampion(12L);

    RiotResponse riotResponse = new RiotResponse();
    riotResponse.setMatches(Arrays.asList(riotMatch));
    //When
    Mockito.when(matchClient.getMatchesByUserName(username, startDate, endDate)).thenReturn(riotResponse);

    //Then
    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> actual = matchService.getMatchesForGraph(username, startDate, endDate);

    MiniMatch miniMatch = MiniMatch.builder()
      .championId(12L)
      .matchDate(startDate)
      .build();

    Map<Long, List<MiniMatch>> championData = new HashMap<>();
    championData.put(12L, Arrays.asList(miniMatch));

    Map<LocalDate, Map<Long, List<MiniMatch>>> expected = new TreeMap<>();
    expected.put(startDate, championData);


    Assert.assertEquals(expected, actual);
  }

  // need two matches
  @Test
  public void getMatchesForGraphSuccessfullyChampionHasAlreadyBeenPlayedForThisDateWeWantToUpdateTheList() {
    // given
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now();
    String username = "testName";

    RiotMatch riotMatch = new RiotMatch();
    Long riotTimestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    riotMatch.setTimestamp(riotTimestamp);
    riotMatch.setChampion(12L);

    RiotMatch riotMatch2 = new RiotMatch();
    Long riotTimestamp2 = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    riotMatch2.setTimestamp(riotTimestamp2);
    riotMatch2.setChampion(12L);


    // first unique match
    MiniMatch miniMatch1 = MiniMatch.builder()
      .championId(12L)
      .matchDate(startDate)
      .build();

    // second diff match with same date
    MiniMatch miniMatch2 = MiniMatch.builder()
      .championId(12L)
      .matchDate(startDate)
      .build();



    RiotResponse riotResponse = new RiotResponse();
    riotResponse.setMatches(Arrays.asList(riotMatch, riotMatch2));


    // should contain same match twice.
    List<MiniMatch> miniMatchList = new ArrayList<>();
    miniMatchList.add(miniMatch1);
    miniMatchList.add(miniMatch2);

    // when
    Mockito.when(matchClient.getMatchesByUserName(username, startDate, endDate)).thenReturn(riotResponse);

    // then
    // build inner map
    Map<Long, List<MiniMatch>> matchesByChampionId = new LinkedHashMap<>();
    matchesByChampionId.put(12L, miniMatchList);

    // build outermap
    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> expected = new TreeMap<>();
    expected.put(startDate, matchesByChampionId);


    // expected and actual next?

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> actual = matchService.getMatchesForGraph(username, startDate, endDate);

    Assert.assertEquals(expected, actual);

  }




  @Test
  public void getMatchesForGraphSuccessfullyChampionPlayedButDifferentDate() {
    // given
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now();
    String username = "testName";


    // a riot match
    RiotMatch riotMatch = new RiotMatch();
    Long riotTimestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    riotMatch.setChampion(12L);
    riotMatch.setTimestamp(riotTimestamp);

    // same date different champion
    RiotMatch riotMatch2 = new RiotMatch();
    riotMatch2.setChampion(13L); // diff champion
    riotMatch2.setTimestamp(riotTimestamp);



    RiotResponse riotResponse = new RiotResponse();
    riotResponse.setMatches(Arrays.asList(riotMatch, riotMatch2));



    // when
    Mockito.when(matchClient.getMatchesByUserName(username, startDate, endDate)).thenReturn(riotResponse);

    MiniMatch miniMatch1 = MiniMatch.builder()
      .championId(12L)
      .matchDate(startDate)
      .build();


    MiniMatch miniMatch2 = MiniMatch.builder()
      .championId(13L)
      .matchDate(startDate)
      .build();


    List<MiniMatch> miniMatchList = new ArrayList<>();
    miniMatchList.add(miniMatch1);

    List<MiniMatch> miniMatchList2 = new ArrayList<>();
    miniMatchList2.add(miniMatch2);


    // inner map
    Map<Long, List<MiniMatch>> matchesByChampionId = new LinkedHashMap<>();
    matchesByChampionId.put(riotMatch.getChampion(), miniMatchList);
    matchesByChampionId.put(riotMatch2.getChampion(), miniMatchList2);

    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> expected = new TreeMap<>();

    expected.put(startDate, matchesByChampionId);


    // actual?
    TreeMap<LocalDate, Map<Long, List<MiniMatch>>> actual = matchService.getMatchesForGraph(username, startDate, endDate);

    Assert.assertEquals(expected, actual);

  }
}
