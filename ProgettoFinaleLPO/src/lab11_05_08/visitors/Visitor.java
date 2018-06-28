package lab11_05_08.visitors;

import lab11_05_08.parser.ast.Exp;
import lab11_05_08.parser.ast.ExpSeq;
import lab11_05_08.parser.ast.Ident;
import lab11_05_08.parser.ast.Stmt;
import lab11_05_08.parser.ast.StmtSeq;

public interface Visitor<T> {
	T visitAdd(Exp left, Exp right);

	T visitAssignStmt(Ident ident, Exp exp);

	T visitForEachStmt(Ident ident, Exp exp, StmtSeq block);

	T visitIntLiteral(int value);

	T visitBoolLiteral(boolean value);

	T visitListLiteral(ExpSeq exps);

	T visitMoreExp(Exp first, ExpSeq rest);

	T visitMoreStmt(Stmt first, StmtSeq rest);

	T visitMul(Exp left, Exp right);

	T visitPrefix(Exp left, Exp right);

	T visitAnd(Exp left, Exp right);

	T visitEquivalent(Exp left, Exp right);

	T visitPrintStmt(Exp exp);

	T visitProg(StmtSeq stmtSeq);

	T visitSign(Exp exp);

	T visitNot(Exp exp);

	T visitIdent(String name);

	T visitSingleExp(Exp exp);

	T visitSingleStmt(Stmt stmt);

	T visitVarStmt(Ident ident, Exp exp);

	T visitOptionalLiteral(Exp exp);
}
