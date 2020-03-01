package com.league.web.service;

import com.league.web.httpClient.model.MatchResponse;
import com.league.web.client.MatchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchService {
  private MatchClient matchClient;

  @Autowired
  public MatchService(MatchClient matchClient) {
    this.matchClient = matchClient;
  }

  /**
   *
   * @param userName
   * @return a MatchResponse using the MatchClient
   */
  public MatchResponse getMatches(String userName) {
    return matchClient.getMatchesByUserName(userName);
  }
}
