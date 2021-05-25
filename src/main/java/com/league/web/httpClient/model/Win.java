package com.league.web.httpClient.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Win {
  public List<Integer> wins;
  public final String name = "Wins";

  public Win(List<Integer> gatheredWins) {
    this.wins = new ArrayList<>();
    this.wins.addAll(gatheredWins);
  }
}
