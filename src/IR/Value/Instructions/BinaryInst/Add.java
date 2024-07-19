package IR.Value.Instructions.BinaryInst;

import IR.Type.IntegerType;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class Add extends BinaryInst {

    // %1 = add i32 %2, %3
    public Add(Value op1, Value op2) {
        super(op1.getType(), op1, op2);
    }

    @Override
    public String toString() {
        return super.toString().replace("OPERATION", "add");
    }

}
