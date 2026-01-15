package Model;

import java.util.List;

public interface MyIList<T> {
    void add(T item);
    String toString();
    List<T> getList();
}
