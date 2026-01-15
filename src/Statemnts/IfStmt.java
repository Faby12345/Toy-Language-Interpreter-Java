package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIDictionary;
import Model.MyIStack;
import Model.PrgState;
import Types.BoolType;
import Types.Type;
import Values.BoolValue;
import Values.Value;

public class IfStmt implements IStmt {
    private final Exp exp;
    private final IStmt thenS;
    private final IStmt elseS;
    public IfStmt(Exp exp, IStmt thenStmt, IStmt elseStmt){
        this.exp = exp;
        this.thenS = thenStmt;
        this.elseS = elseStmt;
    }

    @Override
    public PrgState execute(PrgState state){
        MyIStack<IStmt> stk = state.getStk();
        Value value = exp.eval(state.getSymTable(), state.getHeap());
        if(!value.getType().equals(new BoolType()))
            throw new MyException("if condition must be of type bool");

        boolean cond = ((BoolValue)value).getValue();
        if(cond) {
            stk.push(thenS);
        }
        else{
            stk.push(elseS);
        }
        return null;

    }
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {

        Type typeExp = exp.typeCheck(typeEnv);
        if(typeExp.equals(new BoolType())) {
            thenS.typeCheck(typeEnv.deepCopy());
            elseS.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else{
            throw new MyException("if condition must be of type bool");
        }
    }
    @Override public String toString(){ return "(IF("+exp+") THEN("+thenS+") ELSE("+elseS+"))"; }
}
