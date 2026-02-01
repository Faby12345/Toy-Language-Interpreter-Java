package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Expresions.NotExp;
import Expresions.ValueExp;
import Expresions.VarExp;
import Model.MyIDictionary;
import Model.MyIHeap;
import Model.MyIStack;
import Model.PrgState;
import Types.BoolType;
import Types.Type;
import Values.BoolValue;
import Values.Value;

public class RepeatUntilStmt implements IStmt{
    private IStmt stmt;
    private Exp exp;
    public RepeatUntilStmt(IStmt stmt, Exp exp){
        this.stmt = stmt;
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state) {
        MyIStack<IStmt> stack = state.getStk();



        stack.push(
                new WhileStmt(
                        new NotExp(exp),
                        stmt
                )
        );
        stack.push(stmt);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeExp = exp.typeCheck(typeEnv);
        if (!typeExp.equals(new BoolType())) {
            throw new MyException("While: condition must be of type bool");
        }

        stmt.typeCheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public String toString(){
        return "Repeat{ " + this.stmt.toString() + "} Until(" + exp.toString() + ")";
    }
}
