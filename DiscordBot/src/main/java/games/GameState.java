package games;

/**
 * @author Tom
 */
public enum GameState {
    PREREGISTRATION,     // Before registration starts
    OPEN_REGISTRATION,   // During registration
    CLOSED_REGISTRATION, // After registration
    ONGOING,             // Game started
    DONE                 // Game ended
}
