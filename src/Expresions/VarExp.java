package Expresions;

import Model.MyIDictionary;
import Values.Value;

public class VarExp implements Exp{
    String id;
    public VarExp(String id){
        this.id = id;
    }
    @Override
    public String toString(){
        return id;
    }
    public Value eval(MyIDictionary<String, Value> symTable){
        return symTable.lookup(id);
    }
}
