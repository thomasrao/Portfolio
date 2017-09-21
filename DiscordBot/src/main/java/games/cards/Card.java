package games.cards;

import games.exceptions.InvalidCardTypeException;
import games.exceptions.InvalidCardValueException;

/**
 * @author Tom
 */
public final class Card {
    private final CardValue value; // The value of a card
    private final CardType type;   // The type of the card


    public Card(CardValue value, CardType type) throws InvalidCardValueException, InvalidCardTypeException {
        if (value == null)
            throw new InvalidCardValueException();
        else if (type == null)
            throw new InvalidCardTypeException();

        this.value = value;
        this.type = type;
    }


    /**
     * Gets the type of the card as a CardType enum.
     * @return the type of the card.
     */
    public CardType getType() {
        return type;
    }

    /**
     * Gets the value of the card.
     * @return the value of the card.
     */
    public int getValue() {
        return value.getValue();
    }

    @Override
    public String toString() {
        return value.getEmoji() + " " + type.getEmoji();
    }
}
