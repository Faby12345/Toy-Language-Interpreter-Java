package Model;

import Exceptions.StackEmptyException;
import Statemnts.IStmt;

public class Controller {
    private final IRepository repository;
    public Controller(IRepository repository){
        this.repository = repository;
    }


    public PrgState OneStep(PrgState state){
       MyIStack stack = state.getStk();
       if(stack.isEmpty()) {
           System.out.println("Program output: " + state.getOut());
           throw new StackEmptyException();
       }
        IStmt stmt = (IStmt) stack.pop();
        repository.output();
        return stmt.execute(state);
    }


    public void allSteps(PrgState state){
        repository.output();
        while(!state.getStk().isEmpty()){
            state = OneStep(state);
            repository.output();
        }
        System.out.println("Program output: " + state.getOut());
    }
}
