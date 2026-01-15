package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIDictionary;
import Model.MyIFileTable;
import Model.PrgState;
import Types.StringType;
import Types.Type;
import Values.StringValue;
import Values.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class openRFile implements IStmt{
    private final Exp exp;
    public openRFile(Exp e){ exp=e; }
    @Override public PrgState execute(PrgState state) {
        Value v = exp.eval(state.getSymTable(), state.getHeap()); // or your A2 eval signature
        if(!(v.getType() instanceof StringType))
            throw new MyException("openRFile: exp not string");

        StringValue sv = (StringValue) v;
        MyIFileTable ft = state.getFileTable();
        if(ft.isDefined(sv)) throw new MyException("File already opened: " + sv.GetValue());

        try {
            BufferedReader br = new BufferedReader(new FileReader(sv.GetValue()));
            ft.put(sv, br);
        } catch (IOException e) {
            throw new MyException("openRFile I/O: " + e.getMessage());
        }
        return null;

    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeExp = exp.typeCheck(typeEnv);

        if (typeExp.equals(new StringType())) {
            return typeEnv;   // no changes to the environment
        } else {
            throw new MyException("OpenRFile requires a string expression");
        }
    }

    @Override public String toString(){ return "openRFile("+exp+")"; }
}
