package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Equivalent extends BinaryOp {

    public Equivalent(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitEquivalent(left, right);
    }
}

