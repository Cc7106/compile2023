package IR.Value;

import IR.Type.Type;

public class ConstInt extends User {
    private int intConst;

    // i32 i8 i1 (type)

    public ConstInt(Type type, int intConst) {
        super(Integer.toString(intConst), type);
        this.intConst = intConst;
    }

    public int getIntConst() {
        return intConst;
    }

    public String toString() {
        return this.getType() + " " + this.getName();
    }
}
