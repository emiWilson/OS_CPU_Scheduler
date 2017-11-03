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
		turn = new int[(hour / ten)]; 
		wait = new int[(hour / ten)];
		
		int j = 0; //index in arrays turn and wait
		int counter = 0; //number of processes in a given interval
		/*******************
		 * Need to work on this part, may be off!
		 */
		for (int i = 0; i < list.size(); i++){
			Node node = list.get(i);
			
			if(node.timeCompleted <= j*3600){
				turn[j] += node.turnaround;
				wait[j] += node.wait;
				counter++;
			}
			
			if(list.get(i).timeCompleted > j*3600){
				if(counter != 0){
					turn[j] = turn[j]/counter;
					wait[j] = wait[j]/counter;
				}
				counter = 0; //reset counter to 0 for next 10 minute interval
			}
			
			
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
