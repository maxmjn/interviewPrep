package com.hubspot.dao;


import java.util.List;

public class Partner {
  List<PartnerDetails> partners;

  public Partner() {
  }

  @Override
  public String toString() {
    return "Partner{" +
        "partners=" + partners +
        '}';
  }

  public List<PartnerDetails> getPartners() {
    return partners;
  }

  public void setPartners(List<PartnerDetails> partners) {
    this.partners = partners;
  }
}

