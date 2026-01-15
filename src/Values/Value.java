package Values;

import Types.Type;

public interface Value {
    Type getType();
    String toString();
    Value deepCopy();
}
