package Model;

import Exceptions.MyException;
import Exceptions.StackEmptyException;
import Statemnts.IStmt;
import Values.Value;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;
    private MyIFileTable fileTable;
    private MyIHeap heap; // Shared resource

    private IStmt originalProgram;
    private int id; // Unique ID for the thread
    private static int lastId = 0;

    // Synchronized method to ensure unique IDs in a concurrent environment
    public static synchronized int getNewId() {
        lastId++;
        return lastId;
    }

    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String, Value> symTable, MyIList<Value> out, MyIFileTable fileTable, MyIHeap heap, IStmt originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;


        this.heap = heap;

        this.originalProgram = originalProgram;
        this.id = getNewId(); // Assign unique ID on creation



        if (originalProgram != null) {
            exeStack.push(originalProgram);
        }
    }

    public MyIStack<IStmt> getStk() { return exeStack; }
    public MyIDictionary<String, Value> getSymTable() { return symTable; }
    public MyIList<Value> getOut() { return out; }
    public MyIFileTable getFileTable() { return fileTable; }
    public MyIHeap getHeap() { return heap; }
    public int getId() { return id; }

    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    // New version of oneStep moved from Controller
    public PrgState oneStep() throws MyException {
        if (exeStack.isEmpty()) {
            throw new MyException("prgstate stack is empty");
        }
        IStmt crtStmt = exeStack.pop();
        // The execute method now returns a PrgState (null or new thread)
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        // ID must be printed first
        return "Id: " + id + "\n" +
                "ExeStack:\n" + exeStack + "\n" +
                "SymTable:\n" + symTable + "\n" +
                "Out:\n" + out + "\n" +
                "FileTable:\n" + fileTable + "\n" +
                "Heap:\n" + heap + "\n" +
                "------------------------------------------------------";
    }
}