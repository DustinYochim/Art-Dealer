package main.log;

import java.io.*;

public class LastWonFile {
    private static final String FILE_PATH = "LastWon.txt";

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

    public static void updateRoundNumber(int roundNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(String.valueOf(roundNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRoundNumber() {
        int currentRound = getCurrentRoundNumber();
        currentRound++;
        updateRoundNumber(currentRound);
    }

    public static void reset() {
        updateRoundNumber(0);}
}
