package IR.Value.Instructions;

import IR.Type.ArrayType;
import IR.Type.IntegerType;
import IR.Type.PointerType;
import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.ConstStr;
import IR.Value.Value;

import java.util.ArrayList;

public class GEPInst extends Instruction {

    //%_1 = getelementptr inbounds [9 x i8], [9 x i8]* @str_0, i32 0, i32 0
    Type targetType; //str or arr
    Boolean isConstStr;

    public GEPInst(Value basePointerAddr, ArrayList<Value> Indices) {
        //array / str
        super("%_" + REG_NUM_COUNT++, basePointerAddr.getType().getElementType());
        this.addOperand(basePointerAddr);
        this.targetType = basePointerAddr.getType().getElementType();
        //this.setType(new PointerType(new IntegerType(32)));
        int count = Indices.size();
        Type finalType = this.targetType;
        for (int i = 1; i < count; i++) {
            finalType = finalType.getElementType();
        }
        this.setType(new PointerType(finalType));
        if (finalType.getElementType() instanceof IntegerType && ((IntegerType) finalType.getElementType()).isInt8()) {
            this.isConstStr = true;
        } else {
            this.isConstStr = false;
        }
        for (Value indice: Indices) {
            this.addOperand(indice);
        }

    }

//    public GEPInst(Value basePointerAddr, ArrayList<Value> Indices,ConstStr constStr) {
//        //str
//        super("%_" + REG_NUM_COUNT++, ((PointerType)basePointerAddr.getType()).getTargetType());
//        this.addOperand(basePointerAddr);
//        this.targetType = ((PointerType) basePointerAddr.getType()).getTargetType();
//        this.setType(new PointerType(new IntegerType(8)));
//        this.isConstStr = true;
//        for (Value indice: Indices) {
//            this.addOperand(indice);
//        }
//    }

    public Value fromPointer() {
        return this.getOperand(0);
    }

    public ArrayList<Value> getIndices() {
        ArrayList<Value> indices = new ArrayList<>(getOperands().subList(1, getOperands().size()));
        return indices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //字符串输出
        if (isConstStr) {
            sb.append(this.getName() + " = getelementptr inbounds ");
        } else {
            sb.append(this.getName() + " = getelementptr ");
        }
        sb.append(this.targetType + ", ");
        sb.append(this.fromPointer().getType() + " " + this.fromPointer().getName() + ", ");
        for (int i = 0; i < getIndices().size(); i++) {
            Value indices = getIndices().get(i);
            sb.append(indices.getType() + " " + indices.getName());
            if (i + 1 < getIndices().size()) {
                sb.append(", ");
            }
        }

        return sb.toString();
//        if (isConstStr) {
//            sb.append(this.getName() + " = getelementptr inbounds ");
//            sb.append(this.targetType + ", ");
//            sb.append(this.fromPointer().getType() + " " + this.fromPointer().getName() + ", ");
//            for (int i = 0; i < getIndices().size(); i++) {
//                Value indices = getIndices().get(i);
//                sb.append(indices.getType() + " " + indices.getName());
//                if (i + 1 < getIndices().size()) {
//                    sb.append(", ");
//                }
//            }
//        } else {
//            sb.append(this.getName() + " = getelementptr ");
//            sb.append(this.targetType + ", ");
//            sb.append(this.fromPointer().getType() + " " + this.fromPointer().getName() + ", ");
//            for (int i = 0; i < getIndices().size(); i++) {
//                Value indices = getIndices().get(i);
//                sb.append(indices.getType() + " " + indices.getName());
//                if (i + 1 < getIndices().size()) {
//                    sb.append(", ");
//                }
//            }
//        }
//
//
//        return sb.toString();
    }
}
