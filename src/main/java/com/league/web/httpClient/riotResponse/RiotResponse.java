package com.league.web.httpClient.riotResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RiotResponse {
  private String username;
  private List<RiotMatch> matchData;
}
