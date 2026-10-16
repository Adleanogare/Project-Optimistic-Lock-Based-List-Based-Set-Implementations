package linkedlists.lockbased;

import contention.abstractions.CompositionalSortedSet;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
import contention.abstractions.AbstractCompositionalIntSet;

public class HandOverHandListSet extends AbstractCompositionalIntSet {

    // We need a head and a tail
    private Node head;
    private Node tail;

    // The constructor initialises head, tail and the link
    public HandOverHandListSet() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
    }

    @Override 
    public boolean addInt(int item){
        head.lock();
        Node pred = head; // on lock au début, puis on lock son next
        try {
            Node curr = pred.next; // current
            curr.lock();
            try {
                // tant qu'on a pas trouvé l'emplacement, on parcourt la liste en unlock le prec et lock le next
                 while (curr.key < item) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                 }
                 if (curr.key == item) return false; // ignore les doublons
                 Node add = new Node(item);
                 add.next = curr;
                 pred.next = add;

                 return true;
            } finally { // à la fin on unlock les deux noeuds et c'est fini
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override 
    public boolean removeInt(int item){ //idem mais au lieu d'ajouter en pred et curr, on supprime curr
    //et on renvoie false si l'entier n'est pas dans la lsite
    //pour supprimer, on met curr dans le garbage collector en le déréférençant
        head.lock();
        Node pred = head; // on lock au début, puis on lock son next
        try {
            Node curr = pred.next; // current
            curr.lock();
            try {
                // tant qu'on a pas trouvé l'emplacement, on parcourt la liste en unlock le prec et lock le next
                 while (curr.key < item) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                 }
                 
                 if (curr.key == item) {
                    pred.next = curr.next;

                    return true;
                 }
                 return false;

            } finally { // à la fin on unlock les deux noeuds et c'est fini
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override 
    public boolean containsInt(int item){ //idem 
        head.lock();
        Node pred = head; // on lock au début, puis on lock son next
        try {
            Node curr = pred.next; // current
            curr.lock();
            try {
                // tant qu'on a pas trouvé l'emplacement, on parcourt la liste en unlock le prec et lock le next
                 while (curr.key < item) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                 }
                 
                 return curr.key == item;

            } finally { // à la fin on unlock les deux noeuds et c'est fini
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }


    // A node is a key, a successor, and is lockable.
    private class Node {
        
        public final int key;
        public Node next;
        private final Lock lock = new ReentrantLock(true);

        Node(int item) {
            key = item;
            next = null;
        }
 
        void lock()   { lock.lock(); }
        void unlock() { lock.unlock(); }
    }

    @Override 
    public void clear(){
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
    }

    /* Je ne mets pas size atomic comme il ne l'est pas dans l'autre */
    @Override 
    public int size(){
        int count = 0;
        Node curr = head.next;
        while (curr.key != Integer.MAX_VALUE) {
            curr = curr.next;
            count++;
        }
        return count;
    }
}
