package lab11_05_08.parser;

public enum TokenType { // important: BOOL, IDENT, BINARY, NUM and SKIP must have ordinals 1, 2, 3, 4 and 5
	EOF, BOOL, IDENT, BINARY, NUM, SKIP, IN, FOR, PRINT, PREFIX, VAR, PLUS, TIMES, ASSIGN, OPEN_PAR, CLOSE_PAR, STMT_SEP, EXP_SEP, OPEN_BLOCK, CLOSE_BLOCK, MINUS, OPEN_LIST, CLOSE_LIST, AND, EQUIVALENT, NOT, OPT, EMPTY, DEF, GET, DO, WHILE
}
