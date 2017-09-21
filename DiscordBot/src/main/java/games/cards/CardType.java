package games.cards;

/**
 * @author Tom
 */
public enum CardType {
    SPADE(":spades:"),
    CLUB(":clubs:"),
    HEART(":hearts:"),
    DIAMOND(":diamonds:");

    private final String emoji; // The string to represent the type with a Discord emoji.


    CardType(String emoji) {
        this.emoji = emoji;
    }


    /**
     * Gets the emoji representation for the type.
     * @return the string that converts into an emoji to represent the type.
     */
    public String getEmoji() {
        return emoji;
    }
}
