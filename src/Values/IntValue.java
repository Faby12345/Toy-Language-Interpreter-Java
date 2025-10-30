package Values;


import Types.IntType;
import Types.Type;

public class IntValue implements Value {
    private final int value;
    public IntValue(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    @Override
    public Type getType() {
        return new IntType();
    }
    @Override
    public String toString() {
        return "" + value;
    }

}
