/*
    Authored by Dustin Yochim
 */
package main.model;

import java.util.*;

/**
 * The Hand class is used to represent a hand of cards. It includes methods for adding cards to the hand,
 * returning the hand, and formatting the hand for being output to a log.
 */
public class Hand {
    private final ArrayList<Card> cards; // cards represents the user's "hand" of cards.

    /**
     * Initializes the cards ArrayList which represents a users "hand".
     */
    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a playing card to a users hand.
     * @param card A playing card.
     */
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    /**
     * @return The current cards in the users hand.
     */
    public ArrayList<Card> getHand() {
        return cards;
    }


    /**
     * @return A users current hand, formatted for the logger.
     */
    public String format_hand_for_logger() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.format_card_for_logger()).append(",");
        }
        if (!sb.isEmpty()) {
            // Remove trailing comma
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * removes all cards from the hand
     */
    public void clear() {
        cards.clear();
    }

    /**
     * sorts a hand of cards by rank
     */
    public void sortHandByRank() {
        // https://www.baeldung.com/java-8-comparator-comparing
        cards.sort(Comparator.comparing(Card::getRank));
    }

    /**
     * @return true or false depending on if all cards in the hand are of the same suit
     */
    public boolean checkSameSuit() {
        Suit s = this.cards.get(0).getSuit();
        for (Card card : cards) {
            if (card.getSuit() != s) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true or false depending on if the card hand contains a rising rank
     */
    public boolean checkRisingRank() {
        Rank r = this.cards.get(0).getRank();

        for (int i = 1; i < cards.size(); i++) {
            if ((cards.get(i).rank_to_int() - cards.get(i-1).rank_to_int() != 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the highest ranked card in a hand of cards
     */
    public Rank findHighestRank() {
        Rank highestRank = null;
        for (Card card : cards) {
            if (highestRank == null || card.getRank().compareTo(highestRank) > 0) {
                highestRank = card.getRank();
            }
        }
        return highestRank;
    }


}
