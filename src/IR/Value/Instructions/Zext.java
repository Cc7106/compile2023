package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class Zext extends Instruction {

    // %1 = zext i1 %0 to i32
    public Zext(Value oriValue, Type targetType) {
        super("%_" + REG_NUM_COUNT++, targetType);
        this.addOperand(oriValue);
    }

    public Value getOriValue() {
        return this.getOperand(0);
    }

    @Override
    public String toString() {
        return this.getName() + " = zext " + this.getOriValue().getType() + " " +
                this.getOriValue().getName() + " to " + this.getType();
    }
}
