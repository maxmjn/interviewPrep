package com.company.arrays;

import java.util.Arrays;

/**
 * Created by maxmjn20 on 7/14/17.
 * ArraySort = pivot & recursion
 * -- move less than to left of pivot, greater than to right of pivot, recursion on left of pivot, recursion on right of pivot
 */
public class ArraySort {
    public static void main(String... args){
        int[] a = {4, 1, 2, 0, 9, 1};
        int low = 0;
        int high = a.length-1;
        System.out.println(Arrays.toString(a));

        Arrays.sort(a);
        System.out.println("Using built-in sort:"+ Arrays.toString(a));

        System.out.println("Using quicksort:");
        ArraySort quickSort = new ArraySort();
        quickSort.qsort(a, low, high);
        System.out.println(Arrays.toString(a));

        System.out.println("Using mergeSort bestCase O(n log n) - log n for divide array, n for merging");
        ArraySort mergeSort = new ArraySort();
        mergeSort.mergeSort(a);
        System.out.println(Arrays.toString(a));
    }

    /**
     * Find pivot
     * Move all less than pivot to left
     * Move all greater than pivot to right
     * @param a
     * @param start
     * @param end
     */
    public void qsort(int a[], int start, int end){
        if(null == a || a.length == 0 || start > end){
            return;
        }
        int middle = start + (end - start)/2; //OR (start+end)/2
        int pivot = a[middle];

        int i = start;
        int j = end;
        int temp;

        //move less than pivot to left, greater than pivot to right
        while(i <= j){
            //increment left ptr if less than pivot
            while(a[i] < pivot){
                i++;
            }
            //decrement right ptr if greater than pivot
            while (a[j] > pivot){
                j--;
            }

            //move less than pivot to left, greater than pivot to right
            if(i <= j){
                temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                //move pointers
                i++;
                j--;
            }
        }

        //at this point i & j has crossed over each other
        //recursion left of pivot
        if(start < j) qsort(a, start, j);
        //recursion right of pivot
        if(end > i) qsort(a, i, end);
    }

    /**
     * Divide array into 2 half
     * - recursively divide each half until 1 element left
     * - when we reach single element array its already sorted
     * - merge 1 element sub-arrays
     * @param arrayToSort
     * @return
     */
    public int[] mergeSort(int[] arrayToSort){
        //since recursion base condition
        if(arrayToSort.length < 2) return arrayToSort;

        //get mid index
        int midIndex = arrayToSort.length/2;

        //create left and right array
        int[] left = Arrays.copyOfRange(arrayToSort, 0, midIndex);
        int[] right = Arrays.copyOfRange(arrayToSort, midIndex, arrayToSort.length);

        //divide left and right recursively until 1 element remains
        int[] sortedLeft = mergeSort(left);
        int[] sortedRight = mergeSort(right);

        //create new array for merging left and right
        int[] sortedArray = new int[arrayToSort.length];

        //index to track left, right
        int currentLeftIndex = 0;
        int currentRightIndex = 0;

        //Imagine left and right array are each 1 element arrays
        //To pick left element - left has to be NotEmpty and
        //1. either right is empty
        //2. or left is less than right element
        //once done move on to next elements in left and right

        //start filling sortedArray
        for(int currentSortedIndex=0; currentSortedIndex < sortedArray.length; currentSortedIndex++){
            //to pick left element
            if( currentLeftIndex < sortedLeft.length /*check if left notEmpty. Imagine 1 element array length=1 and currentIndex=0 so not empty */
                    && ( currentRightIndex >= sortedRight.length /* why >= ? If array empty length=0 match index=0. Also above condition
                     for notEmpty is currentIndex < length so if Empty currentIndex > length. Combining both we have >= for empty check*/
                            || sortedLeft[currentLeftIndex] < sortedRight[currentRightIndex]
                    )){
                sortedArray[currentSortedIndex] = sortedLeft[currentLeftIndex];
                currentLeftIndex++;
            } else{ //use sortedRight array
                sortedArray[currentSortedIndex] = sortedRight[currentRightIndex];
                currentRightIndex++;
            }
        }

        return sortedArray;
    }
}
