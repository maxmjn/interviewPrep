package com.company;

import java.util.Arrays;
import java.util.Comparator;

public class Scratch {

  // Decimal declaration and possible chars are [0-9]
  int decimal    =  495;

  // HexaDecimal declaration starts with 0X or 0x and possible chars are [0-9A-Fa-f]
  int hexa       =  0X1EF;

  // Octal declaration starts with 0 and possible chars are [0-7]
  int octal      =  0757;

  // Binary representation starts with 0B or 0b and possible chars are [0-1]
  int binary     =  0b111101111;

  //If the number is string format then you can convert it into int using the below
  String text = "0b111101111";
  int value = text.toLowerCase().startsWith("0b") ? Integer.parseInt(text.substring(2), 2) : Integer.decode(text);

  public static void main(String[] args) {
    String s = "ABC";
    System.out.println(s.codePointAt(0));
  }
}
