package lab11_05_08.visitors.evaluation;

import lab11_05_08.environments.EnvironmentException;
import lab11_05_08.environments.GenEnvironment;
import lab11_05_08.parser.ast.Exp;
import lab11_05_08.parser.ast.ExpSeq;
import lab11_05_08.parser.ast.Ident;
import lab11_05_08.parser.ast.SimpleIdent;
import lab11_05_08.parser.ast.Stmt;
import lab11_05_08.parser.ast.StmtSeq;
import lab11_05_08.visitors.Visitor;

public class Eval implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new EvaluatorException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Ident ident, Exp exp) {
		env.update(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		ListValue list = exp.accept(this).asList();
		for (Value val : list) {
			env.enterLevel();
			env.dec(ident, val);
			block.accept(this);
			env.exitLevel();
		}
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		System.out.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}


	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	@Override
	public Value visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() + right.accept(this).asInt());
	}

	@Override
	public Value visitIntLiteral(int value) {
		return new IntValue(value);
	}

	@Override
	public Value visitBoolLiteral(boolean value) {return new BoolValue(value);}

	@Override
	public Value visitListLiteral(ExpSeq exps) {
		return exps.accept(this);
	}

	@Override
	public Value visitOptionalLiteral(Exp exp) {
		return exp.accept(this);
	}

	@Override
	public Value visitMul(Exp left, Exp right) {
        return new IntValue(left.accept(this).asInt() * right.accept(this).asInt());
	}

	@Override
	public Value visitPrefix(Exp left, Exp right) {
		Value el = left.accept(this);
		return right.accept(this).asList().prefix(el);
	}


	@Override
	public Value visitAnd(Exp left, Exp right){
	    return new BoolValue(left.accept(this).asBool() && right.accept(this).asBool());
	}

	@Override
	public Value visitEquivalent(Exp left, Exp right){
	    return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public Value visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).asInt());
	}

	@Override
	public Value visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).asBool());
	}

	@Override
	public Value visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// dynamic semantics of sequences of expressions
	// a list of values is returned by the visitor

	@Override
	public Value visitSingleExp(Exp exp) {
		return new ListValue(exp.accept(this), new ListValue());
	}

	@Override
	public Value visitMoreExp(Exp first, ExpSeq rest) {
		return new ListValue(first.accept(this), rest.accept(this).asList());
	}

}
