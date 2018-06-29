package lab11_05_08.visitors.evaluation;

public class EmptyValue extends PrimValue<String> {

    public EmptyValue() {
        super("Empty opt");
    }

    @Override
    public boolean equals(Object obj){
        return  this == obj ||
                obj instanceof EmptyValue;
    }

    /*
    @Override
    public boolean def(){
        return false;
    }
     */
}
