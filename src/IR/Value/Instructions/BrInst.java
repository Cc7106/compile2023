package IR.Value.Instructions;

import IR.Type.Type;
import IR.Type.VoidType;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class BrInst extends Instruction {

    //一个分支，直接跳转
    // br label %14
    public BrInst(BasicBlock toBlock) {
        super("", new VoidType());
        this.addOperand(toBlock);
    }

    //两个分支，有条件
    //br i1 %2, label %12, label %3
    public BrInst(Value cond, BasicBlock trueBlock, BasicBlock falseBlock) {
        super("", new VoidType());
        this.addOperand(cond);
        this.addOperand(trueBlock);
        this.addOperand(falseBlock);
    }

    public Boolean isCond() {
        return this.getOperands().size() == 3;
    }

    @Override
    public String toString() {
        // br
        StringBuilder sb = new StringBuilder();
        sb.append("br " + this.getOperand(0).getType() + " " + this.getOperand(0).getName());
        if (isCond()) {
            sb.append(", " + this.getOperand(1).getType() + " " + this.getOperand(1).getName());
            sb.append(", " + this.getOperand(2).getType() + " " + this.getOperand(2).getName());
        }
        return sb.toString();

    }
}
