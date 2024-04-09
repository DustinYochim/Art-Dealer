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

    public void clear() {
        cards.clear();
    }

    public void sortHand() {
        Collections.sort(cards, (card1, card2) -> {
            if (card1.getRank() != card2.getRank()) {
                return card1.getRank().compareTo(card2.getRank());
            } else {
                return card1.getSuit().compareTo(card2.getSuit());
            }
        });
    }

    public boolean isUnique(List<Hand> otherHands) {
        Set<Card> uniqueCards = new HashSet<>(cards);
        for (Hand hand : otherHands) {
            for (Card card : hand.getHand()) {
                if (uniqueCards.contains(card)) {
                    return false; // Found a duplicate card
                }
            }
        }
        return true; // No duplicate cards found
    }

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
