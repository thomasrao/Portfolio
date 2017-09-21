package games.cards;

/**
 * Created by Tom on 5/25/2017.
 */
public enum CardValue {
    ACE(1, ":one:"),
    TWO(2, ":two:"),
    THREE(3, ":three:"),
    FOUR(4, ":four:"),
    FIVE(5, ":five:"),
    SIX(6, ":six:"),
    SEVEN(7, ":seven:"),
    EIGHT(8, ":eight:"),
    NINE(9, ":nine:"),
    TEN(10, ":keycap_ten:"),
    JACK(11, ":one::one:"),
    QUEEN(12, ":one::two:"),
    KING(13, ":one::three:");

    private final int value;    // The value of the card
    private final String emoji; // The emoji representing the card value


    CardValue(int value, String emoji) {
        this.value = value;
        this.emoji = emoji;
    }


    /**
     * Gets the emoji representation for the type.
     * @return the string that converts into an emoji to represent the type.
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * Gets the value of this.
     * @return the value of this card value.
     */
    public int getValue() {
        return value;
    }
}
