package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIDictionary;
import Model.MyIHeap;
import Model.MyIStack;
import Model.PrgState;
import Types.BoolType;
import Types.Type;
import Values.BoolValue;
import Values.Value;

public class WhileStmt implements IStmt {
    private final Exp expression;
    private final IStmt statement;

    public WhileStmt(Exp expression, IStmt statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIStack<IStmt> stack = state.getStk();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        Value cond = expression.eval(symTable, heap);

        if (!cond.getType().equals(new BoolType())) {
            throw new MyException("While: condition is not boolean");
        }

        BoolValue bVal = (BoolValue) cond;

        if (bVal.getValue()) {

            stack.push(this);
            stack.push(statement);
        }

        return state;
    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeExp = expression.typeCheck(typeEnv);
        if (!typeExp.equals(new BoolType())) {
            throw new MyException("While: condition must be of type bool");
        }

        statement.typeCheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "while(" + expression.toString() + ") " + statement.toString();
    }


}
