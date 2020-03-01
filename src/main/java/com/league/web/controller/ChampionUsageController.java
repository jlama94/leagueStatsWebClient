package com.league.web.controller;

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


  /*
    Intention is to LeagueServiceApp to return an object like this


    SummonerData:
 {
      String userName;
      List<Long> timeStamp;

     ChampionInformation: {
        String championName;
        Long[] timesPlayed;
      }
   }


   FrontEnd:

   chartData = [

      {
        data: SummonerData.ChampionInformation.timesPlayed, label: SummonerData.userName
      }
   ]



   */
  @RequestMapping("/summoner/{userName}")
  public SummonerData getSummonerData(@PathVariable String userName) {
    MatchResponse response = this.getMatches(userName);

    SummonerData summonerData = new SummonerData();

    List<Date> dateList = new ArrayList<>();


    return summonerData;
  }





  public static void main (String[] args) {

    LocalDate date = LocalDate.now().minusDays(7);
    System.out.println(date);


    List<String> myList = new ArrayList<>(Arrays.asList("oKay", "ok", "ok", "hola"));

    Stream<String> stringStream = myList.stream();

    stringStream.forEach(s -> {
      System.out.println(s);
    });

  }
}
