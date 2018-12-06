package com.company.arrays;

public class MagicSticks {
    //input a={1,6,2,3,4} maintain asc order by combining smaller values
    //{1,6,5,4} => {1,6,9} no longer can be modified, it took 2 operations 2+3 and (2+3) + 4
    public static void main(String... args) {
        MagicSticks magicSticks = new MagicSticks();
        int[] a1 = {1,6,2,3,4};
        int ops = magicSticks.getMinOperations(a1.length, a1);
        System.out.println("{1,6,2,3,4} => {1,6,9} ops needed:" + ops + "\n");
        int[] a2 = {1,3,2};
        ops = magicSticks.getMinOperations(a2.length, a2);
        System.out.println("{1,3,2} => {1,5} ops needed:" + ops + "\n");

        int[] a3 = {1,6,2,3,4,17,3};
        ops = magicSticks.getMinOperations(a3.length, a3);
        System.out.println("{1,6,2,3,4,17,3} => {1,6,9,20} ops needed:" + ops + "\n");

        int[] a4 = {1,6};
        ops = magicSticks.getMinOperations(a4.length, a4);
        System.out.println("{1,6} => {1,6} ops needed:" + ops + "\n");

        int[] a5 = {1};
        ops = magicSticks.getMinOperations(a5.length, a5);
        System.out.println("{1} => {1} ops needed:" + ops + "\n");

        int[] a6 = {6,1,2,3};
        ops = magicSticks.getMinOperations(a6.length, a6);
        System.out.println("{6,1,2,3} => {6,6} ops needed:" + ops + "\n");
    }

    private int getMinOperations(int length, int[] a) {

        if(length < 1 || a == null){
            throw new IllegalArgumentException("array is empty");
        }
        int currentValue = 0;
        int previousValue = 0;
        int operations = 0;

        for (int i=0; i < length; i++) {
            currentValue = a[i];

            //if previous > current build next higher value using subsequent elements
            // until newCurrent > previous or run out elements
            if(previousValue > currentValue){
                int newCurrentValue = 0;
                //once previous > current {1,6,2,3,4} think of {2,3,4} new sub-array
                while (i < length && previousValue > newCurrentValue ){
                    if(newCurrentValue > 0){ //increment when newCurrentValue moves
                        operations++;
                    }
                    currentValue = a[i];
                    newCurrentValue = newCurrentValue + currentValue;
                    i++;
                }
                currentValue=newCurrentValue;
            }else {
                //if set {1,6,2,3,4,17} after iteration {1,6,(2+3+4),17} prev=6, cur=2+3+4 when cur=17 set prev=9

                previousValue = currentValue;
            }
        }
        if(previousValue > currentValue){
            operations++;
        }
        System.out.println("previousValue:" + previousValue + ", currentValue:" + currentValue);
        return operations;
    }
}
