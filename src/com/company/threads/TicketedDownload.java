package com.company.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * A utility
 */
public class TicketedDownload {

    public ExecutorService executor = Executors.newFixedThreadPool(50);

    /**
     * Gets tickets
     * @param location
     * @return ticket
     * @throws Exception when things go wrong
     */
    public Callable submitSplitJob(String location) throws Exception {
        URI uri = new URI(location);

        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                BufferedReader br = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));
                //Set lines = new HashSet();
                List<String> lines = new LinkedList<>();
                String line = br.readLine();
                do {
                    List<String> lineContents = Arrays.stream(line.split(",")).collect(Collectors.toList());
                    lines.addAll(lineContents);
                    line = br.readLine();
                } while(line != null);
                return lines;
            }
        };

        executor.submit(c);
        return c;
    }

    public static void main(String[] args) {
        TicketedDownload ticketedDownload = new TicketedDownload();

        try {
            List<Callable> tickets = new ArrayList<>();
            tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-10-rows.csv"));
            tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-100-rows.csv"));
            //TODO: Takes too long to run these?
            //tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-1000-rows.csv"));
            //tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-5000-rows.csv"));
            //tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-10000-rows.csv"));
            //tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-50000-rows.csv"));
            //tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-100000-rows.csv"));
            //tickets.add(ticketedDownload.submitSplitJob("https://www.sample-videos.com/csv/Sample-Spreadsheet-500000-rows.csv"));
            for(int i = 0; i < tickets.size(); i++){
                Set s = (Set)tickets.get(i).call();
                Iterator it = s.iterator();
                while(it.hasNext()){
                    List l = (List)it.next();
                    for(int j = 0; j < l.size(); j++){
                        System.out.println(l.get(j));
                    }
                }
            }
        } catch(Exception e){
            throw new RuntimeException("Something happened");
        }
    }
}
