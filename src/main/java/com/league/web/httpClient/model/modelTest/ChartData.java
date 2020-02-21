package com.league.web.httpClient.model.modelTest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ChartData {
  private Integer[] data;
  private String label;
}
