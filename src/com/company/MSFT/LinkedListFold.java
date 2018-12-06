package com.company.MSFT;

public class LinkedListFold {
    int value;
    LinkedListFold next;

    public LinkedListFold(){}

    public LinkedListFold(int value){
        this.value = value;
        this.next = null;
    }

    /**
     * Folds linkedList
     * @param args
     */
    public static void main(String... args){
        LinkedListFold linkedListNode = new LinkedListFold();
        LinkedListFold l1 = linkedListNode.build();
        System.out.println("Original");
        linkedListNode.print(l1);
        //traverse linkedList to get size
        int size = getSize(l1);
        System.out.println("Size:" + size);

        //find mid-point, break-up list
        LinkedListFold l2 = splitMidPoint(l1, size/2);
        System.out.println("Mid-point:" + l2.value);
        System.out.println("After split: l1");
        linkedListNode.print(l1);
//        System.out.println("After split: l2");
//        linkedListNode.print(l2);

        LinkedListFold l2head = linkedListNode.reverseInPlace(l2);
        System.out.println("l2 reverseInPlace");
        linkedListNode.print(l2head);

        //folding list
        LinkedListFold foldedList = foldList(l1, l2head);
        System.out.println("Folded list");
        linkedListNode.print(foldedList);
    }

    /**
     *
     * @param l1
     * @param l2
     * @return
     */
    private static LinkedListFold foldList(LinkedListFold l1, LinkedListFold l2) {
        LinkedListFold curL1 = l1;
        LinkedListFold nextL1;
        LinkedListFold curL2 = l2;
        LinkedListFold nextL2;
        while(null!=curL1 && null!=curL2){
            nextL1 = curL1.next;
            nextL2 = curL2.next;
            curL1.next = curL2;
            curL2.next = nextL1;
            curL1 = nextL1;
            curL2 = nextL2;
        }
        return l1;
    }

    /**
     * returns mid-point of list
     * @param head
     * @param limit
     * @return
     */
    private static LinkedListFold splitMidPoint(LinkedListFold head, int limit) {
        if(null==head || limit<=0){
            return null;
        }
        int i=0;
        LinkedListFold prev=null;
        while (null!=head){
            if(i >=limit){
                break;
            }
            i++;
            prev=head;
            head = head.next;
        }
        prev.next=null; //list is split
        return head;
    }

    /**
     * Returns linked list size
     * @param head
     * @return
     */
    private static int getSize(LinkedListFold head) {
        int i=0;
        LinkedListFold node = head;
        if(null==node){
            return i;
        }
        while(null!=node){
            i++;
            node = node.next;
        }
        return i;
    }

    /**
     * Builds data
     * @return
     */
    public LinkedListFold build(){
        LinkedListFold current = null;
        for(int i=8; i > 0; i--){
            LinkedListFold node = new LinkedListFold(i);
            node.next = current;
            current = node;
        }
        return current;
    }

    /**
     * Displays linkedlist
     * @param head
     */
    public void print(LinkedListFold head){

        LinkedListFold current = head;
        while(current != null){
            System.out.print(current.value + "->");
            current = current.next;
        }
        System.out.println(current);
    }

    /**
     * Reverses linkedList
     * @param head
     * @return
     */
    public LinkedListFold reverseInPlace(LinkedListFold head){
        LinkedListFold current = head;
        LinkedListFold previous = null;
        LinkedListFold nextNode = null;

        while(current != null){
            nextNode = current.next;
            current.next = previous;
            previous = current;
            current = nextNode;
        }
        return previous;
    }
}
