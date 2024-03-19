/*
    The deck class was written by Dustin Yochim.
 */

package main.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of playing cards. The Deck is made up of Card objects.
 * The Deck class has methods for shuffling and dealing cards.
 */
public class Deck {

    private final List<Card> cards; // "cards" represents the deck of cards

    /**
     * Constructor initializes ArrayList of Cards.
     */
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    public Card getCard(Rank rank, Suit suit) {
        Card foundCard = null;
        for (Card card : this.cards) {
            if (card.getRank().equals(rank) && card.getSuit().equals(suit)) {
                foundCard = card;
                break;
            }
        }
        return foundCard;
    }

    /**
     * Uses the Rank and Suit enums to initialize a deck of cards.
     */
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Uses the Collections.shuffle() method to "shuffle" the deck of cards.
     */
    public void shuffleDeck() {
        Collections.shuffle(cards); // https://stackoverflow.com/a/16112539
    }

    /**
     * Shuffles the card deck, and then deals the given numCards into hand.
     * @param hand The user's "hand" of cards.
     * @param numCards The number of cards to deal.
     */
    public void dealRandomCardsIntoHand(Hand hand, int numCards) {
        this.shuffleDeck();

        /*
        Because the deck is shuffled, there is no need for a random number generator,
        instead the first four cards can just be drawn.
         */
        for (int i = 0; i < numCards; i++) {
            hand.addCard(cards.get(i));
        }
    }
}
