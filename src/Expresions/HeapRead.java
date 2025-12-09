package Expresions;

import Exceptions.MyException;
import Model.MyIDictionary;
import Model.MyIHeap;
import Types.RefType;
import Types.Type;
import Values.RefValue;
import Values.Value;

public class HeapRead implements Exp{
    private final Exp expression;
    public HeapRead(Exp expression){
        this.expression = expression;
    }
    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyIHeap hp){
        Value v = expression.eval(symTable, hp);
        if (!(v instanceof RefValue))
            throw new MyException("Reading Heap argument is not a RefValue");
        RefValue ref = (RefValue) v;
        int addr = ref.getAddr();

        if (!hp.isDefined(addr))
            throw new MyException("Address " + addr + " is not in the heap");
        return hp.lookup(addr);
    }

    public Types.Type typeCheck(MyIDictionary<String, Types.Type> typeEnv){
        Type typ = expression.typeCheck(typeEnv);
        if(typ instanceof RefType){
            RefType refTyp = (RefType) typ;
            return refTyp.getInner();
        }
        else{
            throw new MyException("Reading Heap argument is not a RefType");
        }
    }
    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}
