package lab11_05_08.parser;

public interface Tokenizer extends AutoCloseable {
	//TODO aggiungere metodi binaryValue() e boolValue()

	TokenType next() throws TokenizerException;

	String tokenString();

	int intValue();

	boolean boolValue();

	TokenType tokenType();

	boolean hasNext();

	public void close() throws TokenizerException;

}