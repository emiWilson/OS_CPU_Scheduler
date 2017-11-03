package Analysis;

import java.io.*;
import java.util.*;
/**
 * Need to get priority queue to run last of the processes
 * Need to get RR to stop acting funny. 
 * Maybe using PCB?
 * @author emily
 *
 */
public class SchedAlgorithms {
	
	LinkedList<Node> list;
	static int totalTime = 3600000;
	static int [] avgWait = new int [totalTime / 600]; //there are 600 seconds in ten minutes
	static int [] avgTurnaround = new int [totalTime /600];
	static PriorityQueue waitLine = new PriorityQueue();
	static ProcessList queue = new ProcessList();
	
	public SchedAlgorithms(LinkedList<Node> input){
		list = input;

	}
	/**
	 * First come first serve algorithm without preemption
	 * Sort arrays using bubble sort (simple to implement and not worried about run time of algorithm) in terms of 
	 * arrival time from first to arrive to last to arrive
	 */
	public void FCFS(){
		//already in sorted order of arrival, so this shouldn't do anything, but just incase!
		/*for (int i = (list.size() -1); i >= 0; i--)
		{
			for (int j = 1; j <= i; j++)
			{
				Node node1 = list.get(j - 1);
				Node node2 = list.get(j);
				int arr1 = node1.arrival;
				int arr2 = node2.arrival;
				//System.out.println(arr1 + ":" +  arr2);
				if (arr1 > arr2)
					System.out.println("swap me!");
					swap(j - 1, j);
				
			}
		}*/
		
		run();
		print();
	}
	/**
	 * Shortest Job first without preemption
	 * This scheduling algorithm runs the processes that take the shortest time first
	 * To simulate this algorithm I will sort the linked list "list" by job length then call the run() method will 
	 * calculate the run time and wait time for 10 minute intervals of this algorithm.
	 */
	public void SJF(){
		
		for (int i = (list.size() - 1); i >= 0; i--)
		{
			for (int j = 1; j <= i; j++)
			{
				Node node1 = list.get(j-1);
				Node node2 = list.get(j);
				
				int burst1 = node1.burst;
				int burst2 = node2.burst;
				
				if(burst1 > burst2)
					swap(j-1, j);
						
			}
			
		}
		run();
		print();
		
	}
	/**
	 * This algorithm is used to run the FCFS and SJF CPU scheduling algorithms.
	 * Each process will run to completion before the next process is called.
	 */
	public void run(){
		int currentTime = 0;
		
		for (int i = 1; i < list.size(); i++){
			Node m = list.get(i);
			m.startTime(currentTime);
			if (m.arrival >= currentTime ){
				//if next process has already arrived
				currentTime += m.burst;
			}else{
				//if next process hasn't arrived yet
				currentTime += m.arrival + m.burst; 
			}
			m.completionTime(currentTime);
			
		}
	}
	/**
	 * Priority with preemption (for single thread CPU)
	 */
	public void Priority(){
		//list is already sorted in order of arrival time
		
		Node first = list.get(0);
		first.setIndex(0);
		//waitLine.putQueue(first, first.priority);
		//queue.enqueue(0, first.priority);
		
		int currentTime = 0;
		
		int i = 1;
		int count = 0; //count of how many processes have been 
		int tenMinInt = 0;
		int index = i;
		Node curr = list.get(i);
		
		
		queue.enqueue(1, 3);
		queue.enqueue(2, 4);
		queue.enqueue(3, 5);
		queue.enqueue(4, 6);
		queue.enqueue(5, 7);
		queue.enqueue(6, 0);
		
		

		/*while (i < list.size()){
			//curr = waitLine.peek(); // currently running process
			index = queue.peek();
			curr = list.get(index);
			System.out.println("It is" + queue.toString());
			//determine if process is finished running, if so start next process in queue
			curr = nextProcess(curr, currentTime);
		
			
			Node item = list.get(i); //next process arriving to CPU
			item.setIndex(i);
			System.out.println(queue.toString());
			if(item.arrival <= currentTime){ 
				//put item in queue
				//waitLine.putQueue(item, item.priority);
				queue.enqueue(i, item.priority);
				
				i++; //get next element in next traversal of while loop
			}
			System.out.println(queue.toString());
			if(curr!=null){curr.addTimeRan();} //adjust node curr for running a timestep
			currentTime ++; //increment currentTime
			
			
		}*/
		System.out.println(queue.toString());
		System.out.println(queue.size());
		System.out.println(queue.dequeue());
		
		/*while((index = queue.dequeue()) != -1){
			curr = list.get(index);
			currentTime += curr.runTimeLeft;
			curr.completionTime(currentTime);
			
			System.out.println(curr.timeCompleted);
		
		}*/
		
		print();	
		
		System.out.println("huh");
	}
	/**
	 * If currently running process is finished running then start new process, else continue running current process
	 * @param current, is the currently running process
	 * @param currentTime, is the currentTime the simualation is at
	 * @return returns current if process is not finished running, or is null and will return next process to run if 
	 * current process is completed running
	 */
	public Node nextProcess(Node current, int currentTime){
		if (current != null){
		 if(current.runTimeLeft == 0){
			 System.out.println(current.runTimeLeft);
			//System.out.println("Next process please");
			//current process is done running
			current.completionTime(currentTime); //give it the time it finished running
			//waitLine.dequeue(); //remove running item from the priority queue
			queue.dequeue();
			
			//start next process if process waiting
			//current = waitLine.peek(); //get next element
			current = list.get( queue.peek() );
			
			
			if (current != null){current.startTime(currentTime);} //set start time of process
		
		 }
		}
		
		return current;
		
		
	}
	
