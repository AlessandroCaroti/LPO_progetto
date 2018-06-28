package lab11_05_08.visitors.evaluation;

public class OptValue extends PrimValue<Value>{

    protected OptValue(Value value) {
        super(value);
    }

    @Override
    public boolean equals(Object obj){
        if (this==obj)
            return true;
        if(!(obj instanceof OptValue))
            return false;
        return value.equals(((OptValue) obj).value);
    }

    @Override
    public String toString(){
        return "opt "+ this.value.toString();
    }
}
