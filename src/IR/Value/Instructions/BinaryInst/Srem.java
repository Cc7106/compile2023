package IR.Value.Instructions.BinaryInst;

import IR.Value.BasicBlock;
import IR.Value.Value;

public class Srem extends BinaryInst {

    //%2 = srem i32 %0, %1
    public Srem(Value op1, Value op2) {
        super(op1.getType(), op1, op2);
    }

    public String toString() {
        return super.toString().replace("OPERATION", "srem");
    }
}
