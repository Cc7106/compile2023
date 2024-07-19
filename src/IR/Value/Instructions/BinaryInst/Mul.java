package IR.Value.Instructions.BinaryInst;

import IR.Value.BasicBlock;
import IR.Value.Value;

public class Mul extends BinaryInst {

    // %1 = mul i32 %2, %3
    public Mul(Value op1, Value op2) {
        super(op1.getType(), op1, op2);
    }

    @Override
    public String toString() {
        return super.toString().replace("OPERATION", "mul");
    }
}
