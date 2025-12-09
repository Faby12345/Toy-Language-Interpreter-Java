package Model;

import Values.StringValue;

import java.io.BufferedReader;
import java.util.Map;

public interface MyIFileTable {
    boolean isDefined(StringValue key);
    void put(StringValue key, BufferedReader br);
    BufferedReader get(StringValue key);
    void remove(StringValue key);
    Map<StringValue, BufferedReader> getContent();
    String toString();
}