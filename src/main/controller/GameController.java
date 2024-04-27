/*
    Authored by Dustin Yochim
 */

package main.controller;

import main.log.LastWonFile;
import main.log.logFile;
import main.model.*;
import main.view.GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * The GameController class handles the flow of the program. In early stages of the program
 * the controller just handles starting the program, dealing cards, and ending the program.
 */
public class GameController {

    /******************************* START OF CLASS ATTRIBUTES **********************************************/
    private final GUI gui;

    final int HAND_SIZE = 4; // the size of a user's hand

    private int currentRound; // used to keep track of the user's current round

    private int currentRoundWins = 0; // used to keep track of wins in current round, 2 required

    final int WINS_REQUIRED_FOR_NEXT_ROUND = 2; // number of wins required to advance to next round
    final int TOTAL_ROUNDS = 7; // in HW4 there are 6 total rounds

    private final Set<Hand> usedHands = new HashSet<>(); // used to keep track of hands in a round

    /******************************* END OF CLASS ATTRIBUTES **********************************************/



    /**
     * The Constructor initializes the action listeners.
     *
     * @param deck A deck of cards.
     * @param gui  An instance of the GUI.
     */
    public GameController(Deck deck, GUI gui) {
        /*
        deck and gui are initialized in App.java and passed in to the controller's
        constructor so that the controller can control the flow between the game and the GUI.
     */
        this.gui = gui;

        // listeners in GUI return flow back here so that we can control the game flow
        this.gui.addStartButtonListener(e -> handleStartGameButtonClick());
        this.gui.addDealButtonListener(e -> handlePickCardsButtonClick());
        this.gui.addQuitButtonListener(e -> handleQuitGameButtonClick());
        this.gui.addHowToPlayButtonListener(e -> handleHowToPlayButtonClick());
        this.gui.addBackButtonListener(e -> handleBackButtonClick());
    }

    private void handleBackButtonClick() {
        gui.showWelcomeScreen();
    }

    private void handleHowToPlayButtonClick() {
        gui.showInstructionsScreen();
    }

    /******************************* START OF BUTTON CLICK HANDLERS **********************************************/
    /**
     * handleStartGameButtonClick() is called whenever the user clicks the start button in the welcome screen.
     * Once clicked, the game is started.
     */
    private void handleStartGameButtonClick() {
        startGame();
    }

    /**
     * handleQuitGame() is called whenever the user clicks the quit button, and it calls the quitGame method.
     */
    private void handleQuitGameButtonClick() {
        quitGame();
    }

    /**
     * handlePickCardsButtonClick() gets the card choices from the user, verifies uniqueness, updates the display,
     * and handles whether the user scores or not.
     */
    private void handlePickCardsButtonClick() {

        gui.displayCurrentRound();
        Hand hand = gui.displayChoice();

        // If the hand is empty (user canceled selection) just return
        if (hand == null) {
            return;
        }

        // Check if the hand has already been used
        if (!usedHands.contains(hand)) {
            if (isUniqueHand(hand)) {
                Hand dealerHand = chooseCardsBasedOnCurrentPattern(currentRound, hand);
                gui.displayPrevious(hand.format_hand_for_logger());
                logFile.writeToFile(hand.format_hand_for_logger());
                gui.displayHand(hand);

                usedHands.add(hand);

                if (dealerHand.getHand().size() == HAND_SIZE) { // dealer chose all 4 cards
                    handleUserScore();
                }
            } else {
                gui.showSameHandWarning();
            }
        } else {
            gui.showSameHandWarning(); // Inform the user that the hand has already been used
        }

        // Reset chosenByDealer attributes to false for all cards in the hand
        for (Card card : hand.getHand()) {
            card.setChosenByDealer(false);
        }
    }

    /******************************* END OF BUTTON CLICK HANDLERS **********************************************/

    /*
    startGame loads the save data (if exists), shows the game screen, and opens the log file for writing.
     */
    private void startGame() {
        loadSaveData();
        gui.showGameScreen(getCurrentRound());
        logFile.openFile();
    }

    /**
     * restartGame resets the round, wins, and userHand and then start's game over
     */
    private void restartGame() {
        currentRound = 1;
        currentRoundWins = 0;
        usedHands.clear();
        startGame();
    }

    /**
     * quitGame() closes the log file and shows the goodbye screen
     */
    private void quitGame() {
        logFile.closeFile();
        gui.showGoodbyeScreen();
    }

    /**
     * handleUserScore increments the currentRoundsWins and determines if user advances to next round or not
     */
    private void handleUserScore() {
        currentRoundWins++;
        if (currentRoundWins == WINS_REQUIRED_FOR_NEXT_ROUND) {
            handleRoundWin();
        } else {
            gui.announceWin(currentRoundWins, WINS_REQUIRED_FOR_NEXT_ROUND);
        }
    }

    /**
     * handleRoundWin() updates the GUI and log file for a user winning a round. If the user wins the last round, it
     * also calls the handleGameWin() method.
     */
    private void handleRoundWin() {

        gui.displayPrevious("USER WON PATTERN " + currentRound);
        logFile.writeToFile("USER WON PATTERN " + currentRound);
        if (currentRound == TOTAL_ROUNDS) {
            handleGameWin();
        } else {
            // User wins the round, increment current round and reset wins counter
            gui.announceWin(currentRoundWins, WINS_REQUIRED_FOR_NEXT_ROUND);
            try {
                gui.playVictorySound();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error playing victory sound: " + e.getMessage());
            }
            incrementCurrentRound();
            gui.updateRoundNumber(currentRound);
            LastWonFile.saveRoundNumber();
            currentRoundWins = 0;
            usedHands.clear();
            gui.clearCardPanel();
        }

    }

    /**
     * handleGameWin() resets the game data and gives the user the option to play again or to quit
     */
    private void handleGameWin() {
        LastWonFile.reset();
        int option = gui.displayRestartOption();

        if (option == 1) {
            restartGame();
        } else {
            quitGame();
        }
    }

    /**
     * @param pattern the current round is used to determine the pattern
     * @param userHand the user's current hand
     * @return the dealer's choices based on picking cards from the user hand using the correct pattern
     */
    private Hand chooseCardsBasedOnCurrentPattern(int pattern, Hand userHand) {
        return switch (pattern) {
            case 2 -> patternTwo(userHand);
            case 3 -> patternThree(userHand);
            case 4 -> patternFour(userHand);
            case 5 -> patternFive(userHand);
            case 6 -> patternSix(userHand);
            case 7 -> patternSeven(userHand);
            default -> patternOne(userHand);
        };
    }

    /***************************** START OF DEALER PATTERNS *******************************************/

    /**
     * patternOne() method selects cards from the user's hand if they are a red card.
     * Written by Jonathon
     * @param hand The user's hand of cards.
     * @return The dealer's hand of selected cards.
     */
    private Hand patternOne(Hand hand) {
            Hand dealerHand = new Hand();

            // Loop through the cards the user picked
            for (Card card : hand.getHand()) {
                // Check if the card's suit is HEARTS or DIAMONDS
                if (card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS) {
                    // Set chosenByDealer attribute to true for the selected card
                    card.setChosenByDealer(true);
                    // Add the card to the dealer's hand
                    dealerHand.addCard(card);
                }
            }

            // Return the dealer's hand
            return dealerHand;
        }

