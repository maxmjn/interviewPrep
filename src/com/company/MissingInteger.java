package com.company;

import java.util.*;

public class MissingInteger {
  /**
   * given an array A of N integers, returns the smallest positive integer (greater than 0) that does not occur in A.
   *
   * For example, given A = [1, 3, 6, 4, 1, 2], the function should return 5.
   *
   * Given A = [1, 2, 3], the function should return 4.
   *
   * Given A = [−1, −3], the function should return 1.
   *
   * Write an efficient algorithm for the following assumptions:
   *
   * N is an integer within the range [1..100,000];
   * each element of array A is an integer within the range [−1,000,000..1,000,000]
   */
  public static void main(String[] args) {
    Solution solution = new Solution();
    solution.solution(new int[]{-1,-3,1,2,-4,3,4});
  }
}

class Solution {
  public int solution(int[] A) {
    // write your code in Java SE 8
    int len=A.length;
    int prev=0;
    int curr=0;
    int ans=-1;

    if(len > 1){
      Arrays.sort(A);
    }
    if(len==1){
      if(A[0] <1 || A[0] > 1) return 1;
      if(A[0]==1) return 2;
    }
    //sorted array - max=-ve, min>1
    if(A[len-1] <1 || A[0]>1) return 1;
    //max=1
    if(A[len-1]==1) return 2;

    //mixed cases -ve and +ve
    prev=A[0];
    curr=A[1];
    for(int i=2; i<len; i++){
      prev=curr;
      curr=A[i];
      if(curr<1 || prev<1) continue;
      if(curr>1 && prev>1 && curr-prev>1) break;
    }
    if(prev<1 && curr >1) ans=1;
    else if(curr-prev==1) ans=curr+1; //sorted input [1,2,3]
    else if(prev > 1 && curr >1) ans = prev+1;
    return ans;
  }
}