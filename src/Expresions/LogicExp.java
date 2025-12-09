package Expresions;

import Exceptions.MyException;
import Model.MyIDictionary;
import Model.MyIHeap;
import Types.BoolType;
import Types.Type;
import Values.BoolValue;
import Values.Value;
import com.sun.jdi.BooleanValue;

public class LogicExp implements Exp {
    Exp e1, e2;
    int op; // 1 - AND, 2 - OR
    public LogicExp(int op, Exp e1, Exp e2){
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }
    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyIHeap heap) {
        Value b1 = e1.eval(symTable, heap);
        if(b1.getType().equals(new BoolType())){
            Value b2 = e2.eval(symTable, heap);
            if(b2.getType().equals(new BoolType())){
                BooleanValue bv1 = (BooleanValue) b1;
                BooleanValue bv2 = (BooleanValue) b2;
                switch (op){
                    case 1: return new BoolValue(bv1.value() && bv2.value());
                    case 2: return new BoolValue(bv1.value() || bv2.value());
                    default: throw new MyException("invalid operator");
                }
            } else {
                throw new MyException("The second operand is not a boolean");
            }

        } else{
            throw new MyException("The first operand is not a boolean");
        }
    }
    public Types.Type typeCheck(MyIDictionary<String, Types.Type> typeEnv){
        Type t1, t2;
        t1 = e1.typeCheck(typeEnv);
        t2 = e2.typeCheck(typeEnv);
        if(t1.equals(new BoolType())){
            if(t2.equals(new BoolType())){
                return new BoolType();
            } else {
                throw new MyException("The second operand is not a boolean");
            }
        } else {
            throw new MyException("The first operand is not a boolean");
        }
    }
    @Override
    public String toString(){
        return "(" + e1 + " " + (op == 1 ? "AND" : "OR") + " " + e2 + ")";
    }
}
