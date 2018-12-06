
public class FizzBuzz{

  public static void main(String[] args) {
//    System.out.println("FizzBuzz");
    int start=1;
    int end =100;
    fizzbuzz(start, end);
  }

  private static void fizzbuzz(int start, int end){
    for (int i = start; i <=end ; i++) {
      if(i%3==0 && i%5==0){
        System.out.println("FizzBuzz");
      } else if(i%3==0){
        System.out.println("Fizz");
      } else if(i%5==0){
        System.out.println("Buzz");
      }else {
        System.out.println(i);
      }
    }
  }
}