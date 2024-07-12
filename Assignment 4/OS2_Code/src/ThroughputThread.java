package barScheduling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ThroughputThread implements Runnable {
    //Counter for each drink order completed
    private AtomicInteger count = new AtomicInteger(); 
    
    //Constructor
    public ThroughputThread(AtomicInteger count){
        this.count=count;
    }
    // Run method just adds the current value of counter to the throughput.txt file and then sets it to 0
    public void run(){
        try{
        FileWriter writer = new FileWriter("Throughput.txt", true);
        writer.write(Integer.toString(count.get())+"\n");
        writer.close();
        count.set(0);
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}
