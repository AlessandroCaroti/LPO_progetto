package lab11_05_08.visitors.typechecking;

import static lab11_05_08.visitors.typechecking.PrimtType.*;

import lab11_05_08.environments.EnvironmentException;
import lab11_05_08.environments.GenEnvironment;
import lab11_05_08.parser.ast.*;
import lab11_05_08.visitors.Visitor;

public class TypeCheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
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
		Type found = env.lookup(ident);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		Type ty = exp.accept(this).getListElemType();
		env.enterLevel();
		env.dec(ident, ty);
		block.accept(this);
		env.exitLevel();
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// static semantics of expressions; a type is returned by the visitor

	@Override
	public Type visitAdd(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitIntLiteral(int value) {
		return INT;
	}

    @Override
    public Type visitBoolLiteral(boolean value) {
        return BOOL;
    }

	@Override
	public Type visitListLiteral(ExpSeq exps) {
		return new ListType(exps.accept(this));
	}

	@Override
	public Type visitOptionalLiteral(Exp exp, boolean undefined) {
		//controllo che se la corrente espressione non è definita bisogna controllare che exp sia un litiral di tipo opzionale
		Type typeRes = exp.accept(this);
		if(undefined && !typeRes.isOpt()) {
			throw new TypecheckerException(typeRes.toString(), OptionalType.TYPE_NAME);
		}
		return new OptionalType(typeRes,undefined);
	}

	@Override
	public Type visitDefined(Exp exp){
		Type typeRes = exp.accept(this);
		if(!typeRes.isOpt())
			throw new TypecheckerException(typeRes.toString(), OptionalType.TYPE_NAME);
		return BOOL;
	}

	@Override
	public Type visitGet(Exp exp){
		Type typeRes = exp.accept(this);
		if(!typeRes.isOpt())
			throw new TypecheckerException(typeRes.toString(), OptionalType.TYPE_NAME);
		return typeRes.getOptElemType();
	}

	@Override
	public Type visitMul(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitPrefix(Exp left, Exp right) {
		Type elemType = left.accept(this);
		return new ListType(elemType).checkEqual(right.accept(this));
	}

	@Override
	public Type visitAnd(Exp left, Exp right){
	    checkBinOp(left, right, BOOL);
	    return BOOL;
	}

	@Override
	public Type visitEquivalent(Exp left, Exp right){
        Type elemType = left.accept(this);
        elemType.checkEqual(right.accept(this));
        return BOOL;
	}

	@Override
	public Type visitSign(Exp exp) {
		return INT.checkEqual(exp.accept(this));
	}

	@Override
	public Type visitNot(Exp exp) {
		return BOOL.checkEqual(exp.accept(this));
	}

	@Override
	public Type visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// static semantics of sequences of expressions
	// a type is returned by the visitor

	@Override
	public Type visitSingleExp(Exp exp) {
		return exp.accept(this);
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq rest) {
		Type found = first.accept(this);
		return found.checkEqual(rest.accept(this));
	}

}
