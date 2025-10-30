package Expresions;

import Model.MyIDictionary;
import Values.Value;

public class ValueExp implements Exp{
    Value e;
    public ValueExp(Value e){
        this.e = e;
    }
    @Override
    public Value eval(MyIDictionary<String,Value> tbl) {
        return e;
    }
    @Override
    public String toString(){
        return e.toString();
    }
}
