package IR.Type;

public class VoidType implements Type {

    public String toString() {
        return "void";
    }

    @Override
    public Type getElementType() {
        return this;
    }
}
