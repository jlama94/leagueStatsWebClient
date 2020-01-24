package com.league.web.assets.model;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Champion {
  private String key; // will map with the championId.
  private String name;
  private String title;
  private String lore; // story of the champion
}
