package com.hubspot.dao;

import java.util.List;

public class PartnerDetails{
  String firstName;
  String lastName;
  String email;
  String country;
  List<String> availableDates;

  public PartnerDetails() {
  }

  @Override
  public String toString() {
    return "PartnerDetails{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", country='" + country + '\'' +
        ", availableDates=" + availableDates +
        '}';
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public List<String> getAvailableDates() {
    return availableDates;
  }

  public void setAvailableDates(List<String> availableDates) {
    this.availableDates = availableDates;
  }
}