	public void runLastOfQueue(int currentTime){
		int itemsLeft = waitLine.getNoItems();
		System.out.println("Items left " + itemsLeft);
		
		Node curr = waitLine.peek();
		
		while(curr != null){

			int timeLeft = curr.runTimeLeft; //time left for current process to finish running
			currentTime += timeLeft; //jump to time that running process finishes runnint (note: don't have to worry about
			//preemption, all processes are in order of priority in queue at this point)
			curr.completionTime(currentTime); //set completion time of process
			waitLine.dequeue(); //get next process of next priority
			curr = waitLine.peek();
			System.out.println(curr.process + " Is the process ID");
			System.out.println(waitLine.getNoItems());
			itemsLeft --;
		}
		/*while (itemsLeft != 0){
			curr = nextProcess(curr, currentTime);
			currentTime++;
			itemsLeft = waitLine.getNoItems();
			curr = waitLine.peek();
			if(curr == null){itemsLeft=0;}
			else{
			int timeLeft = curr.runTimeLeft;
			currentTime += timeLeft;
			curr.completionTime(currentTime);
			waitLine.dequeue();
			
			itemsLeft = waitLine.getNoItems();
			System.out.print("itemsLeft " + itemsLeft);
			}
		}*/
	}
	
	/**
	 * Round Robin without preemption
	 */
	public void RR(){
		//the linked list "list" is already sorted in terms of arrival time
		int timeSlice = 1; //time before switching to next process
		int currentTime = 0;
		LinkedList<Node> RR = list;

		while(RR.size() != 0){
			
			int i = 0;
			
			while(i < RR.size()){
				Node node = RR.get(i);
				node.addTimeRan();
				
				if(node.runTimeLeft == 0 )
					node.completionTime(currentTime); //set the time process is completed to the the current time
					RR.remove(i); //remove node from Round Robin linked list RR
				
				currentTime++;
			}
	//maybe need ot make sure main list is being updated. Maybe just move removed elements to another list, or have a marker that says if the task has been completed or not.
			
		}
		
		System.out.println("Round Robin completed!");
		print();
		System.out.println("Done printing");
	}
	/*
	 * Finds next process that has not been completed
	 */
	public Node next(int i){
		if(list.get(i + 1) == null){ return null;}
		
		Node node = list.get(i + 1); //get node at index i
		
		if (node.runTimeLeft == 0){ next(i + 1);} //recursively call next node if process is already finished running
		
		
		return node; //return node that still has some running time left (runTimeLeft != null)
		
	}
	
	
	/*
	 * Swaps the index of two nodes in the linked list "list"
	 */
	public void swap(int i, int j){
		Node iNode = list.get(i); // get node of index i
		Node jNode = list.get(j); // get node at index j
		
		//swap the nodes.
		list.set(i, jNode); 
		list.set(j, iNode);
		
	}
	
	public void print(){
		for(int i = 0; i < list.size(); i++){
			Node node = list.get(i);
			System.out.println(node.process + "   " + node.arrival + "    " + node.priority + "    " + node.burst + "    " + node.timeCompleted);
		}
	}
}



