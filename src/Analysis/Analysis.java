package Analysis;

import java.util.LinkedList;

public class Analysis {
	
	LinkedList<Node> list;
	
	static int [] turn;
	static int [] wait;
	/**
	 * Constructor
	 * @param input linked list that has all the processes
	 */
	public Analysis(LinkedList<Node> input){
		list = input;
		
		//calculate the arrays turn and wait
		calculateTurnaround();
		calculateWait();
		avgCalc();
	}
	/*
	 * calculate turnaround time  for each process
	 */
	public void calculateTurnaround(){
		for (int i = 0; i < list.size(); i++){
			list.get(i).turnaroundTime();
		}
	}
	/*
	 * calculate the wait time for each process
	 */
	public void calculateWait(){
		for (int i = 0; i < list.size(); i++){
			list.get(i).waitTime();
		}
	}
	/**
	 * calculate the average turnaround and wait time for every 10 minutes
	 */
	public void avgCalc(){
		int ten = 600000; // number of ms in 10 minutes
		int hour = 3600000;
		
		int count = 0;
		
		
		turn = new int [6]; //average turn around time for each 10 minute interval in a hour
		wait = new int [6]; //average wait time for 10 minute intervals
		
		int j = 0;
		
		for(int i = 0; i < list.size(); i++){ //for all processes
			
			Node item = list.get(i);
			if(item.timeCompleted > (j +1) * ten){
				if (count != 0){
					turn[j] = turn[j] / count; //divide element by the number of counts
					wait[j] = wait[j] / count;
				}
				j++;
				turn[j] = 0;
				wait[j] = 0;
				
			}
			turn[j] += item.turnaround;
			wait[j] += item.wait;
			
			count ++;
 		}
		
		
		
		
		
	}
	/**
	 * return the turnaround array where each element represents a 10 minute interval
	 * @return turnaround array
	 */
	public int [] getTurn(){
		return turn;
		
	}
	/**
	 * return wait time array where each element represents a 10 minute interval
	 * @return wait time array
	 */
	public int [] getWait(){
		return wait;
	}
	
	

}
