package Statemnts;

import Model.MyIDictionary;
import Model.PrgState;
import Types.Type;

public class NoOpStmt implements IStmt{
    @Override
    public String toString(){
        return "NoOp";
    }
    @Override
    public PrgState execute(PrgState state){
        return state;
    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        return typeEnv;
    }
}
