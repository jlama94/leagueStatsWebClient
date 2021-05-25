package com.league.web.httpClient.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Loss {
  public List<Integer> losses;
  public final String name = "Losses";

  public Loss(List<Integer> gatheredLosses) {
    this.losses = new ArrayList<>();
    this.losses.addAll(gatheredLosses);
  }
}
