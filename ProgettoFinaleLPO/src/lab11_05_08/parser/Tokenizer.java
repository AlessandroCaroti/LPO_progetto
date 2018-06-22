package lab11_05_08.parser;

public interface Tokenizer extends AutoCloseable {
	//TODO FORSE aggiungere metodi binaryValue() e boolValue() FORSE

	TokenType next() throws TokenizerException;

	String tokenString();

	int intValue();

	TokenType tokenType();

	boolean hasNext();

	public void close() throws TokenizerException;

}