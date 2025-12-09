package Statemnts;

import Model.MyIDictionary;
import Model.PrgState;
import Types.Type;

public interface IStmt {
    PrgState execute(PrgState state); // prg(stk, dic, out, OG), prg.stk.peek.execute(prg)
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv);
    String toString();
}
