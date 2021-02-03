package com.league.web.viewMapper;

import com.league.web.httpClient.model.MiniMatch;
import com.league.web.httpClient.ui.MatchUI;
import com.league.web.httpClient.ui.MatchUIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ChampionUsageViewMapper {

  @Autowired
  public ChampionUsageViewMapper() {
  }


  public MatchUIResponse buildMatchUIResponse(TreeMap<LocalDate, Map<Long, List<MiniMatch>>> championMatchesByDatePlayed,
                                              Map<String, List<Integer>> championData) {
    //building our UI object
    MatchUIResponse matchUIResponse = new MatchUIResponse();

    List<MatchUI> temp_match_UI_list = new ArrayList<>();
    MatchUI matchUI;
    Integer[] data;

    List<String> dateLabels = new ArrayList<>();
    // set Data labels
    for (Map.Entry<LocalDate, Map<Long, List<MiniMatch>>> localDateMapEntry : championMatchesByDatePlayed.entrySet()) {
      dateLabels.add(localDateMapEntry.getKey().toString());
    }


    String[] labelsArray = new String[dateLabels.size()];
    dateLabels.toArray(labelsArray);

    matchUIResponse.setDateLabels(labelsArray);


    for (Map.Entry<String, List<Integer>> entry : championData.entrySet()) {
      matchUI = new MatchUI();
      matchUI.setLabel(entry.getKey());

      data = new Integer[entry.getValue().size()];

      matchUI.setData(entry.getValue().toArray(data));


      if (!temp_match_UI_list.contains(matchUI)) {
        temp_match_UI_list.add(matchUI);
      }
    }

    MatchUI[] array_of_matchesUI = new MatchUI[temp_match_UI_list.size()];

    array_of_matchesUI = temp_match_UI_list.toArray(array_of_matchesUI);

    matchUIResponse.setResponse(array_of_matchesUI);

    return matchUIResponse;
  }
}
