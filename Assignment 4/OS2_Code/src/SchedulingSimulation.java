//M. M. Kuttel 2024 mkuttel@gmail.com

package barScheduling;
// the main class, starts all threads


import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


public class SchedulingSimulation {
	static int noPatrons=100; //number of customers - default value if not provided on command line
	static int sched=0; //which scheduling algorithm, 0= FCFS
	static int fileNum=0; //Default name for file if not specified in args
			
	static CountDownLatch startSignal;

	
	static Patron[] patrons; // array for customer threads
	static Barman Andre;
	static FileWriter writer;
	static FileWriter throughputWriter; // File writer for thoughput
	static AtomicInteger counter = new AtomicInteger(0); //Integer used to track throughput
	public void writeToFile(String data) throws IOException {
	    synchronized (writer) {
	    	writer.write(data);
	    }
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		long startTime, endTime; //Timing metrics
		int throughputCounter=0;

		//deal with command line arguments if provided
		if (args.length==1) {
			noPatrons=Integer.parseInt(args[0]);  //total people to enter room
		} else if (args.length==2) {
			noPatrons=Integer.parseInt(args[0]);  //total people to enter room
			sched=Integer.parseInt(args[1]); 
		} 
		// Starting writers
		writer = new FileWriter("Measurements.txt", false);
		throughputWriter = new FileWriter("Throughput.txt",false);
		
		Patron.fileW=writer;

		startSignal= new CountDownLatch(noPatrons+2);//Barman and patrons and main method must be raeady
		
		//create barman
        Andre= new Barman(startSignal,sched,counter); 
     	Andre.start();
  
	    //create all the patrons, who all need access to Andre
		patrons = new Patron[noPatrons];
		for (int i=0;i<noPatrons;i++) {
			patrons[i] = new Patron(i,startSignal,Andre);
			patrons[i].start();
		}
		
		System.out.println("------Andre the Barman Scheduling Simulation------");
		System.out.println("-------------- with "+ Integer.toString(noPatrons) + " patrons---------------");

      	startSignal.countDown(); //main method ready
      	startTime = System.currentTimeMillis(); // Starting throughput timer
      	//wait till all patrons done, otherwise race condition on the file closing!
      	for (int i=0;i<noPatrons;i++) {
			patrons[i].join();
		}

    	System.out.println("------Waiting for Andre------");
    	Andre.interrupt();   //tell Andre to close up
		endTime = System.currentTimeMillis(); // End timer for throughput
    	Andre.join(); //wait till he has
		throughputWriter.close();
      	writer.close(); //all done, can close file
		
      	System.out.println("------Bar closed------");
	}

}
