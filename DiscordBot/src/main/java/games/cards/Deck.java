package games.cards;

import games.exceptions.InvalidCardTypeException;
import games.exceptions.InvalidCardValueException;
import tools.logging.Log;

/**
 * @author Tom
 */
public final class Deck {
    private Card[] deck; // The set of card part of the deck.
    private int index;   // The location of the next card.


    public Deck() {
        deck = new Card[CardValue.values().length * CardType.values().length];
        index = 0;

        setup();
    }


    /**
     * Gets the next card without removing it.
     * @return the next card in the deck.
     */
    public Card getNextCard() {
        return deck[index];
    }

    /**
     * Checks if there are enough cards.
     * @param count number of cards required to be available.
     * @return true if there are enough cards, false otherwise.
     */
    public boolean hasRemainingCard(int count) {
        return deck.length - index >= count;
    }

    /**
     * Checks if there's at least 1 card remaining.
     * @return
     */
    public boolean hasRemainingCard() {
        return index < deck.length;
    }

    /**
     * Picks the next card and removes it from the deck.
     * @return the card picked up.
     */
    public Card pickNextCard() {
        return deck[index++];
    }

    /**
     * Restarts the deck and possibly shuffle.
     * @param shuffle a flag to shuffle the deck.
     */
    public void restart(boolean shuffle) {
        index = 0;

        if (shuffle)
            shuffle();
    }

    /**
     * Sets up the deck properly.
     */
    private void setup() {
        int count = 0;
        try {
            for (CardValue value : CardValue.values()) {
                for (CardType type : CardType.values()) {
                    deck[count++] = new Card(value, type);
                }
            }
        } catch (InvalidCardValueException | InvalidCardTypeException ice) {
            // This should never happen
            Log.getInstance().error(ice, "Deck");
        }
    }

    /**
     * Shuffles the deck of card.
     * Similar to the riffle shuffle, but not quite.
     */
    public void shuffle() {
        int left = index;
        int right = deck.length - 1;

        while (left <= right) {
            if (Math.random() >= 0.5) { // Right card goes next
                Card temp = deck[left];
                deck[left] = deck[right];
                deck[right] = temp;
            }

            left++;
        }
    }
}
