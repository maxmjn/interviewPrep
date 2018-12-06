package com.hubspot;

import java.util.Scanner;

public class Application {

  public static void main(String[] args) {
    try {
      Scanner scanner = new Scanner(System.in);

      System.out.println("GET endpoint:");
      String getEndpoint = scanner.next();

      System.out.println("POST endpoint:");
      String postEndpoint = scanner.next();

      HttpHandler getHttp = new HttpHandler(getEndpoint);
      HttpHandler postHttp = new HttpHandler(postEndpoint);
      Processor processor = new Processor(getHttp, postHttp);
      String response = processor.process();
      System.out.println(response);
    } catch (Exception e){
      System.out.println("~~~~ Please re-try. Review your input ~~~~");
    }
  }

}
