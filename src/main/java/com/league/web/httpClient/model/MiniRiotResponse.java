package com.league.web.httpClient.model;

import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

// simplifies the RiotResponse, just returns a list of matches with champs IDs and timestamps
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class MiniRiotResponse {

  private List<MiniMatch> miniMatches;
  private RiotResponse riotResponse;

  public MiniRiotResponse(RiotResponse riotResponse) {
    this.riotResponse = riotResponse;
    miniMatches = new ArrayList<>();
    MiniMatch miniMatch;

    for (int i = 0; i < riotResponse.getMatches().size(); i++) {

      RiotMatch riotMatch = riotResponse.getMatches().get(i);

      miniMatch = new MiniMatch();
      miniMatch.setChampionId(riotMatch.getChampion());

      LocalDateTime tmpDate = LocalDateTime
        .ofInstant(Instant.ofEpochMilli(riotMatch.getTimestamp()), ZoneOffset.UTC);


      LocalDate date = tmpDate.toLocalDate();


      miniMatch.setTimestamp(date);


      miniMatches.add(miniMatch);
    }
  }

  public List<MiniMatch> getAllMatches() {
    return miniMatches;
  }

  // seven recent matches
  public List<MiniMatch> getRecentMatches() {
    for (int i = miniMatches.size() - 1; i >= 7; i--) {
      miniMatches.remove(miniMatches.get(i));
    }
    return miniMatches;
  }
}
