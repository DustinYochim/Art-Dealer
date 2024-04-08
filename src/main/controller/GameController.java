/*
    Authored by Dustin Yochim
 */

package main.controller;

import main.log.File;
import main.model.*;
import main.view.GUI;

/**
 * The GameController class handles the flow of the program. In early stages of the program
 * the controller just handles starting the program, dealing cards, and ending the program.
 */
public class GameController {

    private final GUI gui;

    final int NUM_CARDS_TO_DEAL = 4;

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

    /**
     * startGame() is called whenever the user clicks the start button in the welcome screen.
     * Once clicked, the main game screen is loaded and the log file is opened to prepare
     * for the writing of cards.
     */
    private void startGame() {
        gui.showGameScreen();
        File.openFile();
    }

    /**
     * quitGame() is used to end the program. It closes the log file and displays the goodbye screen.
     */
    private void quitGame() {
        File.closeFile();
        gui.showGoodbyeScreen();
    }

    /**
     * dealCards() deals cards into a user's hand and display's the cards in the GUI.
     * It also writes the current hand to the external log file.
     */
    private void dealCards() {
        Hand hand = gui.displayChoice();
        /*
            If the array of cards is not empty, add them to the user's hand, display them on the screen,
            display them in the external log file and the in-game log.
         */
        if (hand != null) {
            Hand dealerHand = chooseCardsBasedOnCurrentPattern(6, hand);
            gui.displayPrevious(hand.format_hand_for_logger()) ;
            File.writeToFile(hand.format_hand_for_logger());
            gui.displayHand(hand);
            System.out.println(hand.getHand());
        }

        // Reset chosenByDealer attributes to false for all cards in the hand
        if (hand != null) {
            for (Card card : hand.getHand()) {
                card.setChosenByDealer(false);
            }
        }
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