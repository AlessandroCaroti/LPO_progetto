package lab11_05_08.parser;

import lab11_05_08.parser.ast.Prog;

public interface Parser {

	Prog parseProg() throws ParserException;

}