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
import java.util.stream.Collectors;


/**
 * The GameController class handles the flow of the program. In early stages of the program
 * the controller just handles starting the program, dealing cards, and ending the program.
 */
public class GameController {

    private final GUI gui;

    final int NUM_CARDS_TO_DEAL = 4;

    private int currentRound;

    private int currentRoundWins = 0;

    final int WINS_REQUIRED_FOR_NEXT_ROUND = 2;
    final int TOTAL_ROUNDS = 6;
    final int COMPLETE_HAND_SIZE = 4;


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
        this.gui.addStartButtonListener(e -> startGame());
        this.gui.addDealButtonListener(e -> dealCards());
        this.gui.addQuitButtonListener(e -> quitGame());
    }

    public int getCurrentRound() {return currentRound;}
    private void incrementCurrentRound() {currentRound++;}


    /**
     * startGame() is called whenever the user clicks the start button in the welcome screen.
     * Once clicked, the main game screen is loaded and the log file is opened to prepare
     * for the writing of cards.
     */
    private void startGame() {

        // check if save data exists
        if (saveFileExists()) {
            int lastRoundWon = readRound();
            currentRound = lastRoundWon + 1;
        } else {
            currentRound = 1;
        }
        gui.showGameScreen(getCurrentRound());
        logFile.openFile();
    }

    private boolean saveFileExists() {
        File saveFile = new File("LastWon.txt");
        return saveFile.exists();
    }

    private int readRound() {
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
     * quitGame() is used to end the program. It closes the log file and displays the goodbye screen.
     */
    private void quitGame() {
        logFile.closeFile();
        gui.showGoodbyeScreen();
    }



    private final Set<Hand> usedHands = new HashSet<>();

    private boolean isUniqueHand(Hand currentHand) {
        for (Hand usedHand : usedHands) {
            if (areHandsEqual(currentHand, usedHand)) {
                return false;
            }
        }
        return true;
    }

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

    /**
     * dealCards() deals cards into a user's hand and display's the cards in the GUI.
     * It also writes the current hand to the external log file.
     */
    private void dealCards() {

        gui.displayCurrentRound();
        Hand hand = gui.displayChoice();

        // If the hand is empty just return
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

                if (dealerHand.getHand().size() == COMPLETE_HAND_SIZE) { // dealer chose all 4 cards
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

    private void handleUserScore() {
        currentRoundWins++;
        if (currentRoundWins == WINS_REQUIRED_FOR_NEXT_ROUND) {
            handleRoundWin();
        } else {
            gui.announceWin(currentRoundWins, WINS_REQUIRED_FOR_NEXT_ROUND);
        }
    }

    private void handleRoundWin() {
        gui.displayPrevious("USER WON PATTERN " + currentRound);
        logFile.writeToFile("USER WON PATTERN " + currentRound);
        if (currentRound == TOTAL_ROUNDS) {
            handleGameWin();
        } else {
            // User wins the round, increment current round and reset wins counter
            gui.announceWin(currentRoundWins, WINS_REQUIRED_FOR_NEXT_ROUND);
            incrementCurrentRound();
            gui.updateRoundNumber(currentRound);
            LastWonFile.saveRoundNumber();
            currentRoundWins = 0;
            usedHands.clear();
            gui.clearCardPanel();
        }

    }

    private void handleGameWin() {
        // User wins the game
        LastWonFile.reset();
        // gui.displayGameResult("Congratulations! You won the game!");
        int option = gui.displayRestartOption();
        if (option == 1) {
            restartGame();
            return; // Exit the method to avoid further execution
        } else {
            quitGame(); // Quit the game
            return; // Exit the method to avoid further execution
        }
    }

    private void restartGame() {
        currentRound = 1;
        currentRoundWins = 0;
        usedHands.clear();
        startGame();
    }


    private Hand chooseCardsBasedOnCurrentPattern(int pattern, Hand userHand) {
        return switch (pattern) {
            case 2 -> patternTwo(userHand);
            case 3 -> patternThree(userHand);
            case 4 -> patternFour(userHand);
            case 5 -> patternFive(userHand);
            case 6 -> patternSix(userHand);
            default -> patternOne(userHand);
        };
    }


    private Hand patternSix(Hand userHand) {
        Hand dealerHand = new Hand();

        Rank highestRank = userHand.findHighestRank();

        // Loop through the cards the user picked
        for (Card card : userHand.getHand()) {
            // Check if the card's suit is HEARTS or DIAMONDS
            if (card.getRank() == highestRank) {
                // Set chosenByDealer attribute to true for the selected card
                card.setChosenByDealer(true);
                // Add the card to the dealer's hand
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    private Hand patternFive(Hand userHand) {
        Hand dealerHand = new Hand();

        // Loop through the cards the user picked
        for (Card card : userHand.getHand()) {
            // Check if the card's suit is HEARTS or DIAMONDS
            if (card.getRank() == Rank.TWO || card.getRank() == Rank.THREE
                    || card.getRank() == Rank.FIVE || card.getRank() == Rank.SEVEN) {
                // Set chosenByDealer attribute to true for the selected card
                card.setChosenByDealer(true);
                // Add the card to the dealer's hand
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    private Hand patternFour(Hand userHand) {
        Hand dealerHand = new Hand();

        // Loop through the cards the user picked
        for (Card card : userHand.getHand()) {
            // Check if the card's suit is HEARTS or DIAMONDS
            if (card.getRank() == Rank.TWO || card.getRank() == Rank.THREE || card.getRank() == Rank.FOUR
            || card.getRank() == Rank.FIVE || card.getRank() == Rank.SIX || card.getRank() == Rank.SEVEN
            || card.getRank() == Rank.EIGHT || card.getRank() == Rank.NINE) {
                // Set chosenByDealer attribute to true for the selected card
                card.setChosenByDealer(true);
                // Add the card to the dealer's hand
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }

    private Hand patternThree(Hand userHand) {
        Hand dealerHand = new Hand();

        // Loop through the cards the user picked
        for (Card card : userHand.getHand()) {
            // Check if the card's suit is HEARTS or DIAMONDS
            if (card.getRank() == Rank.KING || card.getRank() == Rank.QUEEN || card.getRank() == Rank.JACK ) {
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
     * patternOne() method selects cards from the user's hand if they are a red card.
     *
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

        // Loop through the cards the user picked
        for (Card card : hand.getHand()) {
            // Check if the card's suit is CLUBS
            if (card.getSuit() == Suit.CLUBS) {
                // Set chosenByDealer attribute to true for the selected card
                card.setChosenByDealer(true);
                // Add the card to the dealer's hand
                dealerHand.addCard(card);
            }
        }

        // Return the dealer's hand
        return dealerHand;
    }
    }