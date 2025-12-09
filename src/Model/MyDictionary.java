package Model;

import java.util.HashMap;
import java.util.Map;


public class MyDictionary<String, V> implements MyIDictionary<String,V> {
    private final Map<String, V> dictionary = new HashMap<>();
    @Override
    public boolean isDefined(String key) {
        return dictionary.containsKey(key);
    }
    @Override
    public void update(String key, V value) {
        dictionary.put(key, value);
    }
    @Override
    public V lookup(String key) {
        return dictionary.get(key);
    }

    @Override
    public Map<String, V> getMap() {
        return dictionary;
    }
    @Override
    public java.lang.String toString() {
        return dictionary.toString();
    }
    @Override
    public MyIDictionary<String, V> deepCopy() {
        MyDictionary<String, V> newDict = new MyDictionary<>();
        for (String key : dictionary.keySet()) {
            newDict.update(key, dictionary.get(key));
        }
        return newDict;
    }
}
