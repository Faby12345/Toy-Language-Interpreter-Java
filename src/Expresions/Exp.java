package Expresions;

import Model.MyIDictionary;
import Values.Value;

public interface Exp {
    Value eval(MyIDictionary<String, Value> symTable);
    String toString();
}
