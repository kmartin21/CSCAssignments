/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365hw3frame3;

/**
 *
 * @author keithmartin
 */
/**
 * Class MinHeap *
 */
class MinHeap<T> {

    int MAX_SIZE;
    int lastPosition; //last position which is empty
    QueueEntry[] heap;
    //left child:2n+1, Right child: 2n+2 

    public MinHeap(int size) {
        this.MAX_SIZE = size;
        heap = new QueueEntry[1001];
        lastPosition = 0;
    }

    public void insert(QueueEntry o) {
        QueueEntry data = o;
        QueueEntry temp;
        int vertexPos = lastPosition;
        lastPosition++;
        heap[vertexPos] = data;
        data.setPos(vertexPos);
        int parentPos = getParentPosition(vertexPos);
        data.setParentPos(parentPos);
        
        if (parentPos == -1) { // No more checks needed for first insertion, so return
            return;
        }
        siftUp(parentPos, vertexPos);

    }

    public QueueEntry deleteMin() {
        QueueEntry temp1 = heap[0];
        QueueEntry temp;

        if (lastPosition == 0) {
            return null;
        } else if (lastPosition == 1) {
            heap[0] = null;
            lastPosition--;
            return temp1;
        }

        int i = lastPosition - 1;
        heap[i].setPos(0);
        heap[i].setParentPos(-1);
        heap[0] = heap[i];
        heap[i] = null;
        i = 0;

// If there exists a smaller child for vertexPos, then get it.  
        int next = getSmallerChild(i);
        siftDown(next, i);

        lastPosition--;
        return temp1;

    }
    
    public boolean decreasePriority(String s, double priority) {
        QueueEntry entry = get(s);
        if (entry != null) {
            entry.cost = priority;
            siftUp(entry.parentPos, entry.pos);
            return true;
        } else {
            return false;
        }
     }
    
    public boolean isEmpty() {
        return lastPosition < 1;
    }
    
    public boolean contains(String entryName) {
        return get(entryName) != null;
    }
    
    public QueueEntry get(String entryName) {
        for (int i = 0; i < heap.length; i++) {
            if(heap[i].entryName.equals(entryName)) return heap[i];
        }
        return null;
    }

    public Object getMin() {
        return heap[0];
    }

    public int getLeftChildPosition(int i) {
        return (2 * i) + 1;
    }

    public int getRightChildPosition(int i) {
        return (2 * i) + 2;
    }

    public int getParentPosition(int i) {
        if (i == 0) {
            return -1;
        } else if (i % 2 != 0) {
            return (i / 2);
        } else {
            return (i / 2) - 1;
        }

    }

    public int getSmallerChild(int i) {
//Returns the index of the smaller child, if any of the children are smaller  
        int left = getLeftChildPosition(i);
        int right = getRightChildPosition(i);

        if (heap[left] == null && heap[right] == null) {
            return -1;
        } else if (heap[left] != null && heap[right] == null) {
            if (heap[left].cost < heap[i].cost) {
                return left;
            } else {
                return -1;
            }
        } else if (heap[left] != null && heap[right] != null) {
            if (heap[left].cost > heap[right].cost && heap[left].cost < heap[i].cost) {
                return left;
            } else if (heap[left].cost > heap[right].cost && heap[right].cost < heap[i].cost) {
                return right;
            } else {
                return -1;
            }
        }

        return -1;

    }

    
    public void print() {
        for (int i = 0; i < heap.length; i++) {
            if (heap[i] != null) {
                System.out.println("NAME: " + heap[i].entryName + " " + heap[i].cost);
            }
        }
    }

    private boolean siftUp(int parentPos, int i) {
        QueueEntry temp;
        //if parent is larger than vertexPos then sift up
        while (heap[parentPos].cost > heap[i].cost) {
            //hold vertex at i in temp variable to be moved
            temp = heap[i];
            //move parent down to where last vertex position was
            temp.setParentPos(i);
            heap[i] = heap[parentPos];
            //move vertex to where parent was
            temp.setPos(parentPos);
            heap[parentPos] = temp;
            //set i = to what parent originally was
            i = parentPos;
            //get new parent and keep sifting upwards
            parentPos = getParentPosition(i);
            
            if (parentPos == -1) {
                //To avoid array out of bound exception because you're at the top
                return true;
            }
        }
        return false;
    }

    private void siftDown(int next, int i) {
        QueueEntry temp;
        //Start sifiting down
        while (next != -1) {
        // Go down the heap until you have no smaller child
            temp = heap[i];
            int childPos = heap[next].pos;
            int childParentPos = heap[next].parentPos;
            
            heap[next].setParentPos(temp.parentPos);
            heap[next].setPos(i);
            heap[i] = heap[next];
            
            temp.setPos(childPos);
            temp.setParentPos(childParentPos);
            heap[next] = temp;
            i = next;
            next = getSmallerChild(i);
        }
    }
    
    
}
 
