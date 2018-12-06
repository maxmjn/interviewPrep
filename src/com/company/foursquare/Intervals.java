package com.company.foursquare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Intervals {

  /**
   * For given input in "any order"
   * 1 2
   * 2 3
   * 3 4
   * 4 5
   * There's no gap between intervals 1-2-3-4-5
   *
   * Problem statement for given input intervals find missing interval gap(s) for example
   * 1 2
   * 3 4
   * 2-3 interval is missing so expected output is "2 3"
   * @param args
   */
  public static void main(String[] args) {

    List<String> input = new ArrayList<>();
    if(args.length <=0 ){
      input.clear();
      try (InputStreamReader in = new InputStreamReader(System.in); BufferedReader buffer = new BufferedReader(in)) {
        String line;
        System.out.println("Enter data:");
        while (null!=(line = buffer.readLine()) && !"EOF".equalsIgnoreCase(line)) {
          input.add(line);
          //System.out.println(line);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }else{
      input.clear();
      for(String s: args){
        input.add(s);
      }
    }

    int begin=1;
    int end=1;
    String[] data;
    int p1;
    int p2;
    List<Interval> gaps = new LinkedList<>();

//    for(String s: input){
//      data = s.trim().split("\\s");
//      if(null!=data && data.length>1) {
//        try {
//          p1 = Integer.parseInt(data[0]);
//          p2 = Integer.parseInt(data[1]);
//        } catch (NumberFormatException e) {
//          e.printStackTrace();
//          continue;
//        }
//
//        //build gaps
//        //p1..p2 could be within begin..end - no gap
//        // p1...begin..p2..end - no gap, begin=p1
//        // begin..p1..end...p2 - no gap, end=p2
//        // p1..p2..begin..end - gap is p2...begin
//        // begin...end..p1..p2 - gap is end...p1
//        if(p1 >= end){
//          if(p1!=end) {
//            gaps.add(new Interval(end, p1));
//          }
//        }
//        if(p2 <= begin){
//          if(p2!=begin) {
//            gaps.add(new Interval(p2, begin));
//          }
//        }
//        //begin-end
//        if(begin==1 || p1 <= begin){
//          begin=p1;
//        }
//        if(end==1 || p2 >= end){
//          end=p2;
//        }
//
//        //fill gaps
//        updateGaps(gaps, p1, p2);
//
//      }//if-data
//    }//for-loop
//    for(Interval gap: gaps) {
//      System.out.println(gap);
//    }

    List<Interval> fillers = new LinkedList<>();
    begin = -1;
    end = -1;
    for(String s: input) {
      data = s.trim().split("\\s");
      if (null != data && data.length > 1) {
        try {
          p1 = Integer.parseInt(data[0]);
          p2 = Integer.parseInt(data[1]);
        } catch (NumberFormatException e) {
          e.printStackTrace();
          continue;
        }
        fillers.add(new Interval(p1, p2));
        if(begin==-1 || (p1 <= begin)){
          begin = p1;
        }
        if(end==-1 || (p2 >= end)){
          end = p2;
        }
      }
    }

    gaps = fillGaps(new Interval(begin, end), fillers);
    Comparator<Interval> intervalComparator = (o1, o2) -> {
      if(o1.start == o2.start && o1.finish==o2.finish){
        return 0;
      }else if(o1.start < o2.start && o1.finish < o2.finish){
        return -1;
      }else {
        return 1;
      }
    };
    Collections.sort(gaps, intervalComparator);
    for(Interval gap: gaps) {
      System.out.println(gap);
    }
  }

  /**
   * "fillers" are used to fill ranges available
   * @param gapToFill
   * @param fillers
   * @return
   */
  private static List<Interval> fillGaps(Interval gapToFill, List<Interval> fillers) {
    int p1;
    int p2;
    int start = gapToFill.start;
    int finish = gapToFill.finish;
    int gapSize;
    boolean allgapsFilled = false;
    List<Interval> gaps = new LinkedList<>();
    gaps.add(new Interval(start, finish));
    for(Interval filler: fillers){
      p1 = filler.start;
      p2 = filler.finish;
      gapSize = gaps.size(); //to handle adding new gaps
      for (int i = 0; i < gapSize ; i++) {
        start = gaps.get(i).start;
        finish = gaps.get(i).finish;

        // p1...p2 could be start=p1&&p2=finish no gaps to fill or start...p1...p2...finish gaps are (start,p1), (p2,finish)
        if(p1==start && p2==finish){
          //all gaps filled
          allgapsFilled = true;
          break;
        }
        if((start <= p2 && p2 <= finish) || (start <= p1 && p1 <= finish)){ //assumption is p1 <= p2
          gaps.remove(i);
          //add new gap
          if(p1 >= start && p1<= finish){
            if(p1 != start) {
              gaps.add(new Interval(start, p1));
            }
          }
          if(p2 >= start && p2 <= finish){
            if(p2!=finish) {
              gaps.add(new Interval(p2, finish));
            }
          }
        }
      }//for-loop-gaps
      if(allgapsFilled){
        gaps.clear();
        break;
      }
    }//for-loop fillers
    return gaps;
  }

//  private static void updateGaps(List<Interval> gaps, int p1, int p2) {
//    if(null==gaps || gaps.size() <=0){
//      return;
//    }
//    int start;
//    int finish;
//    List<Interval> newGaps = new LinkedList<>();
//    for(int i=0; i < gaps.size(); i++){
//      start = gaps.get(i).start;
//      finish = gaps.get(i).finish;
//
//      //skip gaps that are not in range
//      // p1...p2 could be start=p1&&p2=finish or p1...start...p2...finish or start...p1...p2...finish or start...p1...finish...p2
//      if((start <= p2 && p2 <= finish) || (start <= p1 && p1 <= finish)){ //assumption is p1 <= p2
//        gaps.remove(i);
//
//        //p1=start, p2=end nothing to do
//        //add new gap
//        if(p1 >= start && p1<= finish){
//          if(p1 != start) {
//            newGaps.add(new Interval(start, p1));
//          }
//        }
//        if(p2 >= start && p2 <= finish){
//          if(p2!=finish) {
//            newGaps.add(new Interval(p2, finish));
//          }
//        }
//      }
//    }//for-loop
//
//    if(newGaps.size()>0){
//      gaps.addAll(newGaps);
//    }
//  }
}
//for prototyping using public variables
class Interval {
  public int start;
  public int finish;
  public Interval(int start, int stop){
   this.start = start;
   this.finish = stop;
  }

  @Override
  public String toString() {
    return this.start + " " + this.finish;
  }

}