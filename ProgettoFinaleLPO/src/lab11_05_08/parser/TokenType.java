package lab11_05_08.parser;

//TODO aggiungere BINARY
public enum TokenType { // important: IDENT, NUM, SKIP and BOOL must have ordinals 1, 2, 3, and 4
	EOF, IDENT, NUM, SKIP, BOOL, IN, FOR, PRINT, PREFIX, VAR, PLUS, TIMES, ASSIGN, OPEN_PAR, CLOSE_PAR, STMT_SEP, EXP_SEP, OPEN_BLOCK, CLOSE_BLOCK, MINUS, OPEN_LIST, CLOSE_LIST
}
