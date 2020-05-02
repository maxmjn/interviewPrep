/*
Given two arrays A and B of length N, determine if there is a way to make A equal to B by reversing any subarrays from array B any number of times.
Signature
bool areTheyEqual(int[] arr_a, int[] arr_b)
Input
All integers in array are in the range [0, 1,000,000,000].
Output
Return true if B can be made equal to A, return false otherwise.
Example
A = [1, 2, 3, 4]
B = [1, 4, 3, 2]
output = true
After reversing the subarray of B from indices 1 to 3, array B will equal array A.
 */
package arrays;

import java.io.*;
import java.util.*;
// Add any extra import statements you may need here


class ReverseToMakeEqual {

    // Add any helper functions you may need here

    boolean areTheyEqual(int[] array_a, int[] array_b) {
        // Write your code here
        if(array_a.length != array_b.length) return false;

        Map<Integer, Integer> mapA = new HashMap<>();
        Map<Integer, Integer> mapB = new HashMap<>();
        for(int i=0; i < array_b.length; i++){
            //put array element and count
            mapA.put(array_a[i], mapA.getOrDefault(array_a[i], 1) + 1);
            mapB.put(array_b[i], mapB.getOrDefault(array_b[i], 1) + 1);
        }
        int key;
        int val;
        for(Map.Entry<Integer,Integer> entry : mapA.entrySet()){
            key=entry.getKey();
            val=entry.getValue();
            if(mapB.containsKey(key) && mapB.get(key)==val) continue;
            else return false;
        }
        return true;
    }

    // These are the tests we use to determine if the solution is correct.
    // You can add your own at the bottom, but they are otherwise not editable!
    int test_case_number = 1;
    void check(boolean expected, boolean output) {
        boolean result = (expected == output);
        char rightTick = '\u2713';
        char wrongTick = '\u2717';
        if (result) {
            System.out.println(rightTick + " Test #" + test_case_number);
        }
        else {
            System.out.print(wrongTick + " Test #" + test_case_number + ": Expected ");
            System.out.print(expected);
            System.out.print(" Your output: ");
            System.out.print(output);
            System.out.println();
        }
        test_case_number++;
    }
    void printString(String str) {
        System.out.print("[" + str + "]");
    }

    public void run() {
        int[] array_a_1 = {1, 2, 3, 4};
        int[] array_b_1 = {1, 4, 3, 2};
        boolean expected_1 = true;
        boolean output_1 = areTheyEqual(array_a_1, array_b_1);
        check(expected_1, output_1);

        int[] array_a_2 = {1, 2, 3, 4};
        int[] array_b_2 = {1, 4, 3, 3};
        boolean expected_2 = false;
        boolean output_2 = areTheyEqual(array_a_2, array_b_2);
        check(expected_2, output_2);
        // Add your own test cases here

    }

    public static void main(String[] args) {
        new ReverseToMakeEqual().run();
    }
}