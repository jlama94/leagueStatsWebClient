package com.league.web.jsonMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;


@Component
public class JSONMapper {
  private Gson gson;

  public JSONMapper() {
    GsonBuilder gsonBuilder = new GsonBuilder()
      .setPrettyPrinting()
      .serializeNulls();

    gson = gsonBuilder.create();
  }
}
