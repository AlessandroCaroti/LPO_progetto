package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public interface AST {
	<T> T accept(Visitor<T> visitor);
}
