package Analysis;

import java.io.*;
import java.util.*;

import Analysis.ProcessList.Item;
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
//	static PriorityQueue waitLine = new PriorityQueue();
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
		
		int CPUtime = 0;
		
		int i = 0; //index of arriving process
		
		Node curr = list.get(i); //get first process
		CPUtime += curr.arrival; //jump CPU time to time first process arrives
		curr.startTime(CPUtime); //give the first process a running time
		curr.setIndex(i);
		queue.enqueue(i, curr.priority);
		i++;
		
		//queue.enqueue(0, curr.priority); //push first element into the queue ** Why does this make such a big difference
		
		while (i < list.size()){ //while there are still processes in list
			
			//next process arriving to CPU
			Node item = list.get(i);
			item.setIndex(i); //so can re-access item in list
			//current process is done running
			if (curr.runTimeLeft == 0){
				curr.completionTime(CPUtime); //give it the time it finished running
				int deq = queue.dequeue(); //remove element
				
				if(queue.size() != 0){
					curr = nextProcess(curr, CPUtime);
				}else{
					curr = null;
				}
			}
	
			if(item.arrival == CPUtime){ //see if item is arriving after or at the current time
				//put item in queue
				//System.out.println("item added but too early " + CPUtime + " process item " + item.index + " and current is " + curr.index);

				queue.enqueue(i, item.priority);
				System.out.println("Queue size " + queue.size());
				i++; //get next element in next traversal of while loop
				
				if(curr == null){
					curr = list.get(queue.getIndex());
					curr.startTime(CPUtime);
				}
			}	
			//get whatever process is at the end of the priority queue, set as current process
			if (queue.size() != 0){
				curr = nextProcess(curr, CPUtime); 

			}
			
			if(curr != null){curr.addTimeRan();} //adjust node curr for running a timestep

			
			CPUtime ++; //increment currentTime
			
			
		}
		System.out.println("In Queue" + queue.toString());
		//finish any processes still in queue
		while(queue.size() != 0){
			i = queue.dequeue(); //get index of next process that needs to run
			curr = list.get(i); //get the node of the next process that needs to run
			
			if(curr.timeStarted != -1){ //if process has not yet been started 
				curr.startTime(CPUtime); //set start time for process i
			}
			
			CPUtime += curr.runTimeLeft; //increment the CPU by the time the current process needs to run
			
			curr.completionTime(CPUtime); //set end time for process with index iRun
			
			System.out.println(curr.process + " has time left " + curr.runTimeLeft);
			
		
		}
	
	//curr = list.get(2);	
	//System.out.println(curr.process +"    "+ curr.arrival +"    "+ curr.timeStarted);
	//System.out.println("Queue is: "+ queue.toString());
	
	}
	/**
	 * If currently running process is finished running then start new process, else continue running current process
	 * @param current, is the currently running process
	 * @param currentTime, is the currentTime the simulation is at
	 * @return returns current if process is not finished running, or is null and will return next process to run if 
	 * current process is completed running
	 */
	public Node nextProcess(Node current, int currentTime){
		
		Node next = current;
		

			
			
			if(queue.size() != 0){
				int index = queue.getIndex();//get index of element at head
				next = list.get(index); //find node with index
				//if the node has not already been started
				if(next.timeStarted == -1){
					System.out.println("Give start time");
					next.startTime(currentTime); //set start time of process if haven't been started previously
				}
			}else{ //if size of queue is 0
				next = null;
			}
			
		//System.out.println("******* now running process " + next.process + " has time left " + next.runTimeLeft);
			
		
		
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
		
			System.out.println("\n\nproc     arr      prior      burst          start        end         turnaround   "
					+ "     wait");
		for(int i = 0; i < list.size(); i++){
			Node node = list.get(i);
			System.out.println(node.process + "       " + node.arrival + "        " + node.priority + "           " 
			+ node.burst + "               " +node.timeStarted+"           "+ node.timeCompleted + "             " 
					+ node.turnaround + "            " + node.wait );
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



