package IR.Value.Instructions.BinaryInst;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Instructions.Instruction;
import IR.Value.Value;

public class BinaryInst extends Instruction {

    public BinaryInst(Type finalType, Value op1, Value op2) {
        super("%_" + REG_NUM_COUNT++, finalType);
        this.addOperand(op1);
        this.addOperand(op2);
    }

    public Value getOp1() {
        return this.getOperand(0);
    }

    public Value getOp2() {
        return this.getOperand(1);
    }

    public String toString() {
        return this.getName() + " = OPERATION " + this.getOp1().getType() + " " +
                this.getOp1().getName() + ", " + this.getOp2().getName();
    }
}

