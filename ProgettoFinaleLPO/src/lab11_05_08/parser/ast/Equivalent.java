package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class Equivalent extends BinaryOp {

    public Equivalent(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitEquivalent(left, right);
    }
}

