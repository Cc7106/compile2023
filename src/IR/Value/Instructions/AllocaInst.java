package IR.Value.Instructions;

import IR.Type.PointerType;
import IR.Type.Type;
import IR.Value.BasicBlock;

public class AllocaInst extends Instruction {

    // %1 = alloca i32
    // %1 -> pointerType
    public AllocaInst(Type pointToType) {
        super("%_" + REG_NUM_COUNT++, new PointerType(pointToType));
    }


    @Override
    public String toString() {
        return this.getName() + " = alloca " + ((PointerType)this.getType()).getTargetType();
    }
}
