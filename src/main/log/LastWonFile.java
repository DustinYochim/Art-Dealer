/*
* The LastWonFile.java file handles opening, reading/writing, and closing the LastWon.txt file in order to load the
* user's save data. If the file does not exist, a file is created and user starts from round 1
*
* */

package main.log;

import java.io.*;

/**
 * LastWonFile class contains methods for getting and updating the currentRound attribute.
 */
public class LastWonFile {
    private static final String FILE_PATH = "LastWon.txt"; // file path of save data

    /**
     * Read's the save data and returns the current round number if it exists, otherwise returns 1
     * @return the current round
     */
    public static int getCurrentRoundNumber() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 1;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /** Takes in the current round number and updates the file
     * @param roundNumber the current round number
     */
    public static void updateRoundNumber(int roundNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(String.valueOf(roundNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current round number, increments it, and then calls updateRoundNumber to update the file
     */
    public static void saveRoundNumber() {
        int currentRound = getCurrentRoundNumber();
        currentRound++;
        updateRoundNumber(currentRound);
    }

    /**
     * Reset's the last round number to 0.
     */
    public static void reset() {
        updateRoundNumber(0);}
}
