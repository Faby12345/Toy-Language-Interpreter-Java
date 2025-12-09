package Expresions;

import Exceptions.MyException;
import Model.MyIDictionary;
import Model.MyIHeap;
import Types.IntType;
import Types.Type;
import Values.IntValue;
import Values.Value;

public class ArithExp implements Exp{
    Exp e1, e2;
    int op;
    public ArithExp(int op, Exp e1, Exp e2){
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }
    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyIHeap heap){
        Value v1,v2;
        v1 = e1.eval(symTable, heap);
        if(v1.getType().equals(new Types.IntType())){
            v2 = e2.eval(symTable, heap);
            if(v2.getType().equals(new Types.IntType())){
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                switch (op){
                    case 1: return new IntValue(i1.getValue() + i2.getValue());
                    case 2: return new IntValue(i1.getValue() - i2.getValue());
                    case 3: return new IntValue(i1.getValue() * i2.getValue());
                    case 4:
                        if(i2.getValue() != 0)
                            return new IntValue(i1.getValue() / i2.getValue());
                        else
                            throw new MyException("division by zero is not allowed");
                    default: throw new MyException("invalid operator");
                }
            }
            else {
                throw new MyException("The second operand is not an integer");
            }
        }
        else {
            throw new MyException("The first operand is not an integer");
        }
    }
    private String TranslateOpForPrint(){
        if (op == 1) return "+";
        else if (op == 2) return "-";
        else if (op == 3) return "*";
        else if (op == 4) return "/";
        else return "invalid operator";
    }
    public Types.Type typeCheck(MyIDictionary<String, Types.Type> typeEnv){
       Type t1, t2;
       t1 = e1.typeCheck(typeEnv);
       t2 = e2.typeCheck(typeEnv);
       if(t1.equals(new IntType())){
           if(t2.equals(new IntType())){
               return new IntType();
           }
           else{
               throw new MyException("The second operand is not an integer");
           }
       }
       else{
           throw new MyException("The first operand is not an integer");
       }

    }
    @Override
    public String toString(){
        return "(" + e1 + " " + TranslateOpForPrint() + " " + e2 + ")";
    }
}
