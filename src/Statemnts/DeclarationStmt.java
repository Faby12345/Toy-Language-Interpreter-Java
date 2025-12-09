package Statemnts;

import Exceptions.MyException;
import Model.MyIDictionary;
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
        if (!state.getSymTable().isDefined(id)) {
            Value def = type.defaultValue();      // 🔥 single line
            state.getSymTable().update(id, def);
        } else
            throw new MyException("the variable " + id + " was already declared");

        return state;
    }
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {
        typeEnv.update(id, type);
        return typeEnv;
    }

    @Override
    public String toString(){
        return type.toString() + " " + id;
    }
}
