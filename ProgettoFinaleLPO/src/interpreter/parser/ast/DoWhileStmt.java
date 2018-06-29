package interpreter.parser.ast;

import interpreter.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class DoWhileStmt implements Stmt {
    private final StmtSeq block;
    private final Exp condition;

    public DoWhileStmt(StmtSeq block, Exp cond){
        this.block     = requireNonNull(block);
        this.condition = requireNonNull(cond);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + condition +  "," + block + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDoWhileStmt(condition, block);
    }
}
