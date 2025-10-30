package Statemnts;

import Expresions.Exp;
import Model.PrgState;
import Values.Value;

public class PrintStmt implements IStmt {
    private final Exp exp;
    public PrintStmt(Exp exp){
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state){
        Value value = exp.eval(state.getSymTable());
        state.getOut().add(value);
        return state;
    }
    @Override
    public String toString(){
        return "Print(" + exp + ")";
    }



}
