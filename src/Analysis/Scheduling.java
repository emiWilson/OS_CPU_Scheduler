package Analysis;
import SampleFiles.*;
/**
 * CPU Scheduling Algorithms.
 * 
 * @author emily
 *
 */
public class Scheduling {
	static int [] process;
	static int [] arrival;
	static int [] priority;
	static int [] burst;
	
	public Scheduling(int [] inputProcess, int [] inputArrival, int [] inputPriority, int [] inputBurst){
		process = inputProcess;
		arrival = inputArrival;
		priority = inputPriority;
		burst = inputBurst;
	}
	/**
	 * First come first serve algorithm without preemption
	 * Sort arrays using bubble sort (simple to implement and not worried about run time of algorithm) in terms of 
	 * arrival time from first to arrive to last to arrive
	 */
	public void FCFS(){
		
		for (int i = (arrival.length -1); i >= 0; i--)
		{
			for (int j = 1; j <= i; j++)
			{
				if (arrival[j - 1] > arrival[j])
					swap(j -1, j);
				
			}
		}
	}
	/**
	 * SJF without preemption
	 */
	public void SJF(){
		
		for (int i = (arrival.length - 1); i >= 0; i--)
		{
			for (int j = 1; j <= i; j++)
			{
				if (burst[j - 1] > burst[j])
					swap(j -1, j);
					
				
			}
			
		}
		
	}
	/**
	 * Priority with preemption (for single thread CPU)
	 */
	public void Priority(){
		FCFS();
		
		PriorityQueue waitLine = new PriorityQueue();
		waitLine.putQueue(0, priority[0]);
	}
		/*FCFS(); //run FCFS() so jobs are in order of arrival to CPU
		
		int [] wait = new int [arrival.length]; //note wait[0] is the currently running process, holds indexes of processes
		int[] time = new int [arrival.length];
		
		int currentTime = 0;
		
		//first process
		wait[0] = 0;
		int next = 1; //first empty element in wait.
		currentTime = arrival[0]; //set currentTime to the arrival of the first process
		time[0] = burst[0]; 
		
		int i = 0;
		while(i < arrival.length){
			
			int running = priority[wait[0]];
			
			if(arrival[i] >= currentTime){	
				i++;
				
				if (priority[i] < running)
				{
					wait[next] = i;
					wait = swapElements(0, next, wait);
					next++;
				}else{
					
				}
			}
		
			//start next process
			if (time[0] == 0){
				wait = shift(next, wait);
				next--; 
			}
			
			
			time[0] --; //decrement the time the current process has been running
			currentTime++; //increment the time the CPU has been used
		}
		
		
		
	}*/
	
	public int [] shift(int length, int [] wait){
		int [] temp = wait;
		for(int i = 0; i < length; i++){
			wait[i] = temp[i+1];
 		}
		return wait;
	}

	/**
	 * RR without preemption
	 */
	public void RR(){
		
	}
	
	/**
	 * This is a helper method that swaps elements in array
	 */
	public void swap(int i, int j){
		process = swapElements(i, j, process);
		arrival = swapElements(i,j, arrival);
		priority = swapElements(i, j, priority);
		burst = swapElements(i, j, burst);
	}
	
	public int [] swapElements(int n, int m, int [] arr){
		int temp = arr[n];
		arr[n] = arr[m];
		arr[m] = temp;
		return arr;
		
	}

}
