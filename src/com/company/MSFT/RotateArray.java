package com.company.MSFT;

public class RotateArray {
    public static void main(String[] args) {
        int[] array = {12, 34, 7, 5, 6, 9};
        int len = array.length;
        int k = 10;
        int toMove;
        //o(n.k) time where n=array length, k=rotation
        //o(1) space
        while(k%len > 0){
            toMove = array[len-1]; //last element to be rotated
            for (int i = len-1; i > 0; i--) {
                array[i] = array[i-1];
            }
            array[0] = toMove;
            k--;
        }
        for (int i = 0; i < len; i++) {
            System.out.print(array[i]);
            if(i < len-1) System.out.print(",");
        }
    }
}
