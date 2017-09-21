package tools.factories;

import tools.logging.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Tom
 */
public final class ShitpostFactory implements IFactory<String> {
    private final static String SHITPOST_FILE_PATH = "data/shitpost.txt";

    private final static ShitpostFactory _instance;

    private List<String> _shitposts; // List of messages
    private Random _random;          // Random number generator
    private boolean _modified;       // Modified flag


    static {
        _instance = new ShitpostFactory();
    }

    private ShitpostFactory() {
        _shitposts = new ArrayList<>();
        _random = new Random();
        _modified = false;
    }


    /**
     * Adds a message to the list of possible messages.
     * @param value
     */
    @Override
    public void add(String value) {
        _shitposts.add(value);
        _modified = true;
    }


    /**
     * Clears the list of possible messages.
     */
    @Override
    public void clear() {
        _shitposts.clear();
    }

    /**
     * Gets a specific message.
     * @param index the index of the message array/
     * @return the message at the specified index.
     */
    @Override
    public String get(long index) {
        return _shitposts.get((int) index);
    }

    /**
     * Gets the instance of this class.
     * @return the instance of this class.
     */
    public static ShitpostFactory getInstance() {
        return _instance;
    }

    /**
     * Gets a random message.
     * @return a random message; null if list of messages is empty.
     */
    public String getRandom() {
        if (size() == 0)
            return null;
        return get(_random.nextInt(size()));
    }

    /**
     * Loads the list of messages.
     */
    @Override
    public void load() {
        File file = new File(SHITPOST_FILE_PATH);
        if (!file.getParentFile().exists())
            return;

        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine())
                add(scanner.nextLine());
        } catch (IOException ioe) {
            Log.getInstance().error(ioe, "ShitpostFactory");
        }
    }

    /**
     * Removes a specific message from the list.
     * @param index the index of the message to remove.
     * @return the message removed.
     */
    @Override
    public String remove(long index) {
        String post = get(index);
        _shitposts.remove((int) index);
        return post;
    }

    /**
     * Save the list of messages if it was modified.
     */
    @Override
    public void save() {
        if (!_modified)
            return;

        StringBuilder sb = new StringBuilder();
        for (String post : _shitposts) {
            sb.append(post).append("\n");
        }

        sb.deleteCharAt(sb.length() - 1);

        File file = new File(SHITPOST_FILE_PATH);
        if (!file.getParentFile().exists())
            file.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (IOException ioe) {
            Log.getInstance().error(ioe, "ShitpostFactory");
        }
    }

    public int size() {
        return _shitposts.size();
    }
}
