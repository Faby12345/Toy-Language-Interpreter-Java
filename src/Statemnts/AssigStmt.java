package Statemnts;

import Expresions.Exp;
import Model.MyIDictionary;
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
        if(symTable.isDefined(id)){ // if the variable was declared
           Value value = exp.eval(symTable);
           Type typeId = (symTable.lookup(id)).getType();
           if(value.getType().equals(typeId)){
               symTable.update(id, value);
           }
           else
               throw new RuntimeException("declared type of variable " + id + " and type of the assigned expression do not match");

        } else throw new RuntimeException("the used variable " + id + " was not declared before");

        return state;
    }
    @Override
    public String toString(){
        return id+"="+ exp.toString();
    }
}
