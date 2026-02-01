package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIDictionary;
import Model.PrgState;
import Types.BoolType;
import Types.Type;
import Values.BoolValue;
import Values.Value;

public class CondAssigStmt implements IStmt{
    private final Exp exp1;
    private final Exp exp2;
    private final Exp exp3;
    private final String varName;
    public CondAssigStmt(String VarName, Exp exp1, Exp exp2, Exp exp3){
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.varName = VarName;
    }
    public PrgState execute(PrgState state){
        MyIDictionary<String, Value> symTable = state.getSymTable();
        if (!symTable.isDefined(varName)) {
            throw new MyException("CondAssign: variable not defined");
        }
        Value valueExp1 = exp1.eval(symTable, state.getHeap());
        Value valueExp2 = exp2.eval(symTable, state.getHeap());
        Value valueExp3 = exp3.eval(symTable, state.getHeap());
        if (!valueExp1.getType().equals(new BoolType())) {
            throw new MyException("CondAssign: condition is not boolean");
        }
        BoolValue bValueExp1 = (BoolValue) valueExp1;
        if(bValueExp1.getValue()){
            symTable.update(varName, valueExp2);
        } else {
            symTable.update(varName, valueExp3);
        }
        return null;
    }
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeExp1 = exp1.typeCheck(typeEnv);
        Type typeExp2 = exp2.typeCheck(typeEnv);
        Type typeExp3 = exp3.typeCheck(typeEnv);
        if(typeExp1.equals(new BoolType()) && typeExp2.equals(typeExp3)) {
            return typeEnv;
        } else {
            throw new MyException("CondAssig: right hand side and left hand side have different types");
        }
    }
    @Override
    public String toString(){
        return "("+exp1.toString()+")? "+exp2.toString()+" : "+exp3.toString();
    }

}
