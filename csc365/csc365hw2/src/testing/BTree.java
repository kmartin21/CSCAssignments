/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author keithmartin
 */
public class BTree {

    // Random access file used in bTree
    private RandomAccessFile raf; 
    // Key byte block
    private static final int KEY_BLOCK = 150; 
    // Frequency byte block
    private static final int FREQUENCY_BLOCK = 4; 
    // Pointer byte block
    private static int POINTER_BLOCK = 8;
    // Composite byte block
    private static final int COMPOSITE_BLOCK = KEY_BLOCK + FREQUENCY_BLOCK;
    // Node byte block
    private int NODE_BLOCK = COMPOSITE_BLOCK * (MAX-1);
    // Max elements per node in btree (MAX-1)
    private static final int MAX = 10;    // max elements per B-tree node = MAX-1
    // Btree root
    private Node root;       
    // Btree height
    private int HT;               
    // num of key-value pairs in btree
    private int numKeyValuePairs;
    // url string to set url for btree
    private String urlString;      

    // Btree node class
    public static final class Node {

        // number of elements in current node
        private int numElements;         
        // array holding the elements inside node
        private Entry[] elements = new Entry[MAX]; 
        // pointer to the node, used to seek to next node when reading the raf
        private long pointer;

        // Node constructor with k elements
        private Node(int n) {
            numElements = n;
        }             
    }

   
     //internal nodes only use key and next 
     //leaf nodes only use key and value
     
    private static class Entry {

        private String key;
        private String pageUrl;
        private int frequency = 1;
        //used to iterate over nodes
        private Node next;     

        public Entry(String key, String pageUrl, Node next) {
            this.key = key;
            this.pageUrl = pageUrl;
            frequency = 1;
            this.next = next;
        }
    }

    // Btree constructor, takes in a raf name used to intialize the raf for current btree
    public BTree(String fileName) throws FileNotFoundException, Exception {
        root = new Node(0);
        try {
            raf = new RandomAccessFile(fileName, "rw");
        } catch (IOException e) {
            throw new Exception("Cannot open " + fileName + ", Error: " + e);
        }
    }

    public BTree() {
        root = new Node(0);
    }

    // returns number of key-value pairs in btree
    public int size() {
        return numKeyValuePairs;
    }

    // returns btree height
    public int getHeight() {
        return HT;
    }

    /* 
     * return frequency of word thats passed in, if found
     * return 0 if word isn't in btree
     */
    public int get(String key) {
        return search(root, key, HT);
    }

    // returns root node
    public Node getRootNode() {
        return root;
    }

    // returns url string associated with btree
    public String getUrlString() {
        return urlString;
    }

    // sets url string for btree
    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    // calls findAndIncrementFrequency to increment frequency of that key
    public void incrementKey(String key) {
        findAndIncrementFrequency(root, key, HT);
    }

    // finds key and increments frequency if key passed in is already there
    private void findAndIncrementFrequency(Node node, String key, int ht) {
        Entry[] children = node.elements;

        // external node
        if (ht == 0) {
            for (int j = 0; j < node.numElements; j++) {
                if (eq(key, children[j].key)) {
                    children[j].frequency++;
                }
            }
        } // internal node
        else {
            for (int j = 0; j < node.numElements; j++) {
                if (eq(key, children[j].key)) {
                    children[j].frequency++;
                } else if (j + 1 == node.numElements || less(key, children[j + 1].key)) {
                    findAndIncrementFrequency(children[j].next, key, ht - 1);
                }
            }
        }
    }


    /*
     * Put method for btree:
     * this method calls insert if key isn't already in tree
     * first increments num of keyvalue pairs
     * then goes into insert
     * if insert returns a null node then checkForSplit does not need to occur and exit
     * 
     */
    public void put(String key, String value) {
        //Search and check if there
        if (get(key) == 0) {
            Node newNode = insert(root, key, value, HT);
            numKeyValuePairs++;
            if (newNode == null) {
                return;
            }

            // have to split the root node
            splitRoot(newNode);
            
        } else {
            // increment frequency of key if its there
            incrementKey(key);
        }
    }
    
   // method to split root
    public void splitRoot(Node newNode) {
        // Create new root node with size 2
        Node newRoot = new Node(2);
        // Set roots left children
        newRoot.elements[0] = new Entry(root.elements[0].key, null, root);
        // Set roots right children
        newRoot.elements[1] = new Entry(newNode.elements[0].key, null, newNode);
        root = newRoot;
        HT++;
    }
    
    /*
     * insert method
     * 
     */

