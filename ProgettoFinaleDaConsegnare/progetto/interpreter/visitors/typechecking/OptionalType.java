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
        if(this == obj)
            return true;
        if(!(obj instanceof OptionalType))
            return false;
        OptionalType ot = (OptionalType) obj;
        Type t1, t2;
        t1 = (this.undefined) ? this.elemType.getOptElemType() : this.elemType;
        t2 = (ot.undefined)   ? ot.elemType.getOptElemType()   : ot.elemType;
        System.out.println(this.elemType+" - "+ot.elemType);
        System.out.println(this.undefined+" - "+ot.undefined);
        System.out.println(t1+" - "+t2);
        return t1.equals(t2);
        
    }

    @Override
    public String toString() {
        return (undefined) ? ("EMPTY VALUE") : (elemType + " " + TYPE_NAME);
    }
}
