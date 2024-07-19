package IR.Value.Instructions;

import IR.Type.*;
import IR.Type.VoidType;
import IR.Value.*;

public class StoreInst extends Instruction {

    // store i32 %2, i32* %1
    // 把 %2 的值存入 %1 的地址
    public StoreInst(Value value, Value ptrLocation) {
        super("", new VoidType());
        this.addOperand(ptrLocation);
        this.addOperand(value);
    }

    public Value getPtrLocation() {
        return this.getOperand(0);
    }

    public Value getValue() {
        return this.getOperand(1);
    }


    @Override
    public String toString() {
        return "store " +  getValue().getType() + " " + getValue().getName() +
                ", " + getPtrLocation().getType() + " " + getPtrLocation().getName();
    }
}
