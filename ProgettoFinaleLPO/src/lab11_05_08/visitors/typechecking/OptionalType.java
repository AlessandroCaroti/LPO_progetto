package lab11_05_08.visitors.typechecking;

import static java.util.Objects.requireNonNull;
import static lab11_05_08.parser.TokenType.OPT;

public class OptionalType implements Type {
    private final Type elemType;
    private final boolean undefined;

    public static final String TYPE_NAME = "OPTIONAL";

    public OptionalType(Type elemType, boolean undefined) {
        this.elemType = requireNonNull(elemType);
        this.undefined = undefined;
    }

    public Type getElemType() {
        if(elemType instanceof OptionalType)
            return ((OptionalType) elemType).getElemType();
        return elemType;
    }

    @Override
    public boolean equals(Object obj) {
        return  this == obj ||
                obj instanceof OptionalType;
        /*
        OptionalType ot = (OptionalType) obj;
        if(undefined && ot.undefined)
            return true;
        return  this.elemType.equals(ot.elemType);
        /**/
    }

    @Override
    public String toString() {
        return elemType + " " + TYPE_NAME;
    }
}
