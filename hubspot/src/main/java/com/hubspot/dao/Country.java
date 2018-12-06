package com.hubspot.dao;

import java.util.List;

public class Country{

  Integer attendeeCount;
  List<String> attendees;
  String name;
  String startDate;

  public Country() {
  }

  @Override
  public String toString() {
    return "Country{" +
        "attendeeCount=" + attendeeCount +
        ", attendees=" + attendees +
        ", name='" + name + '\'' +
        ", startDate='" + startDate + '\'' +
        '}';
  }

  public Integer getAttendeeCount() {
    return attendeeCount;
  }

  public void setAttendeeCount(Integer attendeeCount) {
    this.attendeeCount = attendeeCount;
  }

  public List<String> getAttendees() {
    return attendees;
  }

  public void setAttendees(List<String> attendees) {
    this.attendees = attendees;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
}

