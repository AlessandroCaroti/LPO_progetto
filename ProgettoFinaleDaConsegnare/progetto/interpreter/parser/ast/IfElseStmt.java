package interpreter.parser.ast;

import interpreter.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class IfElseStmt implements Stmt {
    private final StmtSeq ifBlock;
    private final StmtSeq elseBlock;
    private final Exp condition;

    public IfElseStmt(Exp cond, StmtSeq block1){
        condition = requireNonNull(cond);
        ifBlock   = requireNonNull(block1);
        elseBlock = null;
    }

    public IfElseStmt(Exp cond, StmtSeq block1, StmtSeq block2){
        condition = requireNonNull(cond);
        ifBlock   = requireNonNull(block1);
        elseBlock = requireNonNull(block2);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitIfElseStmt(condition, ifBlock, elseBlock);
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + condition +  "," + ifBlock + ")";
    }
}
