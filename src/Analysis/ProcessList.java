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
	public void enqueue(int item, int priority){
		

		total++;
	}
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
