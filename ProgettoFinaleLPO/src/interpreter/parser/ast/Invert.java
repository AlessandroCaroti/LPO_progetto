package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Invert extends UnaryOp {

    public Invert(Exp exp) {
        super(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitNot(exp);
    }
}
