package com.company;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static org.junit.Assert.assertEquals;

/*
 * Implement the function PrintNumber.Formatter.execute() such that for a given
 * number will return a human readable string version of that number.  You can
 * safely assume that the number will be a positive, non-zero long integer.
 *
 * See PrintNumber.data() for inputs and expected outputs.
 */
@RunWith(Parameterized.class)
public class PrintNumber {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {1, "One"},
        {5, "Five"},
        {9, "Nine"},
        {10, "Ten"},
        {15, "Fifteen"},
        {19, "Nineteen"},
        {20, "Twenty"},
       { 28,      "Twenty-eight" },
       { 32,      "Thirty-two" },
       { 47,      "Forty-seven" },
       { 53,      "Fifty-three" },
       { 64,      "Sixty-four" },
       { 76,      "Seventy-six" },
//       { 100,     "One hundred" },
//       { 201,     "Two hundred and one" },
//       { 205,     "Two hundred and five" },
//       { 209,     "Two hundred and nine" },
//       { 310,     "Three hundred and ten" },
//       { 315,     "Three hundred and fifteen" },
//       { 319,     "Three hundred and nineteen" },
//       { 420,     "Four hundred and twenty" },
//       { 528,     "Five hundred and twenty-eight" },
//       { 632,     "Six hundred and thirty-two" },
//       { 747,     "Seven hundred and forty-seven" },
//       { 853,     "Eight hundred and fifty-three" },
//       { 964,     "Nine hundred and sixty-four" },
//       { 1000,    "One thousand" },
//       { 2001,    "Two thousand and one" },
//       { 2105,    "Two thousand, one hundred and five" },
//       { 2209,    "Two thousand, two hundred and nine" },
//       { 3010,    "Three thousand and ten" },
//       { 3315,    "Three thousand, three hundred and fifteen" },
//       { 3419,    "Three thousand, four hundred and nineteen" },
//       { 4020,    "Four thousand and twenty" },
//       { 4528,    "Four thousand, five hundred and twenty-eight" },
//       { 1234512, "One million, two hundred thirty-four thousand, five hundred and twelve" },
//       { 9223372036854775807l, "Nine quintillion, two hundred twenty-three quadrillion, three hundred seventy-two trillion, thirty-six billion, eight hundred fifty-four million, seven hundred seventy-five thousand, eight hundred and seven "}
    });
  }


  public static class Formatter {

    public static String execute(final long input) {
      //mod by Pow(10,i) i=1 until input > 0
      //if mod=0 then if div=input/Pow(10,i-1) < 10 then look-up string
      //if mod>0 then look-up string
      List<String> list = new LinkedList<String>();
      int I;
      long copyInput = input;
      for(int digitsPlace = 1; copyInput > 0; digitsPlace++){
        long mod = (long)(copyInput % Math.pow(10, digitsPlace));
        long div = (long) (copyInput / Math.pow(10, digitsPlace));
        if(digitsPlace == 1 && div > 0 && div <= 2){ //since we start with 10^1 check for TEENS
          I = Integer.parseInt("" + copyInput);
          if(TEENS.containsKey(I)) {
            list.add(TEENS.get(I));
            copyInput = 0;
          }
          if(mod == 0) {
            if(TENS.containsKey((int) div)){
              list.add(TENS.get((int) div));
              copyInput = 0;
            }
          }
        }
        if(copyInput == 0) continue;
        if(mod == 0){
          I = Integer.parseInt("" + copyInput);
          if(TEENS.containsKey(I)) list.add(TEENS.get(I));
          if(TENS.containsKey(I)) list.add(TENS.get(I));
          copyInput = 0;
        } else {
          div = -1; //reset
          //example input=28, 2nd iteration copyInput=20, digitsPlace=2 then mod=20/100
          if(digitsPlace > 1) {
            div = (long) (copyInput / Math.pow(10, digitsPlace - 1));
          }
          if(div > 0 && div < 10){
            I = Integer.parseInt("" + div);
          }else {
            I = Integer.parseInt("" + mod);
          }
          if (digitsPlace == 1 && ONES.containsKey(I)) {
            list.add(ONES.get(I));
          }
          if (digitsPlace == 2) {
            if(TEENS.containsKey(I)) list.add(TEENS.get(I));
            if(TENS.containsKey(I)) list.add(TENS.get(I));
          }
          copyInput = copyInput - mod;
        }
      }//for
      StringBuilder sb = new StringBuilder();
      if (list.size() > 0) {
        int size = list.size();
        for (int i = size - 1, j = 1; i >= 0; i--, j++) {
          if(j == 1){
            sb.append(list.get(i));
          }else {
            sb.append("-").append(list.get(i).toLowerCase());
          }
        }//for
      }
      return sb.toString();
    }
    private static final Map<Integer, String> ONES;

    private static final Map<Integer, String> TEENS;

    private static final Map<Integer, String> TENS;

    static {
      final Map<Integer, String> ones = new HashMap<Integer, String>();
      ones.put(1, "One");
      ones.put(2, "Two");
      ones.put(3, "Three");
      ones.put(4, "Four");
      ones.put(5, "Five");
      ones.put(6, "Six");
      ones.put(7, "Seven");
      ones.put(8, "Eight");
      ones.put(9, "Nine");

      final Map<Integer, String> teens = new HashMap<Integer, String>();
      teens.put(10, "Ten");
      teens.put(11, "Eleven");
      teens.put(12, "Twelve");
      teens.put(13, "Thirteen");
      teens.put(14, "Fourteen");
      teens.put(15, "Fifteen");
      teens.put(16, "Sixteen");
      teens.put(17, "Seventeen");
      teens.put(18, "Eighteen");
      teens.put(19, "Nineteen");

      final Map<Integer, String> tens = new HashMap<Integer, String>();
      tens.put(2, "Twenty");
      tens.put(3, "Thirty");
      tens.put(4, "Forty");
      tens.put(5, "Fifty");
      tens.put(6, "Sixty");
      tens.put(7, "Seventy");
      tens.put(8, "Eighty");
      tens.put(9, "Ninety");

      ONES = unmodifiableMap(ones);
      TEENS = unmodifiableMap(teens);
      TENS = unmodifiableMap(tens);
    }
  }

  @Parameter(0)
  public long input;

  @Parameter(1)
  public String expected;

  @Test
  public void test() {
    assertEquals(expected, Formatter.execute(input));
  }

  public static void main(final String[] args) {
    JUnitCore.main(PrintNumber.class.getCanonicalName());
  }
}

