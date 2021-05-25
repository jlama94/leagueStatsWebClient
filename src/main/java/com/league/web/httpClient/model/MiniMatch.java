package com.league.web.httpClient.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

// simplifies the RiotMatch
@Builder
@Getter
@EqualsAndHashCode
public class MiniMatch {
  private LocalDate matchDate;
//  private Long timestamp;
  private Long championId;
}
