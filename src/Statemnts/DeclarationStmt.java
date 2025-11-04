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
            } else if(type.equals(new Types.BoolType())){
                def = new Values.BoolValue(false);
            } else{
                def = new Values.StringValue("");
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
