package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class Get extends UnaryOp {
    public Get(Exp exp) {
        super(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitGet(exp);
    }
}
