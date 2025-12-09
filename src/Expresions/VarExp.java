package Expresions;

import Exceptions.MyException;
import Model.MyHeap;
import Model.MyIDictionary;
import Model.MyIHeap;
import Types.Type;
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
    public Type typeCheck(MyIDictionary<String, Type> typeEnv){
        try{
            return typeEnv.lookup(id);
        } catch (MyException e){
            throw new MyException("Variable " + id + " is not defined");
        }


    }
    public Value eval(MyIDictionary<String, Value> symTable, MyIHeap heap){
        return symTable.lookup(id);
    }
}
