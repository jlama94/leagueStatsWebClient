package com.league.web.httpClient.model;

import com.league.web.httpClient.riotResponse.RiotMatch;
import com.league.web.httpClient.riotResponse.RiotResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
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

    for (RiotMatch match : riotResponse.getMatches()) {
      miniMatch = new MiniMatch();
      miniMatch.setChampionId(match.getChampion());

      LocalDateTime tmpDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(match.getTimestamp()), ZoneOffset.UTC);

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
    List<MiniMatch> result = new ArrayList<>();

    LocalDate now = LocalDate.now(ZoneOffset.UTC);
    LocalDate sevenDaysBeforeNow = now.minusDays(7);

    for (MiniMatch miniMatch : miniMatches) {
      if (miniMatch.getTimestamp().isAfter(sevenDaysBeforeNow)) {
          result.add(miniMatch);
        }
    }
    return result;
  }
}
