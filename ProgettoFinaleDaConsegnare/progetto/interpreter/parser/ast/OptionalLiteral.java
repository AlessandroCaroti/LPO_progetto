package interpreter.parser.ast;

import interpreter.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class OptionalLiteral implements Exp {
    private final Exp exp;
    private final boolean undefined;

    public OptionalLiteral(Exp exp) {
        this.exp = requireNonNull(exp);
        undefined = false;
    }

    public OptionalLiteral(Exp exp, boolean undefined) {
        this.exp = requireNonNull(exp);
        this.undefined = undefined;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOptionalLiteral(exp, undefined);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + exp + ")";
    }

}
