package control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;

public class Test {

    private static void putIntoCounterMap(Map<String,Integer> map, String string){
        if(map.containsKey(string)){
            map.put(string, map.get(string)+1);
        }else{
            map.put(string,1);
        }
    }

    private static List<Integer> getSortedCounters(Map<String,Integer> map){
        List<Integer> counters = new ArrayList<>(map.values());
        Collections.sort(counters);
        counters.removeIf(i -> i ==1);
        return counters;
    }

    public static void main (String[] a) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("/Users/philipfrerk/Downloads/disambiguations_en.ttl"));
        Map<String,Integer> knownLines = new HashMap<>();
        Map<String,Integer> nodes = new HashMap<>();
        Map<String,Integer> edges = new HashMap<>();
        Map<String,Integer> basicDigrams = new HashMap<>();

        for(String line:lines){
            putIntoCounterMap(knownLines, line);
            String[] strings = line.split(" ");
            putIntoCounterMap(nodes, strings[0]);
            putIntoCounterMap(nodes, strings[2]);
            putIntoCounterMap(edges, strings[1]);
        }






        System.out.println(" "+ getSortedCounters(edges).size());




    }
}
