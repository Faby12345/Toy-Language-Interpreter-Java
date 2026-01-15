package Model;

import java.util.List;

public interface MyIStack <T>{
    T pop();
    void push(T item);
    boolean isEmpty();
    int size();
    T peek();
    String toString();
    List<T> getReversed();
}
