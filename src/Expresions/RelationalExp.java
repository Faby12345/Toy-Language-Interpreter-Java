package Expresions;

import Exceptions.MyException;
import Model.MyIDictionary;
import Model.MyIHeap;
import Types.BoolType;
import Types.IntType;
import Types.Type;
import Values.BoolValue;
import Values.IntValue;
import Values.Value;

import java.util.Objects;

public class RelationalExp implements Exp{
    Exp e1;
    Exp e2;
    String op;

    public RelationalExp(String gop, Exp ge1, Exp ge2) {
        e1 = ge1;
        e2 = ge2;
        op = gop;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> dict, MyIHeap heap){
        if (!(e1.eval(dict, heap) instanceof IntValue)) throw new MyException("The first expression is not an integer! ");
        if (!(e2.eval(dict, heap) instanceof IntValue)) throw new MyException("The second expression is not an integer! ");
        if (
                !Objects.equals(op, "<=") &&
                !Objects.equals(op, "<") &&
                !Objects.equals(op, "==") &&
                !Objects.equals(op, "!=") &&
                !Objects.equals(op, ">") &&
                !Objects.equals(op, ">=")
        )
            throw new MyException("Invalid operator! ");
        switch (op) {
            case "<=" -> {
                return new BoolValue(((IntValue) e1.eval(dict, heap)).getValue() <= ((IntValue) e2.eval(dict, heap)).getValue());
            }
            case "<" -> {
                return new BoolValue(((IntValue) e1.eval(dict, heap)).getValue() < ((IntValue) e2.eval(dict, heap)).getValue());
            }
            case "==" -> {
                return new BoolValue(((IntValue) e1.eval(dict, heap)).getValue() == ((IntValue) e2.eval(dict, heap)).getValue());
            }
            case "!=" -> {
                return new BoolValue(((IntValue) e1.eval(dict, heap)).getValue() != ((IntValue) e2.eval(dict, heap)).getValue());
            }
            case ">" -> {
                return new BoolValue(((IntValue) e1.eval(dict, heap)).getValue() > ((IntValue) e2.eval(dict, heap)).getValue());
            }
            case ">=" -> {
                return new BoolValue(((IntValue) e1.eval(dict, heap)).getValue() >= ((IntValue) e2.eval(dict, heap)).getValue());
            }
        }
        return null;
    }
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type t1 = e1.typeCheck(typeEnv);
        Type t2 = e2.typeCheck(typeEnv);

        if (!t1.equals(new IntType())) {
            throw new MyException("RelationalExp: first operand is not an int");
        }

        if (!t2.equals(new IntType())) {
            throw new MyException("RelationalExp: second operand is not an int");
        }


        return new BoolType();
    }


    public String toString() {
        return e1.toString() + op + e2.toString();
    }
}

