package interpreter.parser;

import static interpreter.parser.TokenType.*;

import interpreter.parser.ast.*;

/*
Prog ::= StmtSeq 'EOF'
 StmtSeq ::= Stmt (';' StmtSeq)?
 Stmt ::= 'var'? ID '=' Exp | 'print' Exp |  'for' ID ':' Exp '{' StmtSeq '}'
 ExpSeq ::= Exp (',' ExpSeq)?
 Exp ::= Add ('::' Exp)?
 Add ::= Mul ('+' Mul)*
 Mul::= Atom ('*' Atom)*
 Atom ::= '-' Atom | '[' ExpSeq ']' | NUM | ID | '(' Exp ')'

*/

public class StreamParser implements Parser {

	private final Tokenizer tokenizer;

	private void tryNext() throws ParserException {
		try {
			tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
	}

	private void match(TokenType expected) throws ParserException {
		final TokenType found = tokenizer.tokenType();
		if (found != expected)
			throw new ParserException(
					"Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
	}

	private void consume(TokenType expected) throws ParserException {
		match(expected);
		tryNext();
	}

	private void unexpectedTokenError() throws ParserException {
		throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
	}

	public StreamParser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Override
	public Prog parseProg() throws ParserException {
		tryNext(); // one look-ahead symbol
		Prog prog = new ProgClass(parseStmtSeq());

        //TODO rimmuovere stampa di test
		System.out.println("********************************\n\n");

		match(EOF);
		return prog;
	}

	private StmtSeq parseStmtSeq() throws ParserException {
		Stmt stmt = parseStmt();

		//TODO rimmuovere stampa di test
        System.out.println(stmt.toString());

		if (tokenizer.tokenType() == STMT_SEP) {
			tryNext();
			return new MoreStmt(stmt, parseStmtSeq());
		}
		return new SingleStmt(stmt);
	}

	private ExpSeq parseExpSeq() throws ParserException {
		Exp exp = parseExp();
		if (tokenizer.tokenType() == EXP_SEP) {
			tryNext();
			return new MoreExp(exp, parseExpSeq());
		}
		return new SingleExp(exp);
	}

	private Stmt parseStmt() throws ParserException {
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case PRINT:
			return parsePrintStmt();
		case VAR:
			return parseVarStmt();
		case IDENT:
			return parseAssignStmt();
		case FOR:
			return parseForEachStmt();
        case DO:
            return parseDoWhileStmt();
        case IF:
            return parseIfElseStmt();
		}
	}

	private PrintStmt parsePrintStmt() throws ParserException {
		consume(PRINT); // or tryNext();
		return new PrintStmt(parseExp());
	}

	private VarStmt parseVarStmt() throws ParserException {
		consume(VAR); // or tryNext();
		Ident ident = parseIdent();
		consume(ASSIGN);
		return new VarStmt(ident, parseExp());
	}

	private AssignStmt parseAssignStmt() throws ParserException {
		Ident ident = parseIdent();
		consume(ASSIGN);
		return new AssignStmt(ident, parseExp());
	}

	private ForEachStmt parseForEachStmt() throws ParserException {
		consume(FOR); // or tryNext();
		Ident ident = parseIdent();
		consume(IN);
		Exp exp = parseExp();
		consume(OPEN_BLOCK);
		StmtSeq stmts = parseStmtSeq();
		consume(CLOSE_BLOCK);
		return new ForEachStmt(ident, exp, stmts);
	}

	private DoWhileStmt parseDoWhileStmt() throws ParserException{
	    consume(DO);
	    consume(OPEN_BLOCK);
        StmtSeq stmts = parseStmtSeq();
        consume(CLOSE_BLOCK);
        consume(WHILE);
        consume(OPEN_PAR);
        Exp exp = parseExp();
        consume(CLOSE_PAR);
	    return new DoWhileStmt(stmts,exp);
    }

    private IfElseStmt parseIfElseStmt() throws ParserException{
	    consume(IF);
	    consume(OPEN_PAR);
        Exp exp = parseExp();
        consume(CLOSE_PAR);
        consume(OPEN_BLOCK);
        StmtSeq stmts1 = parseStmtSeq();
        consume(CLOSE_BLOCK);
        if(tokenizer.tokenType() == ELSE){
            consume(ELSE);
            consume(OPEN_BLOCK);
            StmtSeq stmts2 = parseStmtSeq();
            consume(CLOSE_BLOCK);
            return new IfElseStmt(exp,stmts1,stmts2);
        }
	    return new IfElseStmt(exp,stmts1);
    }


	private Exp parseExp() throws ParserException {
        Exp exp = parseEquivalent();
        if (tokenizer.tokenType() == AND){
            tryNext();
            exp = new And(exp, parseExp());
        }
        return exp;
	}

	private Exp parseEquivalent() throws ParserException {
        Exp exp = parsePrefix();
        while(tokenizer.tokenType() == EQUIVALENT){
            tryNext();
            exp = new Equivalent(exp, parsePrefix());
        }
        return exp;
	}

	private Exp parsePrefix() throws ParserException {
		Exp exp = parseAdd();
		while (tokenizer.tokenType() == PREFIX) {
			tryNext();
			exp = new Prefix(exp, parseAdd());
		}
		return exp;
	}

	private Exp parseAdd() throws ParserException {
		Exp exp = parseMul();
		while (tokenizer.tokenType() == PLUS) {
			tryNext();
			exp = new Add(exp, parseMul());
		}
		return exp;
	}

	private Exp parseMul() throws ParserException {
		Exp exp = parseAtom();
		while (tokenizer.tokenType() == TIMES) {
			tryNext();
			exp = new Mul(exp, parseAtom());
		}
		return exp;
	}



	private Exp parseAtom() throws ParserException {
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case NUM:
			return parseNum();
        case BOOL:
            return parseBool();
		case IDENT:
			return parseIdent();
		case MINUS:
			return parseMinus();
        case NOT:
            return parseNot();
        case OPT:
            return parseOpt();
        case EMPTY:
            return parseEmpty();
        case DEF:
            return parseDef();
        case GET:
            return parseGet();
		case OPEN_LIST:
			return parseList();
		case OPEN_PAR:
			return parseRoundPar();
		}
	}

