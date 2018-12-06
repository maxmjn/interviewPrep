package com.company.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Java8AtomicRef {

  public static void main(String[] args) {

    ExecutorService executor = Executors.newWorkStealingPool();
    executor = Executors.newFixedThreadPool(10);
    Runnable task;
    Stack s = new Stack();
    char[] chars = {'A', 'B', 'C', 'D', 'E'};
    for (int i = 0; i < 5; i++) {
      char c = chars[i];
      task = ()-> {
        s.push(String.valueOf(c));
      };
      executor.execute(task);
    }
    try {
      Thread.sleep(5000); //sleep to wait for tasks to finish
      executor.shutdown();
      executor.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Non atomic counter:" + s.counter + " vs. Atomic counter:" + s.atomicCounter);
    String output = s.pop();
    while (output != null){
      System.out.println(output);
      output = s.pop();
    }
  }
}

class Stack {
  private final AtomicReference<Element> head = new AtomicReference<>(null);
  public int counter;
  public AtomicInteger atomicCounter  = new AtomicInteger(0);

  public void push(String value){
    Element newElement = new Element(value);
    counter++;
    atomicCounter.incrementAndGet();

    while(true){
      Element oldHead = head.get();
      newElement.next = oldHead;

      //Trying to set the new element as the head
      if(head.compareAndSet(oldHead, newElement)){
        return;
      }
    }
  }

  public String pop(){
    while(true){
      Element oldHead = head.get();

      //The stack is empty
      if(oldHead == null){
        return null;
      }

      Element newHead = oldHead.next;

      //Trying to set the new element as the head
      if(head.compareAndSet(oldHead, newHead)){
        return oldHead.value;
      }
    }
  }

  private static final class Element {
    private final String value;
    private Element next;

    private Element(String value) {
      this.value = value;
    }
  }
}