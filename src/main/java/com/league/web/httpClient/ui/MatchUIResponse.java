package com.league.web.httpClient.ui;

import com.league.web.httpClient.model.Match;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class MatchUIResponse {
  private MatchUI[] response;
}
