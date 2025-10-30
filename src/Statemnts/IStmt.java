package Statemnts;

import Model.PrgState;

public interface IStmt {
    PrgState execute(PrgState state); // prg(stk, dic, out, OG), prg.stk.peek.execute(prg)
    String toString();
}
