package IR.Value.Instructions;

import IR.Type.Type;
import IR.Type.VoidType;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class RetInst extends Instruction {

    //ret i32 %3
    //ret i32 0
    //ret void

    public RetInst() {
        super("", new VoidType());
    }

    public RetInst(BasicBlock curBlock, Value retValue) {
        super("", retValue.getType());
        this.addOperand(retValue);
    }

    @Override
    public String toString() {
        if (this.operandEmpty()) {
            //void
            return "ret void";
        } else {
            return "ret " + this.getOperand(0).getType() + " " + this.getOperand(0).getName();
        }
    }
}
