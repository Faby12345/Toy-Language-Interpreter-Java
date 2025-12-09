package Types;

import Values.RefValue;
import Values.Value;

public class RefType implements Type {
    private final Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof RefType)) return false;
        RefType other = (RefType) another;
        return inner.equals(other.inner);
    }

    @Override
    public String toString() {
        return "Ref(" + inner + ")";
    }

    @Override
    public Value defaultValue() {
        // address 0 = "null" / invalid address
        return new RefValue(0, inner);
    }
}
