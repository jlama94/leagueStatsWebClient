package com.league.web.httpClient.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/*

A match coming from localhost:8080/matches/pteemo

        {
            "platformId": "NA1",
            "champion": 40,
            "season": 13,
            "role": "DUO_SUPPORT",
            "lane": "BOTTOM"
        }

 */

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Match {
  private Long champion;
  private String role;
  private String lane;
}