	private IntLiteral parseNum() throws ParserException {
		int val = tokenizer.intValue();
		consume(NUM); // or tryNext();
		return new IntLiteral(val);
	}

	private BoolLiteral parseBool() throws ParserException {
	    boolean val = tokenizer.boolValue();
        consume(BOOL);
        return  new BoolLiteral(val);
    }

	private Ident parseIdent() throws ParserException {
		String name = tokenizer.tokenString();
		consume(IDENT); // or tryNext();
		return new SimpleIdent(name);
	}

	private Sign parseMinus() throws ParserException {
		consume(MINUS); // or tryNext();
		return new Sign(parseAtom());
	}

	private Invert parseNot() throws ParserException {
		consume(NOT);
		return new Invert(parseAtom());
	}

	private OptionalLiteral parseOpt() throws ParserException {
		consume(OPT);
		return new OptionalLiteral(parseAtom());
	}

	private OptionalLiteral parseEmpty() throws ParserException{
	    consume(EMPTY);
        return new OptionalLiteral(parseAtom(), true);
    }

    private Defined parseDef() throws ParserException{
        consume(DEF);
        return new Defined(parseAtom());
    }

    private Get parseGet() throws ParserException{
	    consume(GET);
	    return new Get(parseAtom());
    }
	private ListLiteral parseList() throws ParserException {
		consume(OPEN_LIST); // or tryNext();
		ExpSeq exps = parseExpSeq();
		consume(CLOSE_LIST);
		return new ListLiteral(exps);
	}

	private Exp parseRoundPar() throws ParserException {
		consume(OPEN_PAR); // or tryNext();
		Exp exp = parseExp();
		consume(CLOSE_PAR);
		return exp;
	}

}
