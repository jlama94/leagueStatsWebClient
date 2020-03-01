package com.league.web.controller;

import com.league.web.httpClient.model.Match;
import com.league.web.httpClient.model.MatchResponse;
import com.league.web.httpClient.model.SummonerData;
import com.league.web.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

// https://howtodoinjava.com/spring5/webmvc/spring-mvc-cors-configuration/
// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/CrossOrigin.html#allowedHeaders--
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
public class ChampionUsageController {

  private MatchService matchService;

  @Autowired
  public ChampionUsageController(MatchService matchService) {
    this.matchService = matchService;
  }


  // this sends like 100 matches
  @RequestMapping("/matches/{userName}")
  private MatchResponse getMatches(@PathVariable String userName) {
    return matchService.getMatches(userName);
  }


  @RequestMapping("/summoner/{userName}")
  public SummonerData getSummonerData(@PathVariable String userName) {
    MatchResponse response = getMatches(userName);

    SummonerData summonerData = new SummonerData();

    List<Date> dateList = new ArrayList<>();


    return summonerData;
  }





  public static void main (String[] args) {

    LocalDate date = LocalDate.now().minusDays(7);
    System.out.println(date);


    List<String> myList = new ArrayList<>(Arrays.asList("oKay", "ok", "ok", "hola"));

    Stream <String> stringStream = myList.stream();

    stringStream.forEach(s -> {
      System.out.println(s);
    });

  }








  /*
  @RequestMapping("/summonerData/{userName}")
  public SummonerData getSummonerData(@PathVariable String userName) {

    // two champions data for a chart.
    ChartData Teemo = new ChartData();
    Teemo.setData(new Integer[] {11, 21, 2, 0, 0, 2 ,4, 10});
    Teemo.setLabel("Teemo"); // #1


    ChartData garen  = new ChartData();
    garen.setData(new Integer[] {1, 2, 0, 4, 2, 1, 9}); // #2
    garen.setLabel("Garen");


    // chart data array that goes inside championChartData object.
    ChartData[] chartDataArray = new ChartData[] { Teemo, garen} ;

    // Labels array
    String[] labels = new String[] {
      "01-01-2020   okay sir",
      "01-02-2020   okay sir",
      "01-03-2020",
      "01-04-2020",
      "01-05-2020",
      "01-06-2020",
      "01-07-2020"
    };

    // champion chart data object.
    ChampionChartData championChartData = new ChampionChartData();
    championChartData.setChartData(chartDataArray);
    championChartData.setLabels(labels);

    // summonerName for SummonerData object.
    SummonerData summonerData = new SummonerData();
    summonerData.setChampionChartData(championChartData);
    summonerData.setSummonerName("pTeemo");

    return summonerData;
  }
  */
}
