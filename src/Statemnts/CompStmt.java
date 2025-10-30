package Statemnts;

import Model.MyIStack;
import Model.PrgState;
import Statemnts.IStmt;

public class CompStmt implements IStmt {
    IStmt first;
    IStmt second;
    public CompStmt(IStmt first, IStmt second){
        this.first = first;
        this.second = second;
    }
    @Override
    public String toString(){
        return first.toString() + ";" + second.toString();
    }
    @Override
    public PrgState execute(PrgState state){
        MyIStack<IStmt> stk = state.getStk();
        stk.push(second);
        stk.push(first);
        return state;
    }

}
