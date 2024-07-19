package IR.Value.Instructions.BinaryInst;

import IR.Value.BasicBlock;
import IR.Value.Value;

public class Or extends BinaryInst {
    // %2 = or i32 %0, %1
    public Or(Value op1, Value op2) {
        super(op1.getType(), op1, op2);
    }

    public String toString() {
        return super.toString().replace("OPERATION", "or");
    }
}
