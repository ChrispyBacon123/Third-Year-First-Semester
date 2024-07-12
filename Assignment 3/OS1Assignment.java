import java.util.*;
import java.io.*;

public class OS1Assignment {
    
public static void main(String[] args) {
    ArrayList<String> translatedEnteries = new ArrayList<>();
    ArrayList<String> virtualEntries = new ArrayList<>();
    Map<String,String> convertTbl = new HashMap<String,String>();

    // Conversion table is hardcoded as per assignment requirements
    convertTbl.put("00000","00010");
    convertTbl.put("00001","00100");
    convertTbl.put("00010","00001");
    convertTbl.put("00011","00111");
    convertTbl.put("00100","00011");
    convertTbl.put("00101","00101");
    convertTbl.put("00110","00110");

    // Placeholder variables
    String entry="";
    int counter = 0;
    String filename = args[0];
    // Reading in virtual memory addresses
    try (InputStream is = new FileInputStream(filename)) {
        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        for (byte b : buffer) {
            virtualEntries.add(String.format("%02x", b));
        }
        
        // Loop combines each address from 8 separated enteries into 1 entry. 
        // Loop also converts addresses to little endian
        for(int i=virtualEntries.size()-1;i>=0;i--){
            entry = entry +virtualEntries.get(i);
            counter++;
            if (counter%8==0){
                translatedEnteries.add(entry);
                entry="";
            }
        }
        // Reverses list so addresses are written in the correct way
        Collections.reverse(translatedEnteries);


        // Loop converts addresses to binary 
        for(int i=0; i<=translatedEnteries.size()-1;i++){          
            translatedEnteries.set(i,hexToBinary(translatedEnteries.get(i)));
        }

        // Using Hashmap to translate from page table addresses to physical memory addresses
        for(int i=0; i<=translatedEnteries.size()-1;i++){
            // Splitting the offset and the portion to be hashed
            String hashAddress = translatedEnteries.get(i).substring(0, 5);
            String offset = translatedEnteries.get(i).substring(5);
            hashAddress = convertTbl.get(hashAddress);
            // Converting back to hexadecimal and storing in arraylist
            String translated = binaryToHex(hashAddress+offset);
            translatedEnteries.set(i, translated);
        }

        writeOutputToFile(translatedEnteries);
    } 

    catch(IOException e){
        System.out.println("Could not read input file");
    }

}
   
/**
 * This method converts a hexadecimal string into a binary string (The binary string is padded so it will have 12 characters)
 * 
 * @param input the hexadecimal string to be converted into binary
 * @return the binary value of the hexadecimal string 
 */
public static String hexToBinary(String input) {
        // Convert hex-dec string to int
        int inthex = Integer.parseInt(input, 16);
        // Convert int to binary
        String binary = Integer.toBinaryString(inthex);
        // Paddin with 0's to ensure conversion can still be done
        while(binary.length()<12){
            binary = "0"+binary;
        }
        return binary;
    }

/**
 * This method will take in a binary string and convert it to it's hexadecimal equivalent value 
 * @param input the binary string to be converted to hexadecimal
 * @return A string representing the hexadecimal value of the binary string entered 
 */
public static String binaryToHex(String input){
    //Convert binary to int 
    int intbin=Integer.parseInt(input, 2);
    //Convert int to hex-dec
    return Integer.toHexString(intbin);
}

/**
 * This method accepts an array list and writes each entry onto a new line of a text file
 * @param addresses this is the list of addresses to be written into the text file
 */
public static void writeOutputToFile(List<String> addresses) { 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output-OS1.test"))) {
            // Loop through each entry and append it to the text file
            for (String address : addresses) {
                writer.write("0x"+address);
                writer.newLine(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}