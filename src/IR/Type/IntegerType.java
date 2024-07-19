package IR.Type;

public class IntegerType implements Type{
    private final int bit;

    public IntegerType(int bit) {
        this.bit = bit;
    }

    public Boolean isInt1() {
        return bit == 1;
    }

    public Boolean isInt8() {
        return bit == 8;
    }

    public Boolean isInt32() {
        return bit == 32;
    }

    public String toString() {
        return "i" + bit;
    }


    @Override
    public Type getElementType() {
        return this;
    }
}
