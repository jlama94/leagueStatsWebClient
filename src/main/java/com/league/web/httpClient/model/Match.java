package com.league.web.httpClient.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Match {
  private Long champion;
  private String role;
  private String timestamp;
}
