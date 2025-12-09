package Expresions;

import Model.MyIDictionary;
import Model.MyIHeap;
import Types.Type;
import Values.Value;

public class ValueExp implements Exp{
    Value e;
    public ValueExp(Value e){
        this.e = e;
    }
    @Override
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap heap) {
        return e;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) {
        return e.getType();
    }

    @Override
    public String toString(){
        return e.toString();
    }
}
