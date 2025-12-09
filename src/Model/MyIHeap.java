package Model;

import Values.Value;
import java.util.Map;

public interface MyIHeap {
    boolean isDefined(Integer key);
    void update(Integer key, Value value);
    Value lookup(Integer key);
    Map<Integer, Value> getMap();
    void setMap(Map<Integer, Value> newMap);
    int allocate(Value value);
    String toString();
}
