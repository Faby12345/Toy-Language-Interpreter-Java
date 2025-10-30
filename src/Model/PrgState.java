package Model;

import Statemnts.IStmt;
import Values.Value;

public class PrgState {
    private final MyIStack<IStmt> exeStack;
    private final MyIDictionary<String, Value> symTable; // ("c", 10), ("b", true)
    private final MyIList<Value> out;
    private final IStmt originalProgram;

    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String, Value> symTable, MyIList<Value> outList, IStmt originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = outList;
        this.originalProgram = originalProgram;
        exeStack.push(originalProgram);
    }
    public MyIStack<IStmt> getStk(){ return exeStack; }
    public MyIDictionary<String, Value> getSymTable(){ return symTable; }
    public MyIList<Value> getOut(){ return out; }
    public IStmt getOriginalProgram(){ return originalProgram; }

    @Override public String toString(){
        return "ExeStack=" + exeStack + System.lineSeparator() +
                "SymTable=" + symTable + System.lineSeparator() +
                "Out=" + out + System.lineSeparator();
    }
}
