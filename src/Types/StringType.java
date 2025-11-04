package Types;

public class StringType implements Type{
    @Override
    public boolean equals(Object another){
        return another instanceof StringType;
    }
    @Override
    public String toString(){
        return "string";
    }
}
