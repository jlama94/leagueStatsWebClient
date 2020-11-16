package com.league.web.httpClient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class MatchResponse {
  private String username;
  private List<Match> matches;
}
