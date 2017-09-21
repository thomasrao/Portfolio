package tools.rule34;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tools.logging.Log;

import java.io.IOException;
import java.util.*;

/**
 * @author Tom
 */
public final class Rule34Database {
    private static class Rule34Data {
        public final short discriminator;
        public final String name;


        public Rule34Data(String id, short discriminator) {
            this.name = id;
            this.discriminator = discriminator;
        }

        public String getURL() {
            return "http://img.rule34.xxx/images/" + discriminator + "/" + name;
        }
    }


    private final static String URL = "http://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=%s&pid=%d&limit=%d";
    private final static int LIMIT = 100;

    private final static Rule34Database instance;

    private Map<String, List<Rule34Data>> database;
    private Random random;


    static {
        instance = new Rule34Database();
    }

    private Rule34Database() {
        database = new HashMap<>();
        random = new Random();
    }


    public void add(String search, String name, short discriminator) {
        database.get(search).add(new Rule34Data(name, discriminator));
    }

    public static Rule34Database getInstance() {
        return instance;
    }

    public String fetch(String search) {
        if (!database.containsKey(search)) {
            Document document = fetchDocument(search, 0);
            if (document == null)
                return null;

            Element root = document.select("posts").first();
            int pages = (int) Math.round((double) Integer.parseInt(root.attr("count")) / LIMIT);

            database.put(search, new ArrayList<>());

            fetchData(search, document);

            for (int page = 1; page < pages; page++) {
                document = fetchDocument(search, page);

                if (document != null)
                    fetchData(search, document);
            }
        }

        if (database.get(search).size() > 0)
            return database.get(search).get(random.nextInt(database.get(search).size())).getURL();
        return null;
    }

    private Document fetchDocument(String search, int page) {
        try {
            return Jsoup.connect("http://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=" + search + "&pid=" + page + "&limit=" + LIMIT).get();
        } catch (IOException ioe) {
            Log.getInstance().error(ioe, "Rule34Database");
        }
        return null;
    }

    private void fetchData(String search, Document document) {
        Elements elements = document.select("posts post");

        for (Element child : elements) {
            String[] parts = child.attr("file_url").split("/");
            add(search, parts[5], Short.parseShort(parts[4]));
        }
    }
}
