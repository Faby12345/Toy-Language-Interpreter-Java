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
import java.io.IOException;

public class closeRFile implements IStmt{

    private final Exp exp;
    public closeRFile(Exp e){ exp=e; }


    @Override public PrgState execute(PrgState state) throws MyException {
        Value v = exp.eval(state.getSymTable(), state.getHeap());
        if(!(v.getType() instanceof StringType))
            throw new MyException("closeRFile: exp not string");
        StringValue sv = (StringValue) v;

        MyIFileTable ft = state.getFileTable();
        BufferedReader br = ft.get(sv);
        if(br == null) throw new MyException("closeRFile: file not opened: " + sv.GetValue());

        try { br.close(); } catch (IOException e) {
            throw new MyException("closeRFile I/O: " + e.getMessage());
        }
        ft.remove(sv);
        return state;
    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeExp = exp.typeCheck(typeEnv);

        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new MyException("CloseRFile requires a string expression");
        }
    }

    @Override public String toString(){ return "closeRFile("+exp+")"; }
}
