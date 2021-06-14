package com.league.web.httpClient.testingObjectModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ChampionsInformationResponse {
  private List<String> championIds;
  Map<String, List<Integer>> wins;
  Map<String, List<Integer>> losses;
}
