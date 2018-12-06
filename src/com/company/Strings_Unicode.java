package com.company;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

public class Strings_Unicode {

  public static void main(String[] args) {

    Strings_Unicode su = new Strings_Unicode();
    su.printBinary_Byte(new byte[]{127});
    su.printBinary_Byte(new byte[]{-128});
    byte b1 = 0b011_1__11_01; //125 in binary, underscores(one or more between numbers) allowed for readability since Java 7
    String s = new String(new byte[]{b1});
    System.out.println("byte:" + b1 + "-> String:" + s);

    s= "\u00a5"; //unicode's decimal equivalent=165, this is stored as 2 bytes within String object's char[]
    // because byte can store only max +ve 127...Java uses UTF-16 for char encoding which is a variable length encoding
    // meaning for example to store chars whose decimal value is less 128 Java UTF-16 will just use only 1-byte
    // Once char values exceed decimal value 127 Java uses 2-bytes
    System.out.println("unicode \\u00a5 is " + s);
    byte[] bytes = s.getBytes();
    byte[] bytes_UTF8 = s.getBytes(StandardCharsets.UTF_8);
    //to see how String's char[] looks like, print byte values
    su.printBinary_Byte(bytes);
    //but why is unicode's decimal equivalent=165 being stored as -62, -91 ? We can try the reverse
    //byte -> String
    s = new String(new byte[]{-62, -91});
    System.out.println("byte:{-62, -91}" + "-> String:" + s);

    //Looks like when unicode char's decimal equivalent exceeds 127 Java UTF-16 encoding uses 2-bytes
    //with 1st byte= -62, 2nd byte starting from -128 i.e 0b10000000 (2's complement)
    s="\u0080";//decimal equivalent 128
    System.out.println("unicode \\u0080 is " + s);
    su.printBinary_Byte(s.getBytes());//1st-byte=-62,2-byte=-128 and counting upwards -128,-127,-126,...

    s="\u00bf"; //decimal 191 - just before 1st-byte rolls over from -62 to -61, 2nd-byte increases from -128, -127,...to -65
    System.out.println("unicode \\u00bf is " + s);
    su.printBinary_Byte(s.getBytes());

    s="\u00c0";//decimal equivalent 192
    System.out.println("unicode \\u00c0 is " + s);
    su.printBinary_Byte(s.getBytes()); //now 1st byte=-61 reduced from -62, 2-byte=-128 and counting upwards -128,-127,-126,...

    s="\u0100";//decimal equivalent 256
    System.out.println("unicode \\u0100 is " + s);
    su.printBinary_Byte(s.getBytes()); //now 1st byte=-60 reduced from -61, 2-byte=-128 and counting upwards -128,-127,-126,...

    s = new String(new byte[]{-58, -128});
    System.out.println("byte:{-58, -128}" + "-> String:" + s);

    //at lowest level we have "binary" which is abstracted as "bytes" which is further abstracted as char or int
    //String -> char[]/int[] -> byte[]
    //So given String "ABC" = char[] {'A', 'B', 'C'} = char[]{'\u0041', '\u0042', '\u0043'} = int[]{65, 66, 67} = byte[]{65, 66, 67}
    //similarly "" = \u0080 = int 128 = byte[] {-62, -128} (note int 128 more than a byte can hold)
    s="\u0080";//decimal equivalent 128
    System.out.println("unicode \\u0080 is " + s );
    su.printCodePoints(s.toCharArray());
    s = new String(new byte[]{-62, -128});
    System.out.println("byte:{-62, -128}" + "-> String:" + s);
    int[] codepoints = new int[]{128};
    s = new String(codepoints, 0, codepoints.length);
    System.out.println("int:{128}" + "-> String:" + s);

    s = new String("ABC");
    System.out.println("String length:" + s.length()); //O(1)
    char[] c = s.toCharArray(); //O(n) as all chars of String are accessed
    System.out.println("Individual chars of String");
    System.out.println("charAt(i):" + s.charAt(2));
    for (int i = 0; i < c.length; i++) {
      System.out.println(c[i]);
    }

    System.out.println("Individual bytes of String." +
        "Bytes 8bits of numbers so put char into byte still output is 2's complement");
    bytes = s.getBytes(); //O(n) as all chars of String are accessed
    for (int i = 0; i < bytes.length; i++) {
      System.out.println(bytes[i]);
    }

    System.out.println("***************************");
    System.out.println("Only English");
    String[] strings1 = {"CCC","abc", "AAA", "BBB", "A"};
    sortUnicodeString(strings1);

    System.out.println("***************************");
    System.out.println("Only Hindi");
    String[] strings2 = {"\u0908", "\u0905", "\u0905\u0906"};
    sortUnicodeString(strings2);

    System.out.println("***************************");
    System.out.println("Both English and Hindi");
    String[] strings3 = {"a", "\u0905", "\u0905\u0906", "\u0908", "B", "abc", "abba", "abba\u0905\u0906"};
    sortUnicodeString(strings3);
  }