    private Node insert(Node node, String key, String pageUrl, int ht) {
        int insertPos;
        Entry newEntry = new Entry(key, pageUrl, null);

        // leaf node
        //if treeheight = 0, the node to insert to is a leaf node
        if (ht == 0) {
                for (insertPos = 0; insertPos < node.numElements; insertPos++) {
                //if the new key is less than key being compared to, then stop
                if (less(key, node.elements[insertPos].key)) {
                    break;
                }
            }
        } // this node has children, internal node
        else {
            
            for (insertPos = 0; insertPos < node.numElements; insertPos++) {
                // check node to see if you're at end of node or if key is less than current key being compared to
                if ((insertPos + 1 == node.numElements) || less(key, node.elements[insertPos + 1].key)) {
                    //recursive call for internal node to get to leaf with the node
                    //as the node's next child 
                    Node tempNode = insert(node.elements[insertPos++].next, key, pageUrl, ht - 1);
                    if (tempNode == null) {
                        // stop insertion
                        return null; 
                    }
                    // if tempNode doesnt return null then set the key and next for newEntry after tempNode has been split
                    newEntry.key = tempNode.elements[0].key;
                    newEntry.next = tempNode;
                    break;
                }
            }
        }

        //Shift elements right
        for (int i = node.numElements; i > insertPos; i--) {
            node.elements[i] = node.elements[i - 1];
        }

        // set elements here
        node.elements[insertPos] = newEntry;

        //increase number of elements
        node.numElements++;
        
        // Check if split is needed
        return checkForSplit(node);
        

    }

    // checkForSplit node in half
    private Node checkForSplit(Node node) {
        if(node.numElements < MAX) {
            //Don't split yet
            return null;
        }
        // split is needed
        // create new root node with half of max num of elements
        Node newNode = new Node(MAX / 2);
        // node that was inserted has half of max num of elements
        node.numElements = MAX / 2;
        for (int j = 0; j < MAX / 2; j++) {
            // put elements into root node to the right after split
            newNode.elements[j] = node.elements[MAX / 2 + j];
        }
        return newNode;
    }

    
    
    private int search(Node node, String key, int ht) {
        Entry[] children = node.elements;

        if (ht == 0) {
            for (int j = 0; j < node.numElements; j++) {
                if (eq(key, children[j].key)) {
                    return children[j].frequency;
                }
            }
        } else {
            for (int j = 0; j < node.numElements; j++) {
                if (j + 1 == node.numElements || less(key, children[j + 1].key)) {
                    return search(children[j].next, key, ht - 1);
                }
            }
        }
        return 0;
    }
    
    //recursively searches to return all the words in btree
    public ArrayList<String> getKeys(Node node, int ht) {
        Entry[] children = node.elements;
        ArrayList<String> allKeys = new ArrayList();

        if (ht == 0) {
            for (int j = 0; j < node.numElements; j++) {
                allKeys.add(children[j].key);
            }
        } else {
            for (int j = 0; j < node.numElements; j++) {
                allKeys.addAll(getKeys(children[j].next, ht - 1));
            }
        }

        allKeys.removeAll(Arrays.asList(null, ""));

        return allKeys;
    }

    // goes through every node and writes it in order into the raf
    public void writeIntoFile(Node node, int ht) throws IOException {
        Entry[] elements = node.elements;
        String blockString = "";
        String keyDataBlock;
        String freqDataBlock;
        String pointerDataBlock;

        
        if (ht == 0) {
            
            // set pointer to node
            node.pointer = raf.getFilePointer();
            // create data block
            pointerDataBlock = Raf.createDataBlock(node.pointer + "", POINTER_BLOCK);
            raf.write(pointerDataBlock.getBytes());
            long startOfNode = raf.getFilePointer();
            for (int j = 0; j < node.numElements; j++) {
                // write keys and frequencies
                keyDataBlock = Raf.createDataBlock(elements[j].key + "", KEY_BLOCK);
                freqDataBlock = Raf.createDataBlock(elements[j].frequency + "", FREQUENCY_BLOCK);
                raf.write(keyDataBlock.getBytes());
                raf.write(freqDataBlock.getBytes());
            }
            long endOfNode = raf.getFilePointer();
            // writes node block
            if ((endOfNode - startOfNode) != NODE_BLOCK) {
                long bytesLeft = NODE_BLOCK - (endOfNode - startOfNode);
                String leftoverByteBlock = Raf.ensureFixedNode(blockString, bytesLeft);
                raf.write(leftoverByteBlock.getBytes());
            }

        } else {
            for (int j = 0; j < node.numElements; j++) {
                writeIntoFile(elements[j].next, ht - 1);
            }
        }
    }
    
    

    // reads cache file and returns arraylist of all commposite blocks
    public ArrayList<String> readBTreeCacheFile(String rafFile) throws IOException {

        ArrayList<String> allWords = new ArrayList<>();
        long pointer = 8;
        RandomAccessFile raf = new RandomAccessFile(rafFile, "r");
        while (raf.getFilePointer() < raf.length()) {
            String words = "";
            String s = "";
            raf.seek(pointer);
            FileChannel inChannel = raf.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(NODE_BLOCK);
            inChannel.read(buffer);
            buffer.flip();
            for (int i = 0; i < buffer.limit(); i++) {
                words += (char) buffer.get();
            }
            for (int i = 1; i < (MAX-1); i++) {
                s = words.substring(((COMPOSITE_BLOCK * i) - COMPOSITE_BLOCK), COMPOSITE_BLOCK * i);
                allWords.add(s);
            }
            
            
            pointer += NODE_BLOCK + 8;
        }
        return allWords;
    }
    

    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(String k1, String k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(String k1, String k2) {
        return k1.compareTo(k2) == 0;
    }
}

