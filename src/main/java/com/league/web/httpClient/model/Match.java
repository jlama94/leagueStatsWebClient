package com.league.web.httpClient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Match {
  private Long championId;
  private Long timestamp;
}
