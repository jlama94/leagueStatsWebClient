package com.league.web.httpClient.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchHistoryTracker {
  public Win win;
  public Loss loss;
}
