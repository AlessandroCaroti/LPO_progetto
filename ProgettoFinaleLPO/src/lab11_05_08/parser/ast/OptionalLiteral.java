package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class OptionalLiteral implements Exp {
    private final Exp exp;

    public OptionalLiteral(Exp exp) {
        this.exp = requireNonNull(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOptionalLiteral(exp);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + exp + ")";
    }
}
