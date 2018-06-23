package lab11_05_08.parser;

import static lab11_05_08.parser.TokenType.*;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class StreamTokenizer implements Tokenizer {
	private static final String regEx;
	private static final Map<String, TokenType> keywords = new HashMap<>();
	private static final Map<String, TokenType> symbols = new HashMap<>();

	private boolean hasNext = true; // any stream contains at least the EOF
									// token
	private TokenType tokenType;
	private String    tokenString;
	private int     intValue;
	private boolean boolValue;
	private int     binaryValue;
	private final Scanner scanner;


	static {
		// remark: groups must correspond to the ordinal of the corresponding
		// token type
        final String boolRegEx   = "(true|false)";              // group 1
		final String identRegEx  = "([a-zA-Z][a-zA-Z0-9]*)";    // group 2
		final String numRegEx    = "(0|[1-9][0-9]*)";           // group 3
		final String skipRegEx   = "(\\s+|//.*)";               // group 4
		final String binaryRegEx = "(0[b|B][0|1]+)";			// group 5
		final String symbolRegEx = "\\+|\\*|=|\\(|\\)|;|,|\\{|\\}|-|::|:|\\[|\\]";
		regEx = boolRegEx   + "|" +
                identRegEx  + "|" +
				numRegEx    + "|" +
				skipRegEx   + "|" +
				binaryRegEx + "|" +
				symbolRegEx;
	}

	static {
		keywords.put("for", FOR);
		keywords.put("print", PRINT);
		keywords.put("var", VAR);
	}

	static {
		symbols.put("+", PLUS);
		symbols.put("*", TIMES);
		symbols.put("::", PREFIX);
		symbols.put("=", ASSIGN);
		symbols.put(":", IN);
		symbols.put("(", OPEN_PAR);
		symbols.put(")", CLOSE_PAR);
		symbols.put(";", STMT_SEP);
		symbols.put(",", EXP_SEP);
		symbols.put("{", OPEN_BLOCK);
		symbols.put("}", CLOSE_BLOCK);
		symbols.put("-", MINUS);
		symbols.put("[", OPEN_LIST);
		symbols.put("]", CLOSE_LIST);
	}

	public StreamTokenizer(Reader reader) {
		scanner = new StreamScanner(regEx, reader);
	}

	private void checkType() {
		tokenString = scanner.group();
        if (scanner.group(BOOL.ordinal()) != null) { // BOOL
            tokenType = BOOL;
            boolValue = Boolean.parseBoolean(tokenString);
            return;
        }
		if (scanner.group(IDENT.ordinal()) != null) { // IDENT or a keyword
			tokenType = keywords.get(tokenString);
			if (tokenType == null)
				tokenType = IDENT;
			return;
		}
		if (scanner.group(NUM.ordinal()) != null) { // NUM
			tokenType = NUM;
			intValue  = Integer.parseInt(tokenString);
			return;
		}
		if (scanner.group(SKIP.ordinal()) != null) { // SKIP
			tokenType = SKIP;
			return;
		}
		if (scanner.group(BINARY.ordinal()) != null) { // BINARY
			tokenType = BINARY;
			binaryValue = Integer.parseInt(tokenString.substring(2),2);
			return;
		}

		tokenType = symbols.get(tokenString); // a symbol
		if (tokenType == null)
			throw new AssertionError("Fatal error");
	}

	@Override
	public TokenType next() throws TokenizerException {
		do {
			tokenType = null;
			tokenString = "";
			try {
				if (hasNext && !scanner.hasNext()) {
					hasNext = false;
					return tokenType = EOF;
				}
				scanner.next();
			} catch (ScannerException e) {
				throw new TokenizerException(e);
			}
			checkType();
		} while (tokenType == SKIP);
		return tokenType;
	}

	private void checkValidToken() {
		if (tokenType == null)
			throw new IllegalStateException();
	}

	private void checkValidToken(TokenType ttype) {
		if (tokenType != ttype)
			throw new IllegalStateException();
	}

	@Override
	public String tokenString() {
		checkValidToken();
		return tokenString;
	}

	@Override
	public int intValue() {
		checkValidToken(NUM);
		return intValue;
	}

	@Override
    public boolean boolValue(){
	    checkValidToken(BOOL);
	    return boolValue;
    }

    @Override
    public int binaryValue(){
	    checkValidToken(BINARY);
	    return binaryValue;
    }

	@Override
	public TokenType tokenType() {
		checkValidToken();
		return tokenType;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public void close() throws TokenizerException {
		try {
			scanner.close();
		} catch (ScannerException e) {
			throw new TokenizerException(e);
		}
	}
}
