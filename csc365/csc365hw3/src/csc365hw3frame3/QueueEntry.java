/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365hw3frame3;

/**
 *
 * @author keithmartin
 */
public class QueueEntry<V> {
    
        public V entryName;
        public double cost;
        public int pos;
        public int parentPos = -1;
        

        public QueueEntry(V entryName, double cost){
            this.entryName = entryName; 
            this.cost = cost;
        }

        public V getEntry() {
            return entryName;
        }

        public double getWeight() {
            return cost;
        }
        
        public void setCost(double cost) {
            this.cost = cost;
        } 

        public void setParentPos(int pos) {
            this.parentPos = pos;
        }
        
        public void setPos(int pos) {
            this.pos = pos;
        }
        
        @Override
        public String toString() {
            return "Vertex [vertex=" + entryName + ", weight=" + cost + "]";
        }
    
}
