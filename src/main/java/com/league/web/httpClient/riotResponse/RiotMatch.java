package com.league.web.httpClient.riotResponse;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RiotMatch {
  private Long champion;
  private Long timestamp;
  private String role;
}
