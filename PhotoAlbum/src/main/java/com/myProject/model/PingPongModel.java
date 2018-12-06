package com.myProject.model;

public class PingPongModel {
  String ping;
  String pong;
  String error;

  public PingPongModel() {
  }

  @Override
  public String toString() {
    return "PingPongSvc{" +
        "ping='" + ping + '\'' +
        ", pong='" + pong + '\'' +
        ", error='" + error + '\'' +
        '}';
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getPing() {
    return ping;
  }

  public void setPing(String ping) {
    this.ping = ping;
  }

  public String getPong() {
    return pong;
  }

  public void setPong(String pong) {
    this.pong = pong;
  }
}
