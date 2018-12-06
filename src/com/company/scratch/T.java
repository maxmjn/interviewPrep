package com.company.scratch;

import java.math.BigInteger;

public class T {

  public static void main(String[] args) {
    BigInteger i = new BigInteger("0");
    System.out.println(i.intValue());
    add(i);
    System.out.println(i.intValue());
  }

  private static void add(BigInteger i){
    i = i.add(BigInteger.valueOf(1L));
    System.out.println(i.intValue());
  }
}
