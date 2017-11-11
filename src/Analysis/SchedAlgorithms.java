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
	static Analysis analyze;
	
	public SchedAlgorithms(LinkedList<Node> input){
		list = input;

	}
	/**
	 * First come first serve algorithm without preemption
	 * Sort arrays using bubble sort (simple to implement and not worried about run time of algorithm) in terms of 
	 * arrival time from first to arrive to last to arrive
	 */
	public void FCFS(){
		run();
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
		
	}
	/**
	 * This algorithm is used to run the FCFS and SJF CPU scheduling algorithms.
	 * Each process will run to completion before the next process is called.
	 */
	public void run(){
		int currentTime = 0;
		
		for (int i = 0; i < list.size(); i++){
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
		
		int currentTime = 0;
		
		int i = 0; //index of arriving process
		int iRun = i; //index of process running
		
		Node curr = list.get(i); //get first process
		currentTime += curr.arrival;
		curr.startTime(currentTime); //give the first process a running time
		
		while (i < list.size()){
			
			curr = list.get(iRun); //node currently running
			System.out.println("At beg. " + curr.process +  " and time " + currentTime);
			//determine if process is finished running, if so start next process in queue
			System.out.println(curr.process + " has time left " + curr.runTimeLeft);
			
			curr = nextProcess(curr, currentTime); //get next process if one currently running is done
		
			
			Node item = list.get(i); //next process arriving to CPU
			item.setIndex(i);
			
			if(item.arrival < currentTime){ //see if item is arriving before the current time
				//put item in queue
				//waitLine.putQueue(item, item.priority);
				System.out.println(item.process + "is in the loop");
				queue.enqueue(i, item.priority);
				
				i++; //get next element in next traversal of while loop
			}
			
			if(curr!=null){curr.addTimeRan();} //adjust node curr for running a timestep
			
			currentTime ++; //increment currentTime
			
			iRun = queue.getIndex();
			
			System.out.println(curr.process + " has time left " + curr.runTimeLeft);
			System.out.println(queue.toString());
			System.out.println("At end" + curr.process);
			
		}

		
		while(queue.size()!= 0){
			iRun = queue.dequeue();
			curr = list.get(iRun);
			
			curr.startTime(currentTime); //set start time for process with index iRun
			
			currentTime += curr.runTimeLeft;
			
			curr.completionTime(currentTime); //set end time for process with index iRun
			
			System.out.println(curr.process + " has time left " + curr.runTimeLeft);
			
		
		}
		
		
		
	
	}
	/**
	 * If currently running process is finished running then start new process, else continue running current process
	 * @param current, is the currently running process
	 * @param currentTime, is the currentTime the simualation is at
	 * @return returns current if process is not finished running, or is null and will return next process to run if 
	 * current process is completed running
	 */
	public Node nextProcess(Node current, int currentTime){
		Node next = current;
		
		if (current != null){
			if (current.runTimeLeft == 0){
			System.out.println("Next process please *********" + current.runTimeLeft);
			//current process is done running
			current.completionTime(currentTime); //give it the time it finished runnining
			//waitLine.dequeue(); //remove running item from the priority queue
			System.out.println("My queue is " + queue.toString());
			queue.dequeue();
			
			System.out.println(queue.toString());
			//start next process if process waiting
			//current = waitLine.peek(); //get next element
			next = list.get(queue.getIndex());
			
			if(next.process == current.process){
				System.out.println("Same process!!!!!!!!!");
			}
			
			next.startTime(currentTime); //set start time of process
			System.out.println("******* now running process " + next.process + " has time left " + next.runTimeLeft);
			}
		 
		}
		
		return next;
		
	}
	/**
	 * Round robin if not time quantum is specified, default time quantum is 1.
	 */
	public void RR(){
		int defaultTimeQuantum = 1;
		RR(defaultTimeQuantum);
	}
	
	/**
	 * Round robin CPU scheduling algorithm without preemption.
	 * @param timeQuantum the time before switching to next process
	 */
	public void RR(int timeQuantum){
		//the linked list "list" is already sorted in terms of arrival time
		

		Node curr;
		
		
		for (int i = 0; i < list.size(); i++){
			queue.enqueue(i);
		}
		
		int currentTime = timeQuantum; //set the current time to check if first process needs to be switched

		while(queue.size()!=0){ //while there are processes in queue
			int elem = queue.dequeue(); //index of first process
			curr = list.get(elem); //get first process in list
			curr.addTimeRan(timeQuantum); //increment run time of currently running process by the timeQuantum
			
			if(curr.runTimeLeft > 0) //if process is not finsihed running yet.
				queue.enqueue(elem); //add item back into queue
			else {
				curr.completionTime(currentTime); //if process is done running, stamp with completion time
			}
			currentTime  += timeQuantum; //increment counter by the time Quantum
			
		}

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
		
			System.out.println("\n\nproc     arr      prior      burst        end     turnaround   wait    start ");
		for(int i = 0; i < list.size(); i++){
			Node node = list.get(i);
			System.out.println(node.process + "       " + node.arrival + "        " + node.priority + "       " + node.burst + "      " + node.timeCompleted + "     " + node.turnaround + "   " + node.wait + "   " + node.timeStarted );
		}
	}
	
	public LinkedList<Node> returnList(){
		return list;
	}
	
	public void Analyze(){
		analyze = new Analysis(list);
	}
	
	public int [] TurnData(){
		return analyze.getTurn();
	}
	
	public int [] waitData(){
		return analyze.getWait();
	}
	
	public void analysis(){
		Analyze();
		int [] turn = TurnData();
		int [] wait = waitData();
		
		System.out.println("Average turnaround time");
		for (int i = 0; i < turn.length; i++){
			System.out.print(turn[i] + " ");
		}
		
		System.out.println("\nAverage wait time");
		for (int i = 0; i < wait.length; i ++){
			System.out.print(wait[i] + " ");
		}
		
	}
}



