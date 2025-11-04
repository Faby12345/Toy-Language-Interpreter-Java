package Statemnts;

import Expresions.Exp;
import Model.MyIFileTable;
import Model.PrgState;
import Types.StringType;
import Values.StringValue;
import Values.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class openRFile implements IStmt{
    private final Exp exp;
    public openRFile(Exp e){ exp=e; }
    @Override public PrgState execute(PrgState state) {
        Value v = exp.eval(state.getSymTable()); // or your A2 eval signature
        if(!(v.getType() instanceof StringType))
            throw new RuntimeException("openRFile: exp not string");

        StringValue sv = (StringValue) v;
        MyIFileTable ft = state.getFileTable();
        if(ft.isDefined(sv)) throw new RuntimeException("File already opened: " + sv.GetValue());

        try {
            BufferedReader br = new BufferedReader(new FileReader(sv.GetValue()));
            ft.put(sv, br);
        } catch (IOException e) {
            throw new RuntimeException("openRFile I/O: " + e.getMessage(), e);
        }
        return state;
    }
    @Override public String toString(){ return "openRFile("+exp+")"; }
}
