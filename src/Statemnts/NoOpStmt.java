package Statemnts;

import Model.PrgState;

public class NoOpStmt implements IStmt{
    @Override
    public String toString(){
        return "NoOp";
    }
    @Override
    public PrgState execute(PrgState state){
        return state;
    }
}
