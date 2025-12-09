package Model;

import Values.Value;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MyHeap implements MyIHeap {
    private final Map<Integer, Value> dictionary = new HashMap<>();
    private int freeLocation = 1;


    @Override
    public boolean isDefined(Integer key) {
        return dictionary.containsKey(key);
    }

    @Override
    public void update(Integer key, Value value) {
        dictionary.put(key, value);
    }

    @Override
    public Value lookup(Integer key) {
        return dictionary.get(key);
    }

    @Override
    public Map<Integer, Value> getMap() {
        return dictionary;
    }

    @Override
    public void setMap(Map<Integer, Value> newMap) {
        dictionary.clear();
        dictionary.putAll(newMap);
    }

    @Override
    public int allocate(Value value) {
        dictionary.put(freeLocation, value);
        return freeLocation++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<Integer, Value> entry : dictionary.entrySet()) {
            sb.append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue().toString())
                    .append(", ");

        }
        sb.append("}");
        return sb.toString();
    }
}

