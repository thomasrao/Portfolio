package commands.guildchannel;


/**
 * Gets a URL which searches for a term
 *  that the user has inputted from Google.
 */

public class GoogleSearch extends Search {
    protected GoogleSearch() {
        super("google", "Google", "https://www.google.com", "/#q=");
    }
}
