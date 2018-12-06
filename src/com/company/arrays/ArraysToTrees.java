package com.company.arrays;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by maxmjn20 on 7/14/17.
 * -- Creation
 * -- Sorting
 * -- Converting to Trees
 */
public class ArraysToTrees {
    public static void main(String... args){
        int [] a={9,1,3,23,10};

        Arrays.sort(a);
        ArraysToTrees arraysToTrees = new ArraysToTrees();
        TreeNode treeNode = arraysToTrees.toBST(a);
        arraysToTrees.levelOrderQueue(treeNode);
    }

    public class TreeNode{
        public TreeNode left;
        public TreeNode right;
        public int data;

        TreeNode(int x){
            this.data = x;
        }
    }
    TreeNode toBST(int[] sortedArray){
        if(null == sortedArray || sortedArray.length == 0)
            return null;

        int start =0;
        int end = sortedArray.length-1;
        return convertToBST(sortedArray, start, end);
    }
    //build BST
    TreeNode convertToBST(int[]sortedArray, int start, int end){
        if(start > end)
            return null;
        int middle = (start + end)/2;
        TreeNode root = new TreeNode(sortedArray[middle]);
        root.left = convertToBST(sortedArray, start, middle-1);
        root.right = convertToBST(sortedArray, middle+1, end);

        return root;
    }

    public void levelOrderQueue(TreeNode root){
        //Stack myStack = new Stack();
//        Queue<TreeNode> q = new LinkedList();
        Queue<TreeNode> q = new ArrayDeque(); //ArrayDeque has O(1) for both add and remove
        int levelNodes =0;
        if(root==null) return;
        q.add(root);
        while(!q.isEmpty()){
            levelNodes = q.size();
            while(levelNodes>0){
                TreeNode n = q.remove();
                System.out.print(" " + n.data);
                if(n.left!=null) q.add(n.left);
                if(n.right!=null) q.add(n.right);
                levelNodes--;
            }
            System.out.println("");
        }
    }
}
