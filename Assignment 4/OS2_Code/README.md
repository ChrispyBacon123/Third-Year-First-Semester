# Andre Strikes Again

This Java program is meant to demonstrate the differences in performance between Shortest Job First (SJF) scheduling compared to First Come First Served (FCFS) scheduling. 

To do this, the program simulates a night at a bar. It does this by having a group of patrons enter and submit their drinks orders to the barman who then has to make and serve them. 

The barman represents the CPU handling instructions and the patrons are processes.
The program takes measure of the following metrics to determine the effectiveness of the algorithm: 
- Turn around time 
- Throughput 
- Response time 
- The waiting time of each process


## Usage

To run the program, follow these steps:

Open the OS2_Code folder

Run the command make run which will run the follwoing command:
 ```bash
   -cp bin barScheduling.SchedulingSimulation 10 0
   ```
Note if you wish to change the arguments of the file, you have to edit the args variable.
  - The first variable is the number of patrons (it will initiall by set to 100)
  - The second variable is which algorithm you wish to use (set to 0 for FCFS, set to 1 for SJF)

## Program Structure

The program consists of three main classes:

1. `DrinkOrder`: Represents an order by a patron that the barman must fufill (this represents a task to be done by the CPU)

2. `Patron`: Represents a bar goer, the patron submits a drinks list to the barman (this represents a process in an OS)

3. `Barman`: Represents a barman (go figure) which makes the drinks after recieving the orders (this represents the CPU handling processes) 

4. `Scheduling simulation`: Represents the night where patrons arrive and give their orders to the barman.

5. `Drink`: Respresents each individual drink that is part of the drink order.

6. `ThroughputThread`: This class is used to create a thread that will calculate the throughput every 5 seconds.

## Outputs

Upon running the program, you will be presented with various strings that explain the ongoings of the bar. 
An output file called Throughput.txt and Measurements.txt will be created.
The files will have the following data values

Measurements.txt
<patronID> <Turn around time> <Waiting time for each patron> <Response time for each patron>

Throughput.txt
<Number of patrons served for 5 second intervals>


## Notes

- Make sure you have Java Development Kit (JDK) installed on your system to compile and run the program.

## Credits

- Original concept by Michelle Kuttle (University of Cape Town 2024).

- Christopher Blignaut (BLGCHR003) extended the code to track for the metrics above (University of Cape Town 2024). 

