/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365hw3frame3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author keithmartin
 */
public class DisplayMethods<V> {
    
    String root = "http://en.wikipedia.org/wiki/Science";
    Set<String> numOfSites = new HashSet<String>();
    
    //return path of dijkstra's
    public String returnPath(Graph graph, String start, String end) throws FileNotFoundException, IOException {
    
        FileInputStream fstream = new FileInputStream("edges.txt");
        String temp = "";
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            String[] tokens = strLine.split(" ");
            graph.add(tokens[0], Double.parseDouble(tokens[1]), tokens[2]);
        }
        
        String target = end;
        Dijkstra d = new Dijkstra(graph, start, end);

        Map<String, String> path = d.getPaths();
        List<String> fullPath = new ArrayList<>();
        String s = "";
            while (target != null) {
                fullPath.add(target);
                target = path.get(target);
            }
            if(fullPath.get(fullPath.size() - 1).compareTo(start) != 0) {
                s = "No path found!!!";
                return s;
            }
            Collections.reverse(fullPath);
            for (int i = 0; i < fullPath.size(); i++) {
                if(i != fullPath.size() - 1)
               s += fullPath.get(i) + " ---> " + "\n";
                else
               s += fullPath.get(i);
            }
        return s;
    }
    
    
    public void addVertex(Graph graph) {
        graph.add("KEITHISTHEBEST");
        numOfSites.add("KEITHISTHEBEST");
    }
    
    
    
    public boolean doRecurse(V vertex, Graph graph) throws FileNotFoundException, IOException {
        FileInputStream fstream = new FileInputStream("edges.txt");
        String temp = "";
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            String[] tokens = strLine.split(" ");
            graph.add(tokens[0], Double.parseDouble(tokens[1]), tokens[2]);
            numOfSites.add(tokens[0]);
            numOfSites.add(tokens[2]);
        }
        Set<V> visited = new HashSet<V>();

        recurse(vertex, graph, visited);
        
        if (visited.size() == numOfSites.size()) return true; 
        else return false;
    }
    
    public void recurse(V vertex, Graph graph, Set<V> visited) {
        //Get outbounds of current vertex
        List<V> outbounds = graph.outboundNeighbors(vertex);
        //Add this vertex to visited
        visited.add(vertex);
        //if outbounds arent null, then if visited doesnt contain
        //outbound vertex, recurse
        if(outbounds != null) {
            for(V v : outbounds) {
                if(!visited.contains(v))  
                recurse(v, graph, visited);
            }
        }
    }
    
}
