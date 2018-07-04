package interpreter.visitors.typechecking;

import static java.util.Objects.requireNonNull;

public class OptionalType implements Type {
    private final Type elemType;
    private final boolean undefined;

    public static final String TYPE_NAME = "OPTIONAL";

    public OptionalType(Type elemType, boolean undefined) {
        this.elemType = requireNonNull(elemType);
        this.undefined = undefined;
    }

    public Type getElemType() {
        return elemType;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof OptionalType;
        /*
        if(this == obj)
            return true;
        if(!(obj instanceof OptionalType))
            return false;
        OptionalType ot = (OptionalType) obj;
        if(undefined && ot.undefined)
            return true;
        if(undefined || ot.undefined)
            return false;
        return  this.elemType.equals(ot.elemType);
        */
    }

    @Override
    public String toString() {
        return (undefined) ? ("EMPTY VALUE") : (elemType + " " + TYPE_NAME);
    }
}
