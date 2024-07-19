package IR.Value;

import IR.Type.ArrayType;
import IR.Type.Type;

import java.util.ArrayList;

public class ConstArr extends User {

    private Boolean isInit = false; //没有初始化 （需要zeroInitializer)

    public ConstArr(ArrayType arrayType, ArrayList<Value> elements) {
        super("", arrayType);
        if (elements != null) {
            isInit = true;
            for (Value element: elements) {
                this.addOperand(element);
            }
        }
    }

    public ArrayList<Value> elements() {
        return this.getOperands();
    }

    public Value getElement(int index) {
        return this.getOperand(index);
    }

    @Override
    public String toString() {
        //[6 x i32] [i32 1, i32 2, i32 3, i32 4, i32 5, i32 6]
        StringBuilder sb = new StringBuilder();
        sb.append(this.getType().toString() + " ");
        if (isInit) {
            //有初始化
            sb.append("[");
            for (int i = 0; i < elements().size(); i++) {
                sb.append(elements().get(i).toString());
                if (i + 1 < elements().size()) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        } else {
            //没初始化，置0
            sb.append("zeroinitializer");
        }
        return sb.toString();
    }
}
