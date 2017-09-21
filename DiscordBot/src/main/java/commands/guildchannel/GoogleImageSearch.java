package commands.guildchannel;


/**
 * Gets a URL which searches for a term
 *  that the user has inputted from
 *  Google Images.
 */

public class GoogleImageSearch extends Search {
    protected GoogleImageSearch() {
        super("googleimg", "Google", "https://images.google.com", "/search?tbm=isch&q=");
    }
}
