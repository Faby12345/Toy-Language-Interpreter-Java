package Statemnts;

import Exceptions.MyException;
import Expresions.Exp;
import Model.MyIDictionary;
import Model.MyIHeap;
import Model.MyIStack;
import Model.PrgState;
import Types.Type;
import Values.Value;

public class AssigStmt implements IStmt {
    private final String id;
    private final Exp exp;
    public AssigStmt(String id, Exp exp){
        this.id = id;
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state){
        MyIStack<IStmt> stk = state.getStk();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        if(symTable.isDefined(id)){ // if the variable was declared
           Value value = exp.eval(symTable, heap);
           Type typeId = (symTable.lookup(id)).getType();
           if(value.getType().equals(typeId)){
               symTable.update(id, value);
           }
           else
               throw new MyException("declared type of variable " + id + " and type of the assigned expression do not match");

        } else throw new MyException("the used variable " + id + " was not declared before");

        return null;
    }
    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        Type typeVar = typeEnv.lookup(id);           // declared type of variable
        Type typeExp = exp.typeCheck(typeEnv);       // type of expression

        if (typeVar.equals(typeExp)) {
            return typeEnv;
        } else {
            throw new MyException("Assigmant: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString(){
        return id+"="+ exp.toString();
    }
}
