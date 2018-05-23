package lab11_05_08.visitors.typechecking;

import static lab11_05_08.visitors.typechecking.PrimtType.*;

import lab11_05_08.environments.EnvironmentException;
import lab11_05_08.environments.GenEnvironment;
import lab11_05_08.parser.ast.Exp;
import lab11_05_08.parser.ast.ExpSeq;
import lab11_05_08.parser.ast.Ident;
import lab11_05_08.parser.ast.SimpleIdent;
import lab11_05_08.parser.ast.Stmt;
import lab11_05_08.parser.ast.StmtSeq;
import lab11_05_08.visitors.Visitor;

public class TypeCheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	private void checkBinOp(Exp left, Exp right, Type type) {
		left.accept(this).checkEqual(type);
		right.accept(this).checkEqual(type);
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Ident ident, Exp exp) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitVarStmt(Ident ident, Exp exp) {
		// to be modified/completed
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitSingleStmt(Stmt stmt) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq rest) {
		// to be modified/completed
		return null;
	}

	// static semantics of expressions; a type is returned by the visitor

	@Override
	public Type visitAdd(Exp left, Exp right) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitIntLiteral(int value) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitListLiteral(ExpSeq exps) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitMul(Exp left, Exp right) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitPrefix(Exp left, Exp right) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitSign(Exp exp) {
		// to be modified/completed
		return null;
	}

	@Override
	public Type visitIdent(String name) {
		// to be modified/completed
		return null;
	}

	// static semantics of sequences of expressions
	// a type is returned by the visitor

	@Override
	public Type visitSingleExp(Exp exp) {
		return null;
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq rest) {
		// to be modified/completed
		return null;
	}

}
