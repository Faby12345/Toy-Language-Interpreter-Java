package Model;

import Exceptions.StackEmptyException;
import Repsitory.IRepository;
import Statemnts.IStmt;

public class Controller {
    private final IRepository repository;

    public Controller(IRepository repository){
        this.repository = repository;
    }

    // Execute exactly one step
    public PrgState OneStep(PrgState state){
        MyIStack<IStmt> stack = state.getStk();
        if (stack.isEmpty()) {

            throw new StackEmptyException();
        }
        IStmt stmt = stack.pop();

        stmt.execute(state);
        return state;
    }


    public void allSteps(PrgState state){

        while (!state.getStk().isEmpty()) {
            state = OneStep(state);
            logSafe();                // post-state only
        }
    }

    private void logSafe() {
        try {
            repository.logPrgState();
        } catch (RuntimeException e1) {
            throw new RuntimeException("Logging failed", e1);
        }
    }

}
