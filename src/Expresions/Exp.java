package Expresions;

import Model.MyIDictionary;
import Model.MyIHeap;
import Types.Type;
import Values.Value;

public interface Exp {
    Value eval(MyIDictionary<String, Value> symTable, MyIHeap hp);
    Type typeCheck(MyIDictionary<String, Type> typeEnv);
    String toString();
}
