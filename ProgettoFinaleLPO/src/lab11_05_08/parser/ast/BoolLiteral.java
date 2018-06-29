package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class BoolLiteral extends PrimLiteral<Boolean> {

    public BoolLiteral(boolean n) {
        super(n);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBoolLiteral(value);
    }
}
