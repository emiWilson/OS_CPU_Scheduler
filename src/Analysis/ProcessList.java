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
	
	public void enqueue(int item){
	
		 Item current = tail;
	        tail = new Item();
	        tail.index = item;

	        if (total++ == 0) head = tail;
	        else current.next = tail;
		
	        
	       tail.priority = LOWEST_PRIORITY;
	      
	   
	}
	
	public Item peek(){
		return head;
	}
	
	public int dequeue(){
		  if (total == 0){ return -1;}
		  	int ele = head.index;
	        head = head.next;
	        if (--total == 0) tail = null;
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
	/*
	 * Add item to queue, but making queue a priority queue
	 * Lowest priority at tail, highest priority at head
	 * If process has same priority as process already in queue, then first come first serve.
	 */
	public void enqueue(int data, int priority) {
	    Item item = new Item();
	    item.priority = priority;
	    item.index = data;

	    if (head == null) {
	        head = item;
	        item.next =  null;
	    } else {
	        Item next = head;
	        Item prev = next;

	        do {
	            if (priority > next.priority) {
	                // break and insert
	                break;
	            }
	            prev = next;
	            next = next.next;
	        } while (next != null);

	        item.next = next;
	        if (item.priority > head.priority) {
	            head = item;
	        } else prev.next = item;
	    }
	}/*{
		//make new item with input data
		Item node = new Item();
		node.index = index;
		node.priority = priority;
		
		Item current = head; //current position in queue, start at tail
		Item nextItem = head;
	
        //if queue is empty head = tail
        if (total++ == 0){ 
        	
        	tail = head; 
        
        }else {
        	nextItem = current.next;
        	do{
        		if (priority > nextItem.priority)
        			break;
        		
        		current = nextItem;
        		nextItem = current.next;
        		
        	}while(nextItem != null);
        	
        	node.next = current;//make sure this is inserting properly, not going to be a element short
        	
        }
        
        if (priority > head.priority){
        	head = node;
        }else{
        	nextItem.next = node;
        }
	     

		total++;
		
	}*/

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(head);  
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
