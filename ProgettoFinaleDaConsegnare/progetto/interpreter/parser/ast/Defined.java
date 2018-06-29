package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Defined extends UnaryOp {
    public Defined(Exp exp) {
        super(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDefined(exp);
    }
}
