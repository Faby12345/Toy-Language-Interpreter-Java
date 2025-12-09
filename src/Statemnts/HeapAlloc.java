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

import javax.lang.model.util.Types;

public class HeapAlloc implements IStmt{
    private final String varName;
    private final Exp expression;
    public HeapAlloc(String var_name, Exp expresion){
        this.varName = var_name;
        this.expression = expresion;
    }

    @Override
    public PrgState execute(PrgState prg){
        MyIHeap prg_heap = prg.getHeap();
        MyIDictionary<String, Value> symTable = prg.getSymTable();
        if (!symTable.isDefined(varName)) {
            throw new MyException("Variable " + varName + " is not defined.");

        }
        Value varValue = symTable.lookup(varName);

        if (!(varValue.getType() instanceof RefType)) {
            throw new RuntimeException("Variable " + varName + " is not of RefType.");
        }
        Value evaluated = expression.eval(symTable, prg.getHeap());

        RefType varRefType = (RefType) varValue.getType();
        Type innerType = varRefType.getInner();

        if (!evaluated.getType().equals(innerType)) {
            throw new MyException(
                    "Type mismatch: variable " + varName +
                            " has type " + varRefType +
                            " but expression has type " + evaluated.getType()
            );
        }
        int newAddress = prg_heap.allocate(evaluated);
        symTable.update(varName, new RefValue(newAddress, innerType));
        return prg;
    }
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv){
        Type typeExp = expression.typeCheck(typeEnv);
        Type typeVar = typeEnv.lookup(varName);
        if(typeVar.equals(new RefType(typeExp))){
            return typeEnv;
        } else {
            throw new MyException("Type mismatch: variable " + varName + " has type " + typeVar + " but expression has type " + typeExp);
        }
    }
    @Override
    public String toString() {
        return "new(" + varName + ", " + expression.toString() + ")";
    }

}
