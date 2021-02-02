package com.league.web.httpClient.ui;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class MatchUIResponse {
  private MatchUI[] response;
  private String[] dateLabels;
}
