package com.league.web.httpClient.detailedResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class DetailedMatchResponse {
  private String username;
  private List<Match> matches;
}
