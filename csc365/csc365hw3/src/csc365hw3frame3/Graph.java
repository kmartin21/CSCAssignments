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
public class Graph <V>{
   

    /**
     * A Map is used to map each vertex to its list of adjacent vertices.
     */
    
    

    public Map<String, List<Edge<V>>> neighbors = new HashMap<String, List<Edge<V>>>();

    /**
     * String representation of graph.
     */
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (String v : neighbors.keySet())
            s.append("\n    " + v + " -> " + neighbors.get(v));
        return s.toString();
    }

    /**
     * Add a vertex to the graph. Nothing happens if vertex is already in graph.
     */
    public void add(String vertex) {
        if (neighbors.containsKey(vertex))
            return;
        neighbors.put(vertex, new ArrayList<Edge<V>>());
    }
    
    public void addTo(String vertex) {
        neighbors.put(vertex, new ArrayList<Edge<V>>());
        
    }
    
    
    /**
     * Add an edge to the graph; if either vertex does not exist, it's added.
     * This implementation allows the creation of multi-edges and self-loops.
     */
    public void add(String from, double weight, String to) {
        this.add(from);
        this.add(to);
        neighbors.get(from).add(new Edge(from, weight, to));
    }

    public int getNumberOfEdges(){
        int sum = 0;
        for(List<Edge<V>> outBounds : neighbors.values()){
            sum += outBounds.size();
        }
        return sum;
    }
    
    public ArrayList<Edge> edgeList() {
        ArrayList<Edge> allEdges = new ArrayList<>();
        for(List<Edge<V>> e : neighbors.values()){
               for(Edge edge : e) {
                   allEdges.add(edge);
               }
        }
        
        return allEdges;
        
    }
    
    
    
    public V getVertex(String vertex){
        for(List<Edge<V>> outBounds : neighbors.values()){
            for(Edge<V> e : outBounds) {
                if(vertex.compareTo((String) e.vertex1) == 0) {
                    return e.vertex1;
                } else if(vertex.compareTo((String) e.vertex2) == 0) {
                    return e.vertex2;
                }
            }
        }
        return null;
    }
    
    public ArrayList<String> getAllVertices(){
        ArrayList<String> allEdges = new ArrayList<>();
        for(String vertex : neighbors.keySet()){
               allEdges.add(vertex);
        }
        return allEdges;
    }

    /**
     * True iff graph contains vertex.
     */
    public boolean contains(V vertex) {
        return neighbors.containsKey(vertex);
    }

    public int outDegree(String vertex) {
        return neighbors.get(vertex).size();
    }

    public int inDegree(V vertex) {
       return inboundNeighbors(vertex).size();
    }

    public List<V> outboundNeighbors(V vertex) {
        List<V> list = new ArrayList<V>();
        for(Edge<V> e: neighbors.get(vertex)){
            list.add((V) e.vertex2);
        }
        return list;
    }
    
    public int numberOfVertices() {
        int sum = 0;
        List<V> list = new ArrayList<V>();
        for (String vertex : neighbors.keySet()) {
            for (Edge<V> e : neighbors.get(vertex)) {
                sum++;
            }
        }
        return sum;
    }
    
    public List<Edge<V>> getAdj(V vertex) {
        return neighbors.get(vertex);
    }
    

    public List<V> inboundNeighbors(V inboundVertex) {
        List<V> inList = new ArrayList<V>();
        for (String to : neighbors.keySet()) {
            for (Edge<V> e : neighbors.get(to))
                if (e.vertex1.equals(inboundVertex) || e.vertex2.equals(inboundVertex))
                    inList.add((V) to);
        }
        return inList;
    }

    public boolean isEdge(V from, V to) {
      for(Edge<V> e :  neighbors.get(from)){
          if(e.vertex1.equals(to) || e.vertex2.equals(to))
              return true;
      }
      return false;
    }

    public double getWeight(V from, V to) {
        for(Edge<V> e :  neighbors.get(from)){
            if(e.vertex2.equals(to))
                return e.edgeWeight;
        }
        return -1;
    }
}
