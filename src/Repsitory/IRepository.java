package Repsitory;

import Model.PrgState;

import java.util.List;

public interface IRepository {
    void addState(PrgState state);
    void logPrgState(PrgState state);
    //void output();
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> list);

}
//test 2