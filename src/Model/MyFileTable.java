package Model;

import Values.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MyFileTable implements MyIFileTable {
    private final Map<StringValue, BufferedReader> map = new HashMap<>();
    @Override public boolean isDefined(StringValue key){ return map.containsKey(key); }
    @Override public void put(StringValue key, BufferedReader br){ map.put(key, br); }
    @Override public BufferedReader get(StringValue key){ return map.get(key); }
    @Override public void remove(StringValue key){ map.remove(key); }
    @Override public Map<StringValue, BufferedReader> getContent(){ return map; }
}