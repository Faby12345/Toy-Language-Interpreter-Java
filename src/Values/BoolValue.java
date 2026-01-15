package Values;

import Types.BoolType;
import Types.Type;

public class BoolValue implements Value{
    private final boolean value;
    public BoolValue(boolean value){
        this.value = value;
    }
    public boolean getValue(){
        return value;
    }
    @Override
    public Type getType() {
        return new BoolType();
    }
    @Override
    public String toString() {
        return "" + value;
    }

    public Value deepCopy(){
        return new BoolValue(value);
    }
}
