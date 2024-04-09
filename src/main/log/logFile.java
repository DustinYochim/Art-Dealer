/*
    Authored by Adam Loepker
    https://www.w3schools.com/java/java_methods.asp
    https://www.geeksforgeeks.org/io-bufferedwriter-class-methods-java/
    https://www.educative.io/answers/what-is-the-printstacktrace-method-in-java
 */

package main.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The File class is used to log the dealt hands to an external file.
 */
public class logFile {
    private static BufferedWriter writer;

    private static final Logger LOGGER = Logger.getLogger(logFile.class.getName());

    /**
     * Opens the log file for appending.
     */
    public static void openFile(){
        try {
            writer = new BufferedWriter(new FileWriter("CardsDealt.txt", true));
            // Add date stamp to the first new line
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateStamp = dateFormat.format(new Date());
            writer.write(dateStamp);
            writer.newLine();
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "Error opening file", e);
        }
    }

    /**
     * Write the user's current "hand" to the log file.
     * @param line A users current "hand".
     */
    public static void writeToFile(String line){
        try{
            writer.write(line);
            writer.newLine();
        } catch(IOException e){
            LOGGER.log(Level.SEVERE, "Error writing to file", e);
        }
    }

    /**
     * Closes the log file.
     */
    public static void closeFile(){
        try{
            writer.close();
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "Error closing file", e);
        }
    }

}
