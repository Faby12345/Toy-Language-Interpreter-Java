package Values;

import Types.RefType;
import Types.Type;

public class RefValue implements Value {
    private final int address;
    private final Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    // adresa de pe heap
    public int getAddr() {
        return address;
    }

    // tipul locației de la adresa respectivă
    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        // valoarea are tipul Ref(locationType)
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "Ref(" + address + ", " + locationType + ")";
    }
}
