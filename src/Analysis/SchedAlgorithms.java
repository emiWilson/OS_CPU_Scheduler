package Analysis;

import java.io.*;
import java.util.*;

import Analysis.ProcessList.Item;

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
		int currentTime = 0; //current time the CPU is at
		Node m = list.get(0); //get first arriving process
		currentTime = m.arrival; //jump current time to arrival of the first process
		
		for (int i = 0; i < list.size(); i++){ //for all items in list
			m = list.get(i);
			m.startTime(currentTime);
			if (m.arrival <= currentTime ){
				//if next process has already arrived
				currentTime += m.burst;
			}else{
				//if next process hasn't arrived yet
				currentTime = m.arrival + m.burst; 
			}
			m.completionTime(currentTime);
			
		}
	}
	/**
	 * Shortest Job first without preemption
	 * This scheduling algorithm runs the processes that take the shortest time first
	 * To simulate this algorithm I will sort the linked list "list" by job length then call the run() method will 
	 * calculate the run time and wait time for 10 minute intervals of this algorithm.
	 */
	public void SJF(){
		int CPUtime = 0; //timestep the CPU is at
		
		Node item = list.get(0);
		queue.enqueue(0, item.burst); //add item to priority queue, where priority is the burst time
		CPUtime += item.arrival; //jump start time to the arrival of the first process
		
		int currIndex; //index of currently running process
		Node curr = null; //node of currently running process
		int i = 1; //index in list of next arriving processes
		
		while(i < list.size()){
			if(queue.size() != 0){
				currIndex = queue.dequeue(); //get next shortest process to run until completion
				curr = list.get(currIndex);
				curr.startTime(CPUtime);
				//run curr until completion (no -preemption)
				CPUtime += curr.runTimeLeft; //increment CPU time by the amount of time the currently running process takes to run
				curr.completionTime(CPUtime); //give curr it's completion time
			}else{
				CPUtime ++;
			}
			
			item = list.get(i);
			//now add items to priority queue
			while((item.arrival <= CPUtime) && (i < list.size())){
				queue.enqueue(i, item.burst);
				item = list.get(i);
				i++;
			}
			
		}
		
		finishQueue(CPUtime, curr);
		
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
	

				queue.enqueue(i, item.priority);
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
		//finish any processes still in queue
		finishQueue(CPUtime, curr);

	
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
	 * @param timeQuantum the time steps before switching to next process
	 */
	public void RR(int timeQuantum){
		//the linked list "list" is already sorted in terms of arrival time
		

		Node curr = null;
		
		int CPUtime = timeQuantum; //set the current time to check if first process needs to be switched
		
		int i = 0; //index of next element in list
		
		while(i < list.size()){ //while there are processes in the list
			
			
			Node item = list.get(i); //get item with index i
			
			if (item.arrival == CPUtime){
				//get next element in list
				queue.enqueue(i);
				i++;
			}

			
			int elem = queue.dequeue(); //index of first process
			
			//if there is a process in queue
			if(elem != -1){
				curr = list.get(elem); //get first process in list
			
				//if the node has not already been started
				if(curr.timeStarted == -1){
					curr.startTime(CPUtime); //set start time of process if haven't been started previously
				}
			
				curr.addTimeRan(timeQuantum); //increment run time of currently running process by the timeQuantum
			
				if(curr.runTimeLeft > 0) //if process is not finsihed running yet.
					queue.enqueue(elem); //add item back into queue
				else {
				curr.completionTime(CPUtime); //if process is done running, stamp with completion time
				}
			}
			CPUtime  += timeQuantum; //increment counter by the time Quantum
			
		}
		//finish any processes still in queue
		finishQueue(CPUtime, curr);

	}
	
	public void finishQueue(int CPUtime, Node curr){
		int i;		
		//finish any processes still in queue
				while(queue.size() != 0){
					i = queue.dequeue(); //get index of next process that needs to run
					curr = list.get(i); //get the node of the next process that needs to run
					
					if(curr.timeStarted == -1){ //if process has not yet been started 
						curr.startTime(CPUtime); //set start time for process i
					}
					
					CPUtime += curr.runTimeLeft; //increment the CPU by the time the current process needs to run
					
					curr.completionTime(CPUtime); //set end time for process with index iRun
				
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
	/**
	 * Prints the data stored in the nodes of the list of processes
	 */
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
	
	/**
	 * Does process analysis which finds the turnaround time and wait time, my method of doing this is a little
	 * awkward, but I think that it is straightforward enough I didn't feel like changing it.
	 */
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



