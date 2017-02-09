/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365hw3frame3;

/**
 *
 * @author keithmartin
 */
public class Edge<V> {
    
    public V vertex1;
    public V vertex2;
    public double edgeWeight;
    
    public Edge(V vertex1, double edgeWeight, V vertex2) {
        this.vertex1 = vertex1;
        this.edgeWeight = (1/edgeWeight);
        this.vertex2 = vertex2;
    }
    
    public void setEdgeWeight(double weight) {
        this.edgeWeight = weight;
    } 
    
    public V getOther(V vertex) {
        if(vertex == vertex1) return vertex2;
        else return vertex1;
    }
    
    @Override
        public String toString() {
            return "Vertex [vertex1= " + vertex1 + " edge weight= " + edgeWeight + " vertex2= " + vertex2 + "]";
        }
    
}
