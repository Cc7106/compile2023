package IR.Type;

public class LabelType implements Type {
    @Override
    public String toString() {
        return "label";
    }

    @Override
    public Type getElementType() {
        return this;
    }
}
