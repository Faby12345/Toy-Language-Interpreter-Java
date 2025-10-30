package Statemnts;

import Model.PrgState;
import Types.Type;
import Values.Value;

public class DeclarationStmt implements IStmt {
    private final String id;
    private final Type type;
    public DeclarationStmt(String id, Type type){
        this.id = id;
        this.type = type;
    }
    @Override
    public PrgState execute(PrgState state){
        if(!state.getSymTable().isDefined(id)) {
            Value def;
            if (type.equals(new Types.IntType())) {
                def = new Values.IntValue(0);
            } else {
                def = new Values.BoolValue(false);
            }
            state.getSymTable().update(id, def);
        }
        else throw new RuntimeException("the variable " + id + " was already declared");

        return state;
    }
    @Override
    public String toString(){
        return type.toString() + " " + id;
    }
}
