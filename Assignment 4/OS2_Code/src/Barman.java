package barScheduling;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
/*
 Barman Thread class.
 */
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Barman extends Thread {
	

	private CountDownLatch startSignal;
	private BlockingQueue<DrinkOrder> orderQueue;
	AtomicInteger counter; 
	ScheduledExecutorService tpTracker = Executors.newScheduledThreadPool(1);

	Barman(CountDownLatch startSignal,int schedAlg, AtomicInteger counter) {
		// Arguments to determine which queue is for what
		if (schedAlg==0)
			this.orderQueue = new LinkedBlockingQueue<>();
		//FIX below
		else this.orderQueue = new PriorityBlockingQueue<>(420,Comparator.comparing(DrinkOrder::getExecutionTime)); 

		this.counter=counter; //Throughput counter so barman thread can deal with it.
		this.startSignal=startSignal;
	}
	
	
	public void placeDrinkOrder(DrinkOrder order) throws InterruptedException {
        orderQueue.put(order);
    }

	//Method to increment the counter to calculate the throughput
	public void incrementCounter(){
		counter.incrementAndGet();
	} 
	
	
	public void run() {
		try {
			DrinkOrder nextOrder;
			
			startSignal.countDown(); //barman ready
			startSignal.await(); //check latch - don't start until told to do so
			tpTracker.scheduleAtFixedRate(new ThroughputThread(counter), 0, 5, TimeUnit.SECONDS); // Starting thread to track throughput
			while(true) {
				nextOrder=orderQueue.take();
				System.out.println("---Barman preparing order for patron "+ nextOrder.toString());
				sleep(nextOrder.getExecutionTime()); //processing order
				System.out.println("---Barman has made order for patron "+ nextOrder.toString());
				nextOrder.orderDone();
			}
				
		} catch (InterruptedException e1) {
			System.out.println("---Barman is packing up ");
			tpTracker.shutdown(); // Terminating throughput thread
		}
	}
}


