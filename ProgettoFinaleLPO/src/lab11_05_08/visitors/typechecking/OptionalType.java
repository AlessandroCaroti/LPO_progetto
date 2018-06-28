package lab11_05_08.visitors.typechecking;

import static java.util.Objects.requireNonNull;

public class OptionalType implements Type {
    private final Type elemType;

    public static final String TYPE_NAME = "OPTIONAL";

    public OptionalType(Type elemType) {
        this.elemType = requireNonNull(elemType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OptionalType))
            return false;
        OptionalType ot = (OptionalType) obj;
        return  this.elemType.equals(ot.elemType);
    }

    @Override
    public String toString() {
        return elemType + " " + TYPE_NAME;
    }
}
