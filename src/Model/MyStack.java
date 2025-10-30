package Model;

import Exceptions.StackEmptyException;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> implements MyIStack<T>{
    private final Deque<T> stack = new ArrayDeque<>();;

    @Override
    public T pop(){
        if(stack.isEmpty()) throw new StackEmptyException();
        return stack.pop();
    }
    @Override
    public boolean isEmpty(){
        return stack.isEmpty();
    }
    @Override
    public int size(){
        return stack.size();
    }
    @Override
    public T peek(){
        if(stack.isEmpty()) throw new StackEmptyException();
        return stack.peek();
    }
    @Override
    public void push(T item){
        stack.push(item);
    }
    @Override
    public String toString(){
        return stack.toString();
    }
}
