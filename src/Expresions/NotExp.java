package Expresions;

import Exceptions.MyException;
import Model.MyIDictionary;
import Model.MyIHeap;
import Types.BoolType;
import Types.Type;
import Values.BoolValue;
import Values.Value;

public class NotExp implements Exp {
    private final Exp exp;

    public NotExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) {
        BoolValue v = (BoolValue) exp.eval(tbl, heap);
        return new BoolValue(!v.getValue());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) {
        if (!exp.typeCheck(typeEnv).equals(new BoolType()))
            throw new MyException("Not: operand not bool");
        return new BoolType();
    }

    @Override
    public String toString() {
        return "!(" + exp.toString() + ")";
    }
}
