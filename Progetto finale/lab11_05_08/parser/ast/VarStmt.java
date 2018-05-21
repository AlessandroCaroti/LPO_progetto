package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class VarStmt extends AbstractAssignStmt {

	public VarStmt(Ident ident, Exp exp) {
		super(ident, exp);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitVarStmt(ident, exp);
	}
}
