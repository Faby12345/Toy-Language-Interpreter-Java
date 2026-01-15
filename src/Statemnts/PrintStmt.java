package Statemnts;

import Expresions.Exp;
import Model.MyIDictionary;
import Model.PrgState;
import Types.Type;
import Values.Value;

public class PrintStmt implements IStmt {
    private final Exp exp;
    public PrintStmt(Exp exp){
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state){
        Value value = exp.eval(state.getSymTable(), state.getHeap());
        state.getOut().add(value);
        return null;
    }
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

        @Override
    public String toString(){
        return "Print(" + exp + ")";
    }



}
