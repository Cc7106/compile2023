package IR.Value.Instructions;

import IR.Type.PointerType;
import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class LoadInst extends Instruction {

    // %2 = load i32, ptr* ptr
    public LoadInst(Value fromPointer) {
        super("%_" + REG_NUM_COUNT++, ((PointerType)fromPointer.getType()).getTargetType());
        this.addOperand(fromPointer);
    }

    private Value fromPointer() {
        return this.getOperand(0);
    }

    @Override
    public String toString() {
        return this.getName() +" = load " + this.getType() + ", "
                + fromPointer().getType() + " " + fromPointer().getName();
    }
}
