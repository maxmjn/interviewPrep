package com.company.scratch;

import java.util.ArrayList;
import java.util.List;

public class Test {

  public static void main(String[] args) {
    String s = "occ=soc10;occ=2671896;occ=occ10;occ=2671860+edu=20178698;edu=20143430;edu=20178681+inc=2671793;"
        + "inc=20178707;inc=4;inc=20143245;inc=20178688+bn=teen;bn=teen-fiction;bn=teen-death;bn=teen-hookers;"
        + "bn=teen-sex;bn=teen-impact;bn=teen-bullying;bn=teen-memes;bn=teen-pregnancy;bn=teen-celeb;bn=teen-mom;"
        + "bn=jeopardy-teen;bn=teen-video;bn=teen-crime;bn=teen-sleepover-death;bn=teen-punished-for-stealing;"
        + "bn=teen-kills-chupacabra;bn=teen-mom-recap;bn=teen-births-drop;bn=teen-mom-2-recap;bn=teen-mom-paid;"
        + "bn=teen-birth-shocker-youtube;bn=teen-mom-nude-pics;bn=teen-shoots-chupacabra;bn=beauty;"
        + "bn=teen-mom-farrah-bikini;bn=teen-mom-salaries;bn=teen-mom-nude;bn=teen-mom-farrah-breast-implants;"
        + "bn=celebrity-beauty;bn=vintage-beauty;bn=hair-beauty;bn=uk-beauty;bn=new-jersey-teen-posted-nude-pict;"
        + "bn=teen-girl-goes-crazy-over-not-ha;bn=beauty-ideas;bn=beauty-myths;bn=beauty-canada;bn=stylelist-beauty;"
        + "bn=stacey-comerford-sleeping-beauty;bn=bv-fashion-beauty;bn=perception-of-beauty;bn=male-beauty-pageant;"
        + "bn=eden-wood-beauty-queen;bn=alexander-mcqueen-savage-beauty;bn=stylelist-beauty-trends;bn=stylelist-beauty-basics;"
        + "bn=extreme-beauty-in-vogue;bn=organic-beauty-products;bn=beauty-and-skin;bn=sarah-palin-beauty-queen;"
        + "bn=bird-feathers-beauty-industry;bn=bv-fashion-and-beauty;bn=sleeping-beauty-syndrome;bn=hero+beauty+products;"
        + "bn=stylelist-beauty-news;bn=stylelist-beauty-features;bn=6-year-old-beauty-queen;bn=beauty-blogs-for-mature-women;"
        + "bn=emma-watson-beauty-and-the-beast";

//    s = "bn=teen;bn=teen-fiction;bn=teen-death;bn=teen-hookers;bn=teen-sex;bn=teen-impact;bn=teen-bullying;"
//        + "bn=teen-memes;bn=teen-pregnancy;bn=teen-celeb;bn=teen-mom;bn=jeopardy-teen;bn=teen-video;bn=teen-crime;"
//        + "bn=teen-sleepover-death;bn=teen-punished-for-stealing;bn=teen-kills-chupacabra;bn=teen-mom-recap;"
//        + "bn=teen-births-drop;bn=teen-mom-2-recap;bn=teen-mom-paid;bn=teen-birth-shocker-youtube;"
//        + "bn=teen-mom-nude-pics;bn=teen-shoots-chupacabra;bn=beauty;bn=teen-mom-farrah-bikini;bn=teen-mom-salaries;"
//        + "bn=teen-mom-nude;bn=teen-mom-farrah-breast-implants;bn=celebrity-beauty;bn=vintage-beauty;bn=hair-beauty;"
//        + "bn=uk-beauty;bn=new-jersey-teen-posted-nude-pict;bn=teen-girl-goes-crazy-over-not-ha;bn=beauty-ideas;bn=beauty-myths;"
//        + "bn=beauty-canada;bn=stylelist-beauty;bn=stacey-comerford-sleeping-beauty;bn=bv-fashion-beauty;bn=perception-of-beauty;"
//        + "bn=male-beauty-pageant;bn=eden-wood-beauty-queen;bn=alexander-mcqueen-savage-beauty;bn=stylelist-beauty-trends;"
//        + "bn=stylelist-beauty-basics;bn=extreme-beauty-in-vogue;bn=organic-beauty-products;bn=beauty-and-skin;"
//        + "bn=sarah-palin-beauty-queen;bn=bird-feathers-beauty-industry;bn=bv-fashion-and-beauty;bn=sleeping-beauty-syndrome;"
//        + "bn=hero+beauty+products;bn=stylelist-beauty-news;bn=stylelist-beauty-features;bn=6-year-old-beauty-queen;"
//        + "bn=beauty-blogs-for-mature-women;bn=emma-watson-beauty-and-the-beast";

//    s="k1=v1";
//    s = "k1=v1++";
//    s = "k1=++v1";
//    s = "k1=v1+k2=v1";
//    s = "k1=v1+v2+k2=v1";
    s = "k1=v1+k1=v2;k1=v3";

    char EQUAL = '=';
    char PLUS = '+';

    int start = 0;
    int equal_loc = s.indexOf(EQUAL);
    String key = s.substring(start, equal_loc);
    int key_lastLoc = s.lastIndexOf(key + EQUAL);

    List<String> list = new ArrayList<>();
    int delimit_loc = s.indexOf(PLUS, key_lastLoc);//is there '+' after key_lastLoc

    //at this point delimit_loc < 0 double-check if there's delimiter
    if(delimit_loc < 0){
      delimit_loc = s.indexOf(PLUS); //handle k1=v1+k1=v2 example seg=20246028+seg=20895386;seg=20850748
    }
    while (key_lastLoc >= 0){

      //after '+' where does next key start?
      //if there's '=' after '+' we got another key
      //what if '=' is part of previous key's data - since we are dealing with key=value we can hope '=' would not be part of a key's value
      if(delimit_loc >0) {
        equal_loc = s.indexOf(EQUAL, delimit_loc);//is there next key? after delimiter

        if(equal_loc >0){
          for(int i=delimit_loc+1; i < equal_loc; i++){//look for any more delimiter between '+' and '='
            if(s.charAt(i) == PLUS) {
              delimit_loc = Math.max(delimit_loc, i);
            }
          }
        }
      }
      if(delimit_loc >0 && equal_loc >0){ //if there's next key, grab substring until delimiter
        list.add(s.substring(start, delimit_loc));
        start = delimit_loc + 1; //processed current key, move to next key
      } else{
        list.add(s.substring(start)); //example k1=v1;k1=v2;k1=v3+v4
        break; //processed till end
      }
      equal_loc = s.indexOf(EQUAL, start); //locate next key
      key = s.substring(start, equal_loc);
      key_lastLoc = s.lastIndexOf(key + EQUAL);
      delimit_loc = s.indexOf(PLUS, key_lastLoc);//is there '+' after k1_lastLoc
    }
    System.out.println(list);
  }
}
