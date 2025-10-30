package Model;

import java.util.Map;

public interface MyIDictionary <String,V>{
    void update(String key, V value);
    boolean isDefined(String key);
    V lookup(String key);
    //String toString();
    Map<String, V> getMap();
    java.lang.String toString();
}
