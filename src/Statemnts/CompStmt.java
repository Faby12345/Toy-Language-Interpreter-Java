package Statemnts;

import Model.MyIDictionary;
import Model.MyIStack;
import Model.PrgState;
import Statemnts.IStmt;
import Types.Type;

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

    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv){
        MyIDictionary<String, Type> envAfterFirst = first.typeCheck(typeEnv);

        MyIDictionary<String, Type> envAfterSecond = second.typeCheck(envAfterFirst);

        return envAfterSecond;
    }
    @Override
    public PrgState execute(PrgState state){
        MyIStack<IStmt> stk = state.getStk();
        stk.push(second);
        stk.push(first);
        return state;
    }

}
