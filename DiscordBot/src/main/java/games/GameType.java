package games;

/**
 * @author Tom
 */
public enum GameType {
    BLACKJACK(10, "A card game that has a goal of reaching as close to 21 points, but not above. Aces may count as either 1 or 11 and face cards count as 10 each.", true),
    UNKNOWN(1, "UNKNOWN", false);


    private final int maximumPlayerSize;  // The maximum amount of players in the game
    private final String description;     // A description of the game
    private final boolean visible;        // Whether this game is visible when being listed


    GameType(int maximumPlayerSize, String description, boolean visible) {
        this.maximumPlayerSize = maximumPlayerSize;
        this.description = description;
        this.visible = visible;
    }


    /**
     * Gets the description of this type of game.
     * @return the description of this type of game.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Gets the maximum size of players that can play in a single game.
     * @return the maximum size.
     */
    public final int getMaximumPlayerCount() {
        return maximumPlayerSize;
    }

    /**
     * Checks if this game type is visible when being listed
     * @return true if it is visible, false otherwise.
     */
    public final boolean isVisible() {
        return visible;
    }
}
