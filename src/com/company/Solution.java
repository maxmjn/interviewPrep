package com.company;

import static java.util.Collections.unmodifiableMap;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/*
 * Implement the function Solution.Formatter.execute() such that for a given
 * number will return a human readable string version of that number.  You can
 * safely assume that the number will be a positive, non-zero long integer.
 *
 * See Solution.data() for inputs and expected outputs.
 */
@RunWith(Parameterized.class)
public class Solution {

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
        // { 28,      "Twenty-eight" },
        // { 32,      "Thirty-two" },
        // { 47,      "Forty-seven" },
        // { 53,      "Fifty-three" },
        // { 64,      "Sixty-four" },
        // { 76,      "Seventy-six" },
        // { 100,     "One hundred" },
        // { 201,     "Two hundred and one" },
        // { 205,     "Two hundred and five" },
        // { 209,     "Two hundred and nine" },
        // { 310,     "Three hundred and ten" },
        // { 315,     "Three hundred and fifteen" },
        // { 319,     "Three hundred and nineteen" },
        // { 420,     "Four hundred and twenty" },
        // { 528,     "Five hundred and twenty-eight" },
        // { 632,     "Six hundred and thirty-two" },
        // { 747,     "Seven hundred and forty-seven" },
        // { 853,     "Eight hundred and fifty-three" },
        // { 964,     "Nine hundred and sixty-four" },
        // { 1000,    "One thousand" },
        // { 2001,    "Two thousand and one" },
        // { 2105,    "Two thousand, one hundred and five" },
        // { 2209,    "Two thousand, two hundred and nine" },
        // { 3010,    "Three thousand and ten" },
        // { 3315,    "Three thousand, three hundred and fifteen" },
        // { 3419,    "Three thousand, four hundred and nineteen" },
        // { 4020,    "Four thousand and twenty" },
        // { 4528,    "Four thousand, five hundred and twenty-eight" },
        // { 1234512, "One million, two hundred thirty-four thousand, five hundred and twelve" },
        // { 9223372036854775807l, "Nine quintillion, two hundred twenty-three quadrillion, three hundred seventy-two trillion, thirty-six billion, eight hundred fifty-four million, seven hundred seventy-five thousand, eight hundred and seven "}
    });
  }

  /*
  if input < 10, module by 10, look up
  if input < 100 T = divide by 10, t= module by 10, look up
  if input < 1000  H = divide by 100, h = module by 100, look up
  */

  public static class Formatter {

    public static String execute(final long input) {
      /* Implementation goes here! */
      //Module by 10
      //Lookup
      //Add to stack
      //divide by 10
      //finally pop stack
      List<String> list = new LinkedList<String>();
      long inputVal = input;
      int pos = 0;
      long val = 0;
      Integer I = Integer.parseInt(inputVal + "");
      Integer T = Integer.parseInt((inputVal / 10) + "");
      if (TEENS.containsKey(I)) {
        list.add(TEENS.get(I));
      } else if (TENS.containsKey(T)) {
        list.add(TENS.get(T));
      } else {
        while (inputVal > 0) {
          val = inputVal % 10;
          inputVal = inputVal / 10;

          //System.out.println("val:" + val);
          I = Integer.parseInt("" + val);
          if (ONES.containsKey(I)) {
            list.add(ONES.get(I));
          }
        }//end of while
      }

      StringBuilder sb = new StringBuilder();
      //System.out.println("size:" + list.size());
      if (list.size() > 0) {
        int size = list.size();
        for (int i = size - 1; i >= 0; i--) {
          sb.append(list.get(i));
        }//for
      }
      //System.out.println("sb:" + sb);
      return sb.toString();
    }

    private static final Map<Integer, String> ONES;

    private static final Map<Integer, String> TEENS;

    private static final Map<Integer, String> TENS;

    static {
      final Map<Integer, String> ones = new HashMap<>();
      ones.put(1, "One");
      ones.put(2, "Two");
      ones.put(3, "Three");
      ones.put(4, "Four");
      ones.put(5, "Five");
      ones.put(6, "Six");
      ones.put(7, "Seven");
      ones.put(8, "Eight");
      ones.put(9, "Nine");

      final Map<Integer, String> teens = new HashMap<>();
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

      final Map<Integer, String> tens = new HashMap<>();
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
    JUnitCore.main(Solution.class.getCanonicalName());
  }
}

