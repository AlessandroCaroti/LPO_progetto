package lab11_05_08.parser.ast;

import lab11_05_08.visitors.Visitor;

public class IntLiteral extends PrimLiteral<Integer> {

	public IntLiteral(int n) {
		super(n);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}

}
