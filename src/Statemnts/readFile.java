package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIFileTable;
import Model.PrgState;
import Types.IntType;
import Types.StringType;
import Values.IntValue;
import Values.StringValue;
import Values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class readFile implements IStmt{
    private final Exp exp; private final String varName;
    public readFile(Exp e, String v){ exp=e; varName=v; }

    @Override public PrgState execute(PrgState state)  {
        // var defined and int
        if(!state.getSymTable().isDefined(varName))
            throw new MyException("readFile: var not declared: " + varName);
        Value old = state.getSymTable().lookup(varName);
        if(!(old.getType() instanceof IntType))
            throw new MyException("readFile: var not int: " + varName);

        // exp to string
        Value v = exp.eval(state.getSymTable());
        if(!(v.getType() instanceof StringType))
            throw new MyException("readFile: exp not string");
        StringValue sv = (StringValue) v;

        // file handle
        MyIFileTable ft = state.getFileTable();
        BufferedReader br = ft.get(sv);
        if(br == null) throw new MyException("readFile: file not opened: " + sv.GetValue());

        // read
        try {
            String line = br.readLine();
            int value = (line == null || line.isEmpty()) ? 0 : Integer.parseInt(line.trim());
            state.getSymTable().update(varName, new IntValue(value));
        } catch (IOException e) {
            throw new MyException("readFile I/O: " + e.getMessage());
        } catch (NumberFormatException nfe) {
            throw new MyException("readFile: non-integer line in file.");
        }
        return state;
    }
    @Override public String toString(){ return "readFile("+exp+","+varName+")"; }
}