    /**
     * patternTwo() method selects cards from the user's hand if they are a club.
     *
     * @param hand The user's hand of cards.
     * @return The dealer's hand of selected cards.
     */
    private Hand patternTwo(Hand hand) {
        Hand dealerHand = new Hand();

        for (Card card : hand.getHand()) {
            if (card.getSuit() == Suit.CLUBS) {
                card.setChosenByDealer(true);
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    /**
     * @param userHand the user's selected hand
     * @return The dealers choices consisting of All Face Cards - Kings, Queens, and Jacks
     */
    private Hand patternThree(Hand userHand) {
        Hand dealerHand = new Hand();

        for (Card card : userHand.getHand()) {
            if (card.getRank() == Rank.KING || card.getRank() == Rank.QUEEN || card.getRank() == Rank.JACK ) {
                card.setChosenByDealer(true);
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    /**
     * @param userHand the user's selected hand
     * @return the art dealers choices, consisting of all single digit cards (2,3,4,5,6,7,8,9)
     */
    private Hand patternFour(Hand userHand) {
        Hand dealerHand = new Hand();

        // Loop through the cards the user picked
        for (Card card : userHand.getHand()) {
            if (card.getRank() == Rank.TWO || card.getRank() == Rank.THREE || card.getRank() == Rank.FOUR
                    || card.getRank() == Rank.FIVE || card.getRank() == Rank.SIX || card.getRank() == Rank.SEVEN
                    || card.getRank() == Rank.EIGHT || card.getRank() == Rank.NINE) {
                card.setChosenByDealer(true);
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    /**
     * @param userHand the user's selected cards
     * @return the art dealers selections, consisting of all single digit primes - 2,3,5,7
     */
    private Hand patternFive(Hand userHand) {
        Hand dealerHand = new Hand();

        for (Card card : userHand.getHand()) {
            if (card.getRank() == Rank.TWO || card.getRank() == Rank.THREE
                    || card.getRank() == Rank.FIVE || card.getRank() == Rank.SEVEN) {
                card.setChosenByDealer(true);
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    /**
     * @param userHand the user's selected cards
     * @return the art dealer selections, consisting of the highest rank cards from the current hand
     */
    private Hand patternSix(Hand userHand) {
        Hand dealerHand = new Hand();

        Rank highestRank = userHand.findHighestRank();

        for (Card card : userHand.getHand()) {
            if (card.getRank() == highestRank) {
                card.setChosenByDealer(true);
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    /**
     * @param userHand the user's selected cards
     * @return the art dealer selections, the user will select all four cards if and only if they are rising run in
     * same suit
     */
    private Hand patternSeven(Hand userHand) {
        Hand dealerHand = new Hand();

        if (userHand.checkSameSuit() && userHand.checkRisingRank()) {
            for (Card card : userHand.getHand()) {
                card.setChosenByDealer(true);
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    /***************************** END OF DEALER PATTERNS *******************************************/


    /***************************** START OF UTILITY METHODS *******************************************/

    /**
     * getCurrentRound() returns the current round the user is on.
     * @return the current round
     */
    public int getCurrentRound() {return currentRound;}

    /**
     * incrementCurrentRound() increments the current round by 1.
     */
    private void incrementCurrentRound() {currentRound++;}

    /**
     * loads the save data from the LastWon.txt file
     */
    private void loadSaveData() {
        // check if save data exists
        if (saveFileExists()) {
            int lastRoundWon = readRoundFromFile();
            currentRound = lastRoundWon + 1;
        } else {
            currentRound = 1; // if save file does not exist default to round 1
        }
    }

    /**
     * @return whether the save file exists
     */
    private boolean saveFileExists() {
        File saveFile = new File("LastWon.txt");
        return saveFile.exists();
    }

    /**
     * @return the Last Won round from the user coming from LastWon.txt
     */
    private int readRoundFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("LastWon.txt"));
            String line = reader.readLine();
            reader.close();
            return Integer.parseInt(line);
        } catch(IOException | NumberFormatException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * @param currentHand the user's current hand
     * @return whether the hand exists in usedHands
     */
    private boolean isUniqueHand(Hand currentHand) {
        for (Hand usedHand : usedHands) {
            if (areHandsEqual(currentHand, usedHand)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param hand1 a hand of cards
     * @param hand2 another hand of cards
     * @return whether the 2 hands are equal
     */
    private boolean areHandsEqual(Hand hand1, Hand hand2) {
        List<Card> cards1 = hand1.getHand();
        List<Card> cards2 = hand2.getHand();

        if (cards1.size() != cards2.size()) {
            return false;
        }

        for (Card card : cards1) {
            if (!cards2.contains(card)) {
                return false;
            }
        }

        for (Card card : cards2) {
            if (!cards1.contains(card)) {
                return false;
            }
        }

        return true;
    }

    /***************************** END OF UTILITY METHODS *******************************************/

    }