package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class Invert extends UnaryOp {

    public Invert(Exp exp) {
        super(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitNot(exp);
    }
}
