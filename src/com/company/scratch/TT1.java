package com.company.scratch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TT1 {

  public static void main(String[] args) {
    int i = 5;

    double d1 = (double)i + 4.6; //necessary to satisfy compiler
    i += 4.6;

    System.out.println(i);
    System.out.println(d1);

    Map map = new HashMap();
    map.put("k1", "v1"); //returns previous value of key


    System.out.println(map.put("k2", "v2"));
    System.out.println(map.put("k1", "newV1"));

    String s1 = "foo";
    String s2 = "foo";
    System.out.println(s1==s2);//true, because for literal Strings Java re-uses String pool if their contents match
    s1 = new String("foo");
    s2 = new String("foo");
    System.out.println(s1==s2);//false, new operator always allocates/creates objects on heap even if their contents match

    System.out.println(ForkJoinPool.getCommonPoolParallelism());
    int[] aint = X.getAint();
    final X x = new X();
    x.s = s1;
    Y y = new Y();
    x.method(x);
    y.method(x);

    int Min = 1;
    int Max = 5;
    for (int j = 0; j < Max; j++) {
      System.out.println("Random:" + ThreadLocalRandom.current().nextInt(Min, Max + 1));
    }

    Runnable task = () -> {
      System.out.println("Best..." + Thread.currentThread().getName());
    };
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.execute(task);
    executor.execute(task);
    executor.execute(task);
    executor.shutdown();
    try {
      executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ExecutorService executorParallel = Executors.newWorkStealingPool();
    executorParallel.execute(task);
    executorParallel.execute(task);
    executorParallel.execute(task);
    executorParallel.shutdown();
    try {
      executorParallel.awaitTermination(2000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

class X{

  public static int[] getAint() {
    return aint;
  }

  private static int[] aint= new int[3];
  public String s;
  public void method(X x){
    s = "foo -> X";
//    x = new X();
    x.s = "wTF";
    System.out.println();
  }
}

class Y{
  public void method(X s){
    System.out.println(s);
  }
}
