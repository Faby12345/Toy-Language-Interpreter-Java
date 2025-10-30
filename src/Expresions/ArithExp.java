package Expresions;

import Model.MyIDictionary;
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
    public Value eval(MyIDictionary<String, Value> symTable){
        Value v1,v2;
        v1 = e1.eval(symTable);
        if(v1.getType().equals(new Types.IntType())){
            v2 = e2.eval(symTable);
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
                            throw new RuntimeException("division by zero is not allowed");
                    default: throw new RuntimeException("invalid operator");
                }
            }
            else {
                throw new RuntimeException("The second operand is not an integer");
            }
        }
        else {
            throw new RuntimeException("The first operand is not an integer");
        }
    }
    private String TranslateOpForPrint(){
        if (op == 1) return "+";
        else if (op == 2) return "-";
        else if (op == 3) return "*";
        else if (op == 4) return "/";
        else return "invalid operator";
    }
    @Override
    public String toString(){
        return "(" + e1 + " " + TranslateOpForPrint() + " " + e2 + ")";
    }
}
