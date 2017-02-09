/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365hw3frame3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author keithmartin
 */
public class Dijkstra<T> {

    public Graph graph;
    public String source;
    public String target;
    MinHeap queue;
    boolean found = false;

    public Dijkstra(Graph graph, String source, String target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        queue = new MinHeap(graph.getNumberOfEdges());
    }

    //Calculates shortest path using Dijkstra's algorithm
    public Map<String, String> getPaths() {
        //Map storing the cost of each url
        Map<String, Double> dist = new HashMap<>();
        //Map storing the key as the previous of value
        Map<String, String> prev = new HashMap<>();
        //Set source distance to source to zero
        dist.put(source, 0.0);
        //Only insert source
        queue.insert(new QueueEntry(source, 0.0));
        //Store all vertices
        ArrayList<String> allVertices = graph.getAllVertices();
        for (String vertex : allVertices) {
            if (source.compareTo(vertex) != 0) {
                //Set cost for each vertex
                dist.put(vertex, Double.POSITIVE_INFINITY);
            }
        }
        //While queue is not empty
        while (!queue.isEmpty()) {
           
            //Get smallest vertex in queue and set it
            QueueEntry entry = queue.deleteMin();
            
            //Stop method when you reach the target
            if (target.compareTo((String) entry.entryName) == 0) {
                break;
            }
            //Get all outbounds of current entryName
            List<String> neighborVertices = graph.outboundNeighbors(entry.entryName);
            for (String neighborVertex : neighborVertices) {
                
                //Calculate distance
                double distance = dist.get(entry.entryName) + graph.getWeight(entry.entryName, neighborVertex);
                //If distance is less than neighborVertex cost
                //Then set that vertex cost to distance and put associated previous in map
                if (distance < dist.get(neighborVertex)) {
                    dist.put(neighborVertex, distance);
                    prev.put(neighborVertex, (String) entry.entryName);
                    //insert new queueEntry with new distance
                    queue.insert(new QueueEntry(neighborVertex, distance));
                }
            }
        }
        return prev;
    }
}
