import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class People {

  class Person{
    public String name;
    public int age;
    public String state;
  }

  public static void main(String[] args) {
    People people = new People();
    people.process();
  }

  private void process() {
    //Stream files
    URL url = ClassLoader.getSystemResource("people.csv");
    Optional<URL> urlOptional = Optional.ofNullable(url);
    try {
      if(urlOptional.isPresent()){
        String path = urlOptional.get().getPath();
//        long numberOfLines =
//            Files.lines(Paths.get(path), Charset.defaultCharset())
//                .count();
//        System.out.println("File line-count:" + numberOfLines);

        int totalAge = 0;
        double avgAge = 0.0;
        //parse file into a List<Person>
        List<Person> personList = new LinkedList<>();
        Files.lines(Paths.get(path), Charset.defaultCharset())
            .forEach(rec -> {
              String[] data = rec.split(",");
              if(data.length == 3
                  && !data[0].equalsIgnoreCase("name")) //discard header row
              {
                Person person = new Person();
                personList.add(person);

                //TODO handle validity conditions
                person.name = data[0];
                person.age = Integer.parseInt(data[1]);
                person.state = data[2];
              }
            });

        Map<String, Integer> mapStateCount = new HashMap<>();
        Map<String, List<Integer>> mapStateAge = new HashMap<>();
        Map<String, List<Integer>> mapStateAge_15_55 = new HashMap<>();
        if(personList.size() > 0){
          for(Person p: personList){
            totalAge = totalAge + p.age;

            //State - count
            if(mapStateCount.containsKey(p.state)){
              int i =mapStateCount.get(p.state);
              i++;
              mapStateCount.put(p.state, i);
            }else{
              mapStateCount.put(p.state, 1);
            }

            //State - Age average
            if(mapStateAge.containsKey(p.state)){
              List<Integer> list =mapStateAge.get(p.state);
              list.add(p.age);
              mapStateAge.put(p.state, list);
            }else{
              List<Integer> list = new LinkedList<>();
              list.add(p.age);
              mapStateAge.put(p.state, list);
            }

            //State - Age average 15 - 55
            if(p.age > 15 && p.age < 55) {
              if (mapStateAge_15_55.containsKey(p.state)) {
                List<Integer> list = mapStateAge_15_55.get(p.state);
                list.add(p.age);
                mapStateAge_15_55.put(p.state, list);
              } else {
                List<Integer> list = new LinkedList<>();
                list.add(p.age);
                mapStateAge_15_55.put(p.state, list);
              }
            }
          }
          System.out.println("Average Age:");
          avgAge = (double) totalAge/personList.size();
          System.out.println(avgAge);

          System.out.println("Number of people/State");
          Set<Entry<String, Integer>> set = mapStateCount.entrySet();
          Comparator<Entry<String, Integer>> comparator = (i, j) -> {
            if(j.getValue() > i.getValue()){
              return 1;
            } else if( i.getValue() > j.getValue()){
              return -1;
            }
            return 0;
          };
          List<Entry<String, Integer>> list = new LinkedList<>(set);
          Collections.sort(list, comparator);
          for(Map.Entry<String, Integer> entry: list){
            System.out.println(entry.getKey() + "," + entry.getValue());
          }

          System.out.println("State Avg age");
          Map<String, Double> mapStateAvgAge = new HashMap<>();
          for(Map.Entry<String, List<Integer>> entry: mapStateAge.entrySet()){
            List<Integer> list1 = entry.getValue();
            //get average
            int sum = list1.stream().reduce(0, (a,b) -> a+b);
            double avg = (double) sum/list1.size();
            mapStateAvgAge.put(entry.getKey(), Math.ceil(avg));
          }
          Set<Entry<String, Double>> set2 = mapStateAvgAge.entrySet();
          Comparator<Entry<String, Double>> comparator2 = (i, j) -> {
            if(j.getValue() > i.getValue()){
              return 1;
            } else if( i.getValue() > j.getValue()){
              return -1;
            }
            return 0;
          };
          List<Entry<String, Double>> list2 = new LinkedList<>(set2);
          Collections.sort(list2, comparator2);
          for(Map.Entry<String, Double> entry: list2){
            System.out.println(entry.getKey() + "," + entry.getValue());
          }


          System.out.println("State Avg age > 15 AND age < 55");
          Map<String, Long> mapTree = new TreeMap<>();
          for(Map.Entry<String, List<Integer>> entry: mapStateAge_15_55.entrySet()){
            List<Integer> list1 = entry.getValue();
            //get average
            int sum = list1.stream().reduce(0, (a,b) -> a+b);
            double avg = (double) sum/list1.size();
            mapTree.put(entry.getKey(), Math.round(avg));
          }
//          set2 = mapStateAvgAge.entrySet();
//          list2 = new LinkedList<>(set2);
//          Collections.sort(list2, comparator2);
          for(Map.Entry<String, Long> entry: mapTree.entrySet()){
            System.out.println(entry.getKey() + "," + entry.getValue());
          }

        }

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