  private void printCodePoints(char[] chars){
    Character character = new Character(' ');
    for(int i=0; i < chars.length; i++){
      System.out.println(chars[i] + " codepoint is " + Character.codePointAt(chars, i));
    }
  }

  private void printBinary_Byte(byte[] bytes){
    System.out.println("Printing binary format for Byte");
    for(byte b: bytes){
      System.out.print(b + ":");
      int mask = 0x80; //hex equivalent of SIGNED 8-bit binary
      mask = 0b0100_0000; //underscore for readability available for Java 7 and later
      while(mask > 0){
        System.out.print((b&mask) ==0 ? 0: 1);
        mask = mask >> 1;
      }
      System.out.println();
    }
  }

  private static void sortUnicodeString(String[] strings){
    //String is char[], byte[] - how to get them
    //char[] chars = s.toCharArray();
    //byte[] bytes = s.getBytes();
    System.out.println("Before sort");
    for(String s1:strings){
      System.out.println(s1);
    }
    Byte[][] bytes1 = new Byte [strings.length][];
    int len;
    String s1;
    //String are UTF-16 unicode; for sort get bytes
    //convert String to array of Bytes(handle unicode) for comparison
    for(int i=0; i < strings.length; i++){
      s1 = strings[i];
      byte[] bytes3 = s1.getBytes();
      len = bytes3.length;
      Byte[] bytes2 = new Byte[len]; //get each char within String
      for (int j = 0; j < len ; j++) {
        bytes2[j] = new Byte(bytes3[j]);
      }
      if(bytes2.length > 0){
        bytes1[i] = bytes2;
      }
    }
    Comparator<Byte[]> byteComparator = (Byte[] b1, Byte[] b2) -> {
      int b1Value = 0;
      int b2Value = 0;
      int b1Len = b1.length;
      int b2Len = b2.length;
      //why longest? - if we iterate only for shortest length then while sort अ and अऄ
      //अ = [-32, -92, -123]
      //अऄ = [-32, -92, -123, -32, -92, -124]
      //if 'अऄ' appears first in the input then after sort output will be अऄ,अ vs अ,अऄ to avoid
      //iteration on longest
      int longest = b1Len > b2Len ? b1Len : b2Len;
      for (int i = 0; i < longest; i++) {
        //Example compare 'a', अ and अऄ
        //a = [97]
        //अ = [-32, -92, -123]
        //अऄ = [-32, -92, -123, -32, -92, -124]
        //so adding Integer.MIN_VALUE to shorter string

        //to handle English chars sorted output A,AAA vs AAA,A checking previous b1/b2Value(s)
        b1Value = i < b1Len ? b1[i].intValue() : ( b1Value >0 ? b1Len : Integer.MIN_VALUE ); //unicode like 'अ' have negative values so using Integer.MIN_VALUE vs 0.
        b2Value = i < b2Len ? b2[i].intValue() : ( b2Value >0 ? b2Len : Integer.MIN_VALUE ); //unicode like 'अ' have negative values so using Integer.MIN_VALUE vs 0.

        //English chars have +ve byte value. If your input has both English and non-English then sorted
        //output will put English after non-English chars example sorted output अऄ,अ,a
        //to bring English to top position of sorted output check if both b1 and b2 are +ve
        if(b1Value > 0 && b2Value < 0){
          b1Value = Integer.MIN_VALUE;
        }
        if(b1Value < 0 && b2Value > 0){
          b2Value = Integer.MIN_VALUE;
        }

        if(b1Value < b2Value ){
          return -1;
        } else if(b1Value > b2Value){
          return 1;
        } else{
          continue; //both byte value match
        }
      }
      return 0;
    };
    Arrays.parallelSort(bytes1, byteComparator);

    System.out.println("After sort");
    for(Byte[] bytes2: bytes1){
      len = bytes2.length;
      byte[] bytes3 = new byte[len];
      for (int i = 0; i < len ; i++) {
        bytes3[i] = bytes2[i].byteValue();
      }
      if(bytes3.length > 0){
        System.out.println(new String(bytes3));
      }
    }
  }
}
