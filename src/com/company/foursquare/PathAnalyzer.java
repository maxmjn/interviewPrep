package com.company.foursquare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PathAnalyzer {

  /**
   * /user userRootEndpoint
   * /user/friends userFriendsEndpoint
   * /user/lists userListsEndpoint
   * /user/X userEndpoint
   * /user/X/friends userFriendsEndpoint
   * /user/X/lists userListsEndpoint
   * /user/X/lists/X userListIdEndpoint
   * / rootEndpoint
   * /X/friends userFriendsEndpoint
   * /X/lists userListsEndpoint
   * /settings settingsEndpoint
   * # end of configuration, request paths to follow (this line is a comment, do not assume it will always be the same)
   * /user
   * /user/friends
   * /user/123
   * /user/123/friends
   * /user/123/friends/zzz
   * /user/friends/friends
   * /
   * /abc/lists
   * /settings
   * /aaa/bbb
   */

  static List<String> config = new ArrayList<>();
  static Map<String, String> inputOutput = new HashMap<>();

  static {
    config.add("/user userRootEndpoint");
    config.add("/user/friends userFriendsEndpoint");
    config.add("/user/lists userListsEndpoint");
    config.add("/user/X userEndpoint");
    config.add("/user/X/friends userFriendsEndpoint");
    config.add("/user/X/lists userListsEndpoint");
    config.add("/user/X/lists/X userListIdEndpoint");
    config.add("/ rootEndpoint");
    config.add("/X/friends userFriendsEndpoint");
    config.add("/X/lists userListsEndpoint");
    config.add("/settings settingsEndpoint");
    config.add("# end of configuration, request paths to follow (this line is a comment, do not assume it will always be the same)");
    config.add("/user");
    config.add("/user/friends");
    config.add("/user/123");
    config.add("/user/123/friends");
    config.add("/user/123/friends/zzz");
    config.add("/user/friends/friends");
    config.add("/");
    config.add("/abc/lists");
    config.add("/settings");
    config.add("/aaa/bbb");

    inputOutput.put("/user", "userRootEndpoint");
    inputOutput.put("/user/friends", "userFriendsEndpoint");
    inputOutput.put("/user/123", "userEndpoint");
    inputOutput.put("/user/123/friends", "userFriendsEndpoint");
    inputOutput.put("/user/123/friends/zzz", "404");
    inputOutput.put("/user/friends/friends", "userFriendsEndpoint");
    inputOutput.put("/", "rootEndpoint");
    inputOutput.put("/abc/lists", "userListsEndpoint");
    inputOutput.put("/settings", "settingsEndpoint");
    inputOutput.put("/aaa/bbb", "404");

  }

  private static String pathDelimiter = "/";
  private static String WILDCARD = "X";
  private static String HASH = "#";
  private static String EMPTY="";
  private static String NOT_FOUND = "404";

  public static void main(String[] args) {

    //Note pass command line arguments or use config
    //command line - each line must be within double quotes if not by default each space is considered as delimiter
    /**
     * "/user userRootEndpoint"
     * "/user/friends userFriendsEndpoint"
     * "/user/lists userListsEndpoint"
     * "/user/X userEndpoint"
     * "/user/X/friends userFriendsEndpoint"
     * "/user/X/lists userListsEndpoint"
     * "/user/X/lists/X userListIdEndpoint"
     * "/ rootEndpoint"
     * "/X/friends userFriendsEndpoint"
     * "/X/lists userListsEndpoint"
     * "/settings settingsEndpoint"
     * "# end of configuration, request paths to follow"
     * "/user"
     * "/user/friends"
     * "/user/123"
     * "/user/123/friends"
     * "/user/123/friends/zzz"
     * "/user/friends/friends"
     * "/"
     * "/abc/lists"
     * "/settings"
     * "/aaa/bbb"
     */

    //if config is used vs. command line args
    //args = config.toArray(args);

    List<String> input = new ArrayList<>();
    if(args.length <=0 ){
      try (InputStreamReader in = new InputStreamReader(System.in); BufferedReader buffer = new BufferedReader(in)) {
        String line;
        System.out.println("Enter data:");
        while (null!=(line = buffer.readLine()) && !"EOF".equalsIgnoreCase(line)) {
          input.add(line);
          //System.out.println(line);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }else{
      for(String s: args){
        input.add(s);
      }
    }

    Set<String> keywords = new HashSet<>();
    Map<String, String> map = new HashMap<>();
    String[] pathTokens;
    String[] tokens;
    boolean foundComment = false;
    List<String> pathToTest = new LinkedList();

    for(String s: input){
      s = s.trim();
      if(foundComment){
        pathToTest.add(s);
      }
      if(s.startsWith(HASH)){
        foundComment = true;
        continue;
      }
      tokens = s.split("\\s");
      if(null!=tokens && tokens.length > 1) {
        map.put(tokens[0], tokens[1]);
        pathTokens = tokens[0].split(pathDelimiter);
        if(null!=pathTokens){
          for(String s1: pathTokens){
            if(!s1.equalsIgnoreCase(EMPTY)) {
              keywords.add(s1);
            }
          }
        }
      }
    }

    //Test
    String expected;
    StringBuilder pathbuilder = new StringBuilder();
    String result = NOT_FOUND;
    String temp="";
    for(String s: pathToTest){

      s = s.trim();
      expected = inputOutput.get(s);
      pathbuilder.delete(0, pathbuilder.length()); //clear

      //pathToTest matches
      if(map.containsKey(s)){
        pathbuilder.append(s);
        result = map.get(pathbuilder.toString());
      }else{
        //tokenize pathToTest, replace wildcard with X
        tokens = s.split(pathDelimiter);
        if(null!=tokens && tokens.length>0){
          for (int i = 0; i < tokens.length; i++) {
            if(tokens[i].equalsIgnoreCase(EMPTY)){
              continue;
            }
            if(!keywords.contains(tokens[i])){
              tokens[i] = WILDCARD;
            }
          }
          //lookup token - you're in else block because pathToTest was not found in the lookup map.
          clearNbuild(tokens, pathbuilder);

          //no result after tokens replaced with wildcard, then all tokens are keywords
          if(!map.containsKey(pathbuilder.toString())){
            tokens = s.split(pathDelimiter);
            //apply wildcard(X) to each token
            for (int i = 0; i < tokens.length; i++) {
              if(tokens[i].equalsIgnoreCase(EMPTY)){
                continue;
              }
              if(keywords.contains(tokens[i])){
                temp = tokens[i];
                tokens[i] = WILDCARD;
              }
              clearNbuild(tokens, pathbuilder);
              if(map.containsKey(pathbuilder.toString())){
                break;
              }
              tokens[i] = temp;
            }
          }
          result = map.get(pathbuilder.toString());
        }

        if(null==result){ //keywords are appearing in the path
          result = NOT_FOUND;
        }
      }

      log(expected, pathbuilder.toString(), result);
    }
  }

  private static void clearNbuild(String[] tokens, StringBuilder sb) {
    sb.delete(0, sb.length()); //clear

    for(String s1: tokens){
      if(!s1.equalsIgnoreCase(EMPTY)) {
        sb.append(pathDelimiter).append(s1);
      }
    }
  }

  private static void log(String expected, String input, String result) {

    if(!expected.equalsIgnoreCase(result)){
      System.out.print(" FAIL....");
      System.out.println(" input:"+ input + " expected:" + expected + " ->result:" + result + " MATCHES?" + expected.equalsIgnoreCase(result));
    }else {
      System.out.println(result);
    }
  }
}
