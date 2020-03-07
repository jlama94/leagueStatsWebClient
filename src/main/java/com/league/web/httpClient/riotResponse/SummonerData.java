package com.league.web.httpClient.riotResponse;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SummonerData {
  private String userName;
  private List<Long> championId;
  private List<String> timeStamp;
}
