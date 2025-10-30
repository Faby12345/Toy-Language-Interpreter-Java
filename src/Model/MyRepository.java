package Model;

import java.util.List;

public class MyRepository implements IRepository{
    private PrgState state;
    public MyRepository(PrgState state){
        this.state = state;
    }
    @Override
    public void setState(PrgState state) {
        this.state = state;
    }
    @Override
    public PrgState getState() {
        return state;
    }
    @Override
    public void output() {
        System.out.println(state);
    }
}
