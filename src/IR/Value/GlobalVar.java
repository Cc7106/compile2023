package IR.Value;

import IR.Type.PointerType;
import IR.Type.Type;

public class GlobalVar extends User {
    //全局变量

    public boolean isConst; //是常量 / 变量
    public GlobalVar(String name, Value value,  boolean isConst) {
        super("@" + name, new PointerType(value.getType()));
        this.isConst = isConst;
        this.addOperand(value);
    }

    public GlobalVar(Value constStr, boolean isConst) {
        //字符串定义
        //super("@str_" + ((ConstStr)constStr).getStrIndex() , constStr.getType());
        super("@str_" + ((ConstStr)constStr).getStrIndex() , new PointerType(constStr.getType()));

        this.isConst = isConst;
        this.addOperand(constStr);
    }

    public Value usedValue() {
        Value value = this.getOperand(0);
        return this.getOperand(0);
    }

    public Type getTargetType() {
        return ((PointerType)this.getType()).getTargetType();
    }


    @Override
    public String toString() {
        //@a = dso_local constant i32 10 //const
        //@b = dso_local global i32 30 //var
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName() + " = dso_local ");
        if (isConst) {
            sb.append("constant ");
        } else {
            sb.append("global ");
        }
        sb.append(usedValue().toString());
//        sb.append(usedValue().getType() + " ");
//        if (usedValue() instanceof ConstInt) {
//            ((ConstInt)usedValue()).getIntConst();
//        } else if (usedValue() instanceof ConstStr) {
//            ((ConstStr) usedValue()).getStrConst();
//        }
        return sb.toString();
    }

    public int getIntConst() {
        return this.usedValue().getIntConst();
    }

    public Value getElement(int index) {
        return this.usedValue().getElement(index);
    }
}
