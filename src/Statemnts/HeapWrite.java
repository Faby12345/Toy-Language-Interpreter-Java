package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIDictionary;
import Model.MyIHeap;
import Model.PrgState;
import Types.RefType;
import Types.Type;
import Values.RefValue;
import Values.Value;

public class HeapWrite implements IStmt{
    private final String varName;
    private final Exp expression;

    public HeapWrite(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();


        if (!symTable.isDefined(varName)) {
            throw new MyException("wH: variable " + varName + " is not defined.");
        }


        Value varValue = symTable.lookup(varName);
        // verif if is reftype
        if (!(varValue instanceof RefValue)) {
            throw new MyException("wH: variable " + varName + " is not a RefValue.");
        }

        RefValue refValue = (RefValue) varValue;
        int addr = refValue.getAddr();


        if (!(refValue.getType() instanceof RefType)) {
            throw new MyException("wH: variable " + varName + " does not have RefType.");
        }

        RefType refType = (RefType) refValue.getType();
        Type innerType = refType.getInner();


        if (!heap.isDefined(addr)) {
            throw new MyException("wH: address " + addr + " is not defined in the heap.");
        }


        Value evaluated = expression.eval(symTable, heap);


        if (!evaluated.getType().equals(innerType)) {
            throw new RuntimeException(
                    "wH: type mismatch. Variable " + varName +
                            " has locationType " + innerType +
                            " but expression has type " + evaluated.getType()
            );
        }


        heap.update(addr, evaluated);

        return null;

    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeVar = typeEnv.lookup(varName);
        Type typeExp = expression.typeCheck(typeEnv);

        if (!(typeVar instanceof RefType)) {
            throw new MyException("WriteHeap: variable " + varName + " is not a ref type");
        }

        Type inner = ((RefType) typeVar).getInner();
        if (!inner.equals(typeExp)) {
            throw new MyException("WriteHeap: type of expression (" + typeExp +
                    ") does not match referenced type (" + inner + ")");
        }

        return typeEnv; // no changes to the environment
    }

        @Override
    public String toString() {
        return "wH(" + varName + ", " + expression.toString() + ")";
    }
}
