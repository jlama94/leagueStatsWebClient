package com.league.web.httpClient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// simplifies the RiotMatch
@Getter
@Setter
@EqualsAndHashCode
public class MiniMatch {
  private LocalDate timestamp;
  private Long championId;
}
