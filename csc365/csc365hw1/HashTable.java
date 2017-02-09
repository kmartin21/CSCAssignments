/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webpagecategorize;

import java.util.ArrayList;

/**
 *
 * @author keithmartin
 */
public class HashTable {

    //Initial table size
    private int table_size = 16;
    //The threshold for the table, when reached used to calculate maxSize
    private float threshold = (float) .75;
    //Initial maxSize of the table
    private int maxSize = 12;
    //Increases by one every time element is added
    private int size = 0;
    public String urlString;
    HashEntry[] table;

    HashTable() {
        //Creates a table of the HashEntry class with a initial size of 16
        table = new HashEntry[table_size];
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public int getTableSize() {
        return table.length;
    }

    class HashEntry {

        //Stores the word
        private String key;
        //Stores the count of the word
        private int value;
        //Set as the next key, used for chaining method
        private HashEntry next;

        public HashEntry(String key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public String getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public HashEntry getNext() {
            return next;
        }

        public void setNext(HashEntry next) {
            this.next = next;
        }
    }

    public int get(String k) {

        //Create hash
        int hash = Math.abs(k.hashCode() % table.length);
        //If index is null keep going
        if (table[hash] == null) {
            return 0;
        } else {
            //Create entry variable
            HashEntry entry = table[hash];
            //while index isnt null and the keys arent equal then move along linked list
            while (entry != null && !entry.key.equalsIgnoreCase(k)) {
                entry = entry.getNext();
            }
            //If spot in linked list is null then keep moving
            if (entry == null) {
                return 0;
                //if keys are equal then return value
            } else if (entry.key.equalsIgnoreCase(k)) {
                return entry.value;
            }

        }
        return 0;
    }

    //Put method using chaining to resolve collisions
    public void put(String k, int v) {
        //Create hash
        int hash = Math.abs(k.hashCode() % table.length);
        //if index is null then put in new entry
        if (table[hash] == null) {
            table[hash] = new HashEntry(k, v);
            //Increment "size" to keep track of elements in table
            size++;
        } else {
            //Create "entry" variable to store hash index
            HashEntry entry = table[hash];
            //While next index is not null and key is not equal to k, move along linked list
            while (entry.getNext() != null && !entry.key.equalsIgnoreCase(k)) {
                //Move onto next element
                entry = entry.getNext();
            }

            //if key is equal to k then increment the count value by 1
            if (entry.key.equalsIgnoreCase(k)) {
                entry.value = entry.value + v;
            } else {
                //Otherwise, set the next entry to the new key thats being passed in
                entry.setNext(new HashEntry(k, v));
                size++;
            }
        }

        //If the amount of elements in the table (size) is greater than 75% of the table size (maxSize)
        //then call the resize(); method to increase the size of the table
        if (size >= maxSize) {
            resize();
        }
    }

    private void resize() {

        //Create new doubled table size
        int newTableSize = 2 * table.length;
        //Set maxSize to 75% of the newTableSize
        maxSize = (int) (newTableSize * threshold);
        //Create new table set to original table to use to iterate through
        HashEntry[] oldTable = table;
        //Set the current table with the newTableSize
        table = new HashEntry[newTableSize];
        //Set the size variable back to zero to keep track of amount of elements inside the new table
        size = 0;

        //Used to keep track of next element
        HashEntry next;

        //Iterate through the elements in the oldTable and use put method to place them into the current, new table
        for (HashEntry element : oldTable) {
            next = element;
            //Loop through the linked list
            while (true) {
                // See if next element is null
                if (next == null) {
                    // The element doesn't exist so break
                    break;
                } else {
                    //It does exist so, rehash element and re-put into new table
                    this.put(next.getKey(), next.getValue());
                    // Get next element and move on
                    next = next.getNext();
                }
            }
        }
    }

    //Method to return all keys
    public String[] getKeys() {

        // Create next element to move through linked list
        HashEntry next;

        // Create arrayList
        ArrayList<String> arrayList = new ArrayList<>();

        for (HashEntry element : table) {
            next = element;
            //Loop through the linked list
            while (true) {
                // See if next element is null
                if (next == null) {
                    // The element doesn't exist so break
                    break;
                } else {
                    //It does exist so, add word to arrayList
                    arrayList.add(next.getKey());
                    // Get next element and move on
                    next = next.getNext();
                }
            }

        }
        //Return all of the keys, with array set to size of the ArrayList
        return arrayList.toArray(new String[arrayList.size()]);
    }
}
