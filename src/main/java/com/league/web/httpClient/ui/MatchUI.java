package com.league.web.httpClient.ui;

import lombok.*;

@EqualsAndHashCode
@Getter
@Builder
public class MatchUI {
  private Integer[] data;
  private String label;
}
