package Values;

import Types.StringType;
import Types.Type;

public class StringValue implements Value{
    private final String value;
    public StringValue(String value){
        this.value = value;
    }
    public String GetValue(){
        return value;
    }
    @Override
    public Type getType(){
        return new StringType();
    }
    @Override
    public String toString() {
        return value;
    }


}
