package Analysis;

//original code from http://eddmann.com/posts/implementing-a-queue-in-java-using-arrays-and-linked-lists/
import java.util.*;

public class ProcessList implements Queue{
	
	private int total = 0;
	private int LOWEST_PRIORITY = 1000000;

    private Item head, tail;
	
	public class Item{
		public int index; //index within linked list
		public Item next;
		public int priority;
		
		
		
	}
	
	public ProcessList(){ }
	
	public boolean isEmpty(){
		return total == 0;
	}
	
	public Item peek(){
		return head;
	}
	public int getIndex(){
		if(head == null){
			System.out.println("Queue is empty, be careful!");
			return -1;
		}
		return head.index;
	}
	
	public int dequeue(){
		  if (total == 0){ return -1;}
		  
		  int ele = head.index;
	      head = head.next;
	      --total;
	        
	      return ele;
	}

	
	public int size(){
		return total;
	}
	
	public Item next(Item item){
		return item.next;
	}
	public boolean hasNext(Item item){
		return (item.next != null);
	}
	/**
	 * Adds item to queue in normal (non - priority order), will be used for round robin algorithm
	 * @param item
	 */
	public void enqueue(int item){
		
		 Item current = tail;
	        tail = new Item();
	        tail.index = item;

	        if (total++ == 0) head = tail;
	        else current.next = tail;
		
	        
	       tail.priority = LOWEST_PRIORITY;
	      
	   
	}
	/*
	 * Add item to queue, but making queue a priority queue
	 * Lowest priority at tail, highest priority at head
	 * If process has same priority as process already in queue, then first come first serve.
	 */
	//original code from stack overflow: https://stackoverflow.com/questions/40635584/insert-method-for-priority-queue-using-linked-lists
	public void enqueue(int data, int priority) {
	    //make new item with data
		Item item = new Item();
	    item.priority = priority;
	    item.index = data;

	  
	    if (isEmpty()) {
	        head = item;
	        item.next =  null;
	    } else {
	        Item next = head;
	        Item prev = next;

	        do {
	            if (priority < next.priority) { //if priority is less than 
	                // break and insert
	                break;
	            }
	            prev = next;
	            next = next.next;
	        } while (next != null);

	        item.next = next;
	        if (item.priority < head.priority) {
	            head = item;
	        } else prev.next = item;
	    }
	    total ++;
	}

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Item tmp = head;
        while (tmp != null) {
            sb.append(tmp.index).append(", ");
            tmp = tmp.next;
        }
        return sb.toString();
    }
}
