package IR.Value.Instructions.BinaryInst;

import IR.Value.BasicBlock;
import IR.Value.Value;

public class And extends BinaryInst {

    // %2 = and i32 %0, %1
    // i1也有可能

    public And(Value op1, Value op2) {
        super(op1.getType(), op1, op2);
    }

    public String toString() {
        return super.toString().replace("OPERATION", "and");
    }
}
