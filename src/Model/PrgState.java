package Model;

import Statemnts.IStmt;
import Values.Value;

public class PrgState {
    private final MyIStack<IStmt> exeStack;
    private final MyIDictionary<String, Value> symTable; // ("c", 10), ("b", true)
    private final MyIList<Value> out;
    private final IStmt originalProgram;
    private final MyIFileTable fileTable;
    private final MyIHeap heap = new MyHeap();

    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String, Value> symTable, MyIList<Value> outList, IStmt originalProgram, MyIFileTable fileTable, MyIHeap heap) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = outList;
        this.originalProgram = originalProgram;
        this.fileTable = fileTable;
        this.heap.setMap(heap.getMap());
        exeStack.push(originalProgram);
    }
    public MyIStack<IStmt> getStk(){ return exeStack; }
    public MyIDictionary<String, Value> getSymTable(){ return symTable; }
    public MyIList<Value> getOut(){ return out; }
    public IStmt getOriginalProgram(){ return originalProgram; }
    public MyIFileTable getFileTable(){ return fileTable; }
    public MyIHeap getHeap(){ return heap; }

    @Override public String toString(){
        return "ExeStack=" + exeStack + System.lineSeparator() +
                "SymTable=" + symTable + System.lineSeparator() +
                "Out=" + out + System.lineSeparator() +
                "file table = " + fileTable.toString()+ System.lineSeparator() +
                "heap = " + heap.toString() + System.lineSeparator();
    }
}
