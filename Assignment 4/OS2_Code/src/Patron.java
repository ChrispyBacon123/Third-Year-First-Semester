//M. M. Kuttel 2024 mkuttel@gmail.com
package barScheduling;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/*
 This is the basic class, representing the patrons at the bar
 */

public class Patron extends Thread {
	
	private Random random = new Random();// for variation in Patron behaviour

	private CountDownLatch startSignal; //all start at once, actually shared
	private Barman theBarman; //the Barman is actually shared though

	private int ID; //thread ID 
	private int lengthOfOrder;
	private long startTime, endTime; //for all the metrics
	private long startResponse,stopResponse;
	private long startWaiting,stopWaiting;
	private long accumulatedWaitingTime, executionTime=0;
	public static FileWriter fileW;


	private DrinkOrder [] drinksOrder;
	
	Patron( int ID,  CountDownLatch startSignal, Barman aBarman) {
		this.ID=ID;
		this.startSignal=startSignal;
		this.theBarman=aBarman;
		this.lengthOfOrder=random.nextInt(5)+1;//between 1 and 5 drinks
		drinksOrder=new DrinkOrder[lengthOfOrder];
	}
	
	public  void writeToFile(String data) throws IOException {
	    synchronized (fileW) {
	    	fileW.write(data);
	    }
	}
	
	

	public void run() {
		try {
			//Do NOT change the block of code below - this is the arrival times
			startSignal.countDown(); //this patron is ready
			startSignal.await(); //wait till everyone is ready
	        int arrivalTime = random.nextInt(300)+ID*100;  // patrons arrive gradually later
	        sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
			System.out.println("thirsty Patron "+ this.ID +" arrived");
			//END do not change
			
	        //create drinks order
	        for(int i=0;i<lengthOfOrder;i++) {
	        	drinksOrder[i]=new DrinkOrder(this.ID);
	        	
	        }
			System.out.println("Patron "+ this.ID + " submitting order of " + lengthOfOrder +" drinks"); //output in standard format  - do not change this
	        startTime = System.currentTimeMillis();//started placing orders
			
			// Placing the orders
			for(int i=0;i<lengthOfOrder;i++) {
				System.out.println("Order placed by " + drinksOrder[i].toString());
				theBarman.placeDrinkOrder(drinksOrder[i]);
				executionTime = executionTime + drinksOrder[i].getExecutionTime(); //Determining total amount of time barman is preparing patron's drink order
			}
			startResponse = System.currentTimeMillis(); //Starting timer for response time
			
			// Waiting for the orders 
			for(int i=0;i<lengthOfOrder;i++) {
				drinksOrder[i].waitForOrder();
				if(i==0){stopResponse = System.currentTimeMillis();} // Getting time that patron recieves their first drink
			}
			theBarman.incrementCounter(); // Incrementing the throughput counter

			// Calculating times 
			endTime = System.currentTimeMillis();
			long turnAroundTime = endTime - startTime;
			long responseTime = stopResponse - startResponse;
			accumulatedWaitingTime = turnAroundTime - executionTime;
			// Writing times of patron into the file
			writeToFile(String.format("%d\t%d\t%d\t%d\n",ID,turnAroundTime,accumulatedWaitingTime,responseTime));
			System.out.println("Patron "+ this.ID + " got order in " + turnAroundTime);
			
			
		} catch (InterruptedException e1) {  //do nothing
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
}
}
	

