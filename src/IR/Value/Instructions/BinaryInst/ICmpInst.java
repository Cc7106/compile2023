package IR.Value.Instructions.BinaryInst;

import IR.Type.IntegerType;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class ICmpInst extends BinaryInst {

    public ICmpInst(Value op1, Value op2) {
        super(new IntegerType(1), op1, op2);
    }

    public String toString() {
        return super.toString().replace("OPERATION", "icmp op");
    }
}
