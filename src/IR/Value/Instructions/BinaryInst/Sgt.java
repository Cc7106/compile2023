package IR.Value.Instructions.BinaryInst;

import IR.Value.BasicBlock;
import IR.Value.Value;

public class Sgt extends ICmpInst {
    public Sgt(Value op1, Value op2) {
        super(op1, op2);
    }

    public String toString() {
        return super.toString().replace("op", "sgt");
    }
}
