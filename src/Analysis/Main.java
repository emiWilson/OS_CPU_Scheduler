package Analysis;

import java.io.*;
import java.util.*;
import SampleFiles.*;

//import SampleFiles.*;

public class Main {
	public static void main(String [] args){
		SampleReader read = new SampleReader("data"); //data
		LinkedList<Node> list = read.listBuild();
		
		SchedAlgorithms alg = new SchedAlgorithms(list);
		//alg.FCFS();
		//alg.SJF();
		
		alg.Priority();
		//alg.RR();
		//alg.print();
		
		alg.analysis();
		alg.print();

		
	}
}


 // This class is the modified SampleReader.java file from course files
 
class SampleReader
{
	DataInputStream dis;

	
	public SampleReader(String sampleFile)
	{
		try {
			File f = new File(sampleFile);
			if (!f.exists()) {
				System.out.println(sampleFile + " does not exist");
				System.exit(-1);
			}
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
		} 
		catch (IOException ie) {
			System.out.println("Something wrong to open " + sampleFile);
			System.exit(-2);
		}
		
	}
	
	public LinkedList<Node> listBuild()
	{
	
		
		int process, arrival, priority, burst;
		LinkedList<Node> list = new LinkedList<Node>();
		System.out.println("proc" + "    " + "arr" + "    " + "prior" + "  " + "burst");
		while(true) {
			process = -1;
			arrival = -1;
			priority = -1;
			burst = -1;
			process = readProcess();
			if (process < 0)
				break;
			arrival = readArrival();
			if (arrival < 0)
				break;
			priority = readPriority();
			if (priority < 0)
				break;
			burst = readBurst();
			if (burst < 0)
				break;
			
			Node next = new Node(process, arrival, priority, burst);
			list.add(next);

				
			System.out.println(process + "       " + arrival + "       " + priority + "       " + burst);
		}
		//System.out.println();
		return list;
	}

	
	public int readProcess()
	{
		try {
			return dis.readInt();
		} catch (IOException ie) {
			return -1;
		}
	}
	
	public int readArrival()
	{
		try {
			return dis.readInt();
		} catch (IOException ie) {
			return -1;
		}
	}
	
	public int readPriority()
	{
		try {
			return dis.readInt();
		} catch (IOException ie) {
			return -1;
		}
	}
	
	public int readBurst()
	{
		try {
			return dis.readInt();
		} catch (IOException ie) {
			return -1;
		}
	}
	

}

