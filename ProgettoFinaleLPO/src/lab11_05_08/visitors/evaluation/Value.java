package lab11_05_08.visitors.evaluation;

public interface Value {
	/* default conversion methods */
	default int asInt() {
		throw new EvaluatorException("Expecting an integer value");
	}

	default boolean asBool() {
	    throw new EvaluatorException("Expecting an boolean value");
	}

	default String asString() {
		throw new EvaluatorException("Expecting a string value");
	}

	default ListValue asList() {
		throw new EvaluatorException("Expecting a list value");
	}

	//TODO forse definire qui il metodo get e def

    default boolean def(){
        throw new EvaluatorException("Expecting a optional value");
    }

    default Value get(){
        throw new EvaluatorException("Expecting a optional value");
    }

}
