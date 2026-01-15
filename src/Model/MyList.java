package Model;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    private final List<T> list = new ArrayList<>();
    @Override
    public void add(T item) {
        list.add(item);
    }
    @Override
    public String toString() {
        return list.toString();
    }
    @Override
    public List<T> getList() {
        return list;
    }
}
