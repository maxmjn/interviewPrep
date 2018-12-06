package com.company.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Throttle {

  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(5);
    Runnable task = new RequestAllowanceImpl(); //creates new Request
    for (int i = 0; i < 10; i++) {
      executor.execute(task); //execute vs. submit; execute catches exception while submit swallows it - example try a task with divide by zero
    }

    executor.shutdown();
    try {
      executor.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

interface RequestAllowance{
  void checkAllowed(Request request);
}

class RequestAllowanceImpl implements RequestAllowance, Runnable{ //Runnable for testing

  final static int ALLOWED_MAX_REQUESTS = 2;
  final static long THROTTLE_WINDOW_NANOSECS = 10;
  final static int minUser = 1;
  final static int maxUser = 3;

  //Map is shared among multiple Threads so ReqCounter has Atomic* fields; could use ConcurrentHashMap()
  //Atomic* is better than applying lock on entire map with ConcurrentHashMap
  public static Map<String, ReqCounter> map = new HashMap<>();

  @Override
  public void run() {
    Request request = new Request();

    int random = ThreadLocalRandom.current().nextInt(minUser, maxUser + 1);
    String user = "U-"+random;
    long ts = System.nanoTime();
    request.setUsername(user);
    request.setTimestamp(ts);
    System.out.println("Created:" + user + " with " + ts);
    long randomSleepInMilliSecs = ThreadLocalRandom.current().nextLong(1L, THROTTLE_WINDOW_NANOSECS+1);
    try {
      //to simulate delay between requests
      Thread.sleep(randomSleepInMilliSecs);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    checkAllowed(request);
  }

  @Override
  public void checkAllowed(Request request) {
    long currentTS = System.currentTimeMillis();
    long reqTS = request.getTimestamp();
    String user = request.getUsername();
    if(map.containsKey(user)){
      System.out.println(user + " found!");
      ReqCounter reqCounter = map.get(user);
      int cnt = reqCounter.getCount().get(); //AtomicInteger to handle concurrency

      //check if incoming request for given user is within 1 sec
      if(currentTS-reqTS <= THROTTLE_WINDOW_NANOSECS){
        if(cnt > ALLOWED_MAX_REQUESTS){
          System.out.println(user + " request# " + cnt + " exceeds 100/sec");
          return;
        }else {
          //update count
          AtomicInteger atomicInteger = reqCounter.getCount();
          atomicInteger.incrementAndGet();
          reqCounter.setCount(atomicInteger);
          System.out.println(user + " request# " + atomicInteger.get() + " allowed...");
          return;
        }
      }
      //check if incoming request for given user > 1 sec
      if(currentTS-reqTS > THROTTLE_WINDOW_NANOSECS){
        //RESET since 1sec window has passed
        reqCounter.setTsInitial(new AtomicLong(reqTS)); //set current request TS
        reqCounter.setCount(new AtomicInteger(1)); //reset counter

        System.out.println(user + " request RESET");
      }
    } else {
      AtomicLong ts = new AtomicLong(reqTS);
      AtomicInteger cnt = new AtomicInteger(1);
      ReqCounter reqCounter = new ReqCounter(cnt, ts);
      map.put(user, reqCounter);
      System.out.println(user + " request# " + cnt.get() + " CREATED");
      return;
    }
  }

  private class ReqCounter{
    private AtomicInteger count; //using Atomic to handle concurrency
    private AtomicLong tsInitial; //initial timestamp request received

    public AtomicInteger getCount() {
      return count;
    }

    public void setCount(AtomicInteger count) {
      this.count = count;
    }

    public AtomicLong getTsInitial() {
      return tsInitial;
    }

    public void setTsInitial(AtomicLong tsInitial) {
      this.tsInitial = tsInitial;
    }

    public ReqCounter(AtomicInteger count, AtomicLong tsInitial) {
      this.count = count;
      this.tsInitial = tsInitial;
    }

  }
}

class Request{
  private String username;
  private Long timestamp;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

}
