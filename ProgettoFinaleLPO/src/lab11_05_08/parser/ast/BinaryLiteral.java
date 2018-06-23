package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class BinaryLiteral extends PrimLiteral<Integer> {

    public BinaryLiteral(int n) {
        super(n);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBinaryLiteral(value);
    }
}
