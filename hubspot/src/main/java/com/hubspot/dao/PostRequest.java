package com.hubspot.dao;

import java.util.LinkedList;
import java.util.List;

public class PostRequest {
  List<Country> countries = new LinkedList<>();

  public PostRequest() {
  }

  public PostRequest(List<Country> countries) {
    this.countries = countries;
  }

  @Override
  public String toString() {
    return "PostRequest{" +
        "countries=" + countries +
        '}';
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }
}


