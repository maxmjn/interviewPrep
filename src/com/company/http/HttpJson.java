package com.company.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpJson {

  public static void main(String[] args) {
    try {
      URL url = new URL("https://randomuser.me/api");
      StringBuffer sb = new StringBuffer();
      try(
          InputStream is = url.openStream();
          BufferedReader br = new BufferedReader(
              new InputStreamReader(is)
          )
      ){
        String input;
        while (null!=(input=br.readLine())){
          sb.append(input);
        }
      } catch (IOException e){
       e.printStackTrace();
      }
      //use Jackson/GSON to parse JSON
      System.out.println(sb);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

  }

}
