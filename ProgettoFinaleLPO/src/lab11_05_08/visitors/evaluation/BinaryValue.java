package lab11_05_08.visitors.evaluation;

public class BinaryValue extends PrimValue<Integer>{

    public BinaryValue(Integer value) {
        super(value);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof BinaryValue))
            return false;
        return value.equals(((BinaryValue) obj).value);
    }

    @Override
    public int asInt() {
        return value;
    }

}
