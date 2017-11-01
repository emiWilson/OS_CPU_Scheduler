package Analysis;

public class Node {
	

		public int process;
		public int arrival;
		public int priority;
		public int burst;
		
		public int index = -1;
		public int timeStarted = -1;
		public int runTimeLeft = -1;
		public int timeCompleted = -1;
		
		public Node(int proc, int arr, int prior, int bur){
			process = proc;
			arrival = arr;
			priority = prior;
			burst = bur;
			runTimeLeft = burst;
		
		}
		
		//index in original list - for swapping purposes!
		public void setIndex(int i){
			index = i;
		}
		
		public void startTime(int t){
			timeStarted = t;
		}
		
		public void completionTime(int t){
			timeCompleted = t;
		}
		
		public void addTimeRan(){
			runTimeLeft--;	
		}
		

}
