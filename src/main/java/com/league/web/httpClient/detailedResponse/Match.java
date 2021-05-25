package com.league.web.httpClient.detailedResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
  private long champion;
  private long timestamp;
  private String role;
  private String lane;
  private boolean win;
  private long gameId;
}
