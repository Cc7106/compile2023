package IR.Value.Instructions.BinaryInst;

import IR.Value.BasicBlock;
import IR.Value.Value;

public class Ne extends ICmpInst {
    public Ne(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override
    public String toString() {
        return super.toString().replace("op", "ne");
    }
}
