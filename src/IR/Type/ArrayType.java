package IR.Type;

public class ArrayType implements Type {
    private final Type elementType;
    private final int length;

    public ArrayType(Type elementType, int length) {
        //字符串 [length * i8]
        // 数组 [length * i32]
        // 二维 [length * [arrayType] ]
        this.elementType = elementType;
        this.length = length;
    }

    public Type getElementType() {
        return elementType;
    }

    public int getLength() {
        return length;
    }

    public String toString() {
        return "[" + length + " x " + elementType.toString() + "]";
    }
}
