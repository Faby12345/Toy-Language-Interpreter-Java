package Model;

import java.util.List;

public interface IRepository {
    void setState(PrgState state);
    PrgState getState();
    void output();
}
