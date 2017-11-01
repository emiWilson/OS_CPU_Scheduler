package Analysis;

import java.io.*;
import java.util.*;

public class SchedAlgorithms {
	
	LinkedList<Node> list;
	static int totalTime = 3600000;
	static int [] avgWait = new int [totalTime / 600]; //there are 600 seconds in ten minutes
	static int [] avgTurnaround = new int [totalTime /600];
	
	public SchedAlgorithms(LinkedList<Node> input){
		list = input;

	}
	/**
	 * First come first serve algorithm without preemption
	 * Sort arrays using bubble sort (simple to implement and not worried about run time of algorithm) in terms of 
	 * arrival time from first to arrive to last to arrive
	 */
	public void FCFS(){
		//already in sorted order, so this shouldn't do anything, but just incase!
		for (int i = (list.size() -1); i >= 0; i--)
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
		}
		
		run();
	}
	/**
	 * SJF without preemption
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
		
	}
	
	public void run(){
		int time = 0;
		time = list.get(0).arrival;
		time += time + list.get(0).burst;
		
		for (int i = 1; i < list.size(); i++){
			Node m = list.get(i);
			if (m.arrival > time ){
				//if next process has already arrived
				time += m.burst;
			}else{
				//if next process hasn't arrived yet
				time += m.arrival + m.burst; 
			}
			
		}
	}
	/**
	 * Priority with preemption (for single thread CPU)
	 */
	public void Priority(){
		FCFS(); //check to make sure list is in order of arrival time
		
		PriorityQueue waitLine = new PriorityQueue();
		
		Node first = list.get(0);
		first.setIndex(0);
		waitLine.putQueue(first, first.priority);

		
		int currentTime = 0;
		
		int i = 1;
		int count = 0; //count of how many processes have been 
		int tenMinInt = 0;
		
		
		while (i < list.size()){
			Node curr = waitLine.peek(); // currently running process
			
			//determine if process is finished running, if so start next process
			if(curr.runTimeLeft == 0){
				//current process is done running
				Node done = waitLine.dequeue(); //remove running item from the priority queue
				done.completionTime(currentTime); //give it the time it finished running
				
				//start next process
				curr = waitLine.peek(); //get next element
				curr.startTime(currentTime); //set start time of process
			}
			
			
			Node item = list.get(i); //next process arriving to CPU
			item.setIndex(i);
			
			if(item.arrival <= currentTime){ 
				//put item at the end of queue
				waitLine.putQueue(item, item.priority);
			
				if (item.priority >= curr.priority){
					swap(curr.index, item.index);
					
				}
				
				i++; //get next element in next traversal of while loop
			}
			
			
			curr.addTimeRan(); //adjust node curr for running a timestep
			currentTime ++; //increment currentTime
			
			
		}
		
	}

	public void swap(int i, int j){
		Node iNode = list.get(i); // get node of index i
		Node jNode = list.get(j); // get node at index j
		
		//swap the nodes.
		list.set(i, jNode); 
		list.set(j, iNode);
		
	}
}

