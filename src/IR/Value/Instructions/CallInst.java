package IR.Value.Instructions;

import IR.Type.IntegerType;
import IR.Type.Type;
import IR.Type.VoidType;
import IR.Value.BasicBlock;
import IR.Value.Function;
import IR.Value.Value;

import java.io.FileWriter;
import java.util.ArrayList;

public class CallInst extends Instruction {

    // %1 = call i32 @func(i32 %2)
    // call void @func()
    private Boolean isVoid;

    public CallInst(Function function, ArrayList<Value> params, Boolean isVoid) {
        //void 的情况
        super("", new VoidType());
        this.isVoid = isVoid;
        if (!isVoid) {
            this.setName("%_" + REG_NUM_COUNT++);
            this.setType(function.getReturnType());
        }
        this.addOperand(function);
        for (Value value: params) {
            this.addOperand(value);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder();
        Function function = (Function) this.getOperand(0);
        if (!isVoid) {
            sb.append(this.getName() + " = ");
        }
        sb.append("call ");
        sb.append(this.getType() + " ");
        sb.append(function.getName() + "(");
        for (int i = 0; i < function.getArgumentsList().size(); i++) {
            Value rParam = this.getOperand(i + 1);
            sb.append(rParam.getType() + " " + rParam.getName());
            if (i + 1 < function.getArgumentsList().size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
