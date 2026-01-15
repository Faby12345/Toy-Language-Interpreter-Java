package Statemnts;

import Exceptions.MyException;
import Model.*;
import Types.Type;
import Values.Value;

import java.util.Map;

public class ForkStmt implements IStmt{
    private final IStmt stmt;
    public ForkStmt(IStmt stmt){this.stmt = stmt;}

    @Override
    public PrgState execute(PrgState state) throws MyException {

        MyIStack<IStmt> newStack = new MyStack<>();
        MyIDictionary<String, Value> newSymTable = new MyDictionary<>();
        for (Map.Entry<String, Value> entry : state.getSymTable().getMap().entrySet()) {
            // Punem cheia și valoarea în noua tabelă
            newSymTable.update(entry.getKey(), entry.getValue().deepCopy());
        }

        return new PrgState(
                newStack,
                newSymTable,       // COPIE
                state.getOut(),    // ORIGINAL (Shared)
                state.getFileTable(), // ORIGINAL (Shared)
                state.getHeap(),   // ORIGINAL (Shared)
                this.stmt
        );
    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        this.stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + this.stmt.toString() + ")";
    }
}
