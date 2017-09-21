package tools.factories;

/**
 * @author Tom
 */
public interface IFactory<T> {
    void add(T value);
    void clear();
    T get(long id);
    void load();
    T remove(long id);
    void save();
}
