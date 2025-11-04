package Repsitory;

import Model.PrgState;

public interface IRepository {
    void addState(PrgState state);
    PrgState  getState();
    void logPrgState();
    void output();

}
//test 2