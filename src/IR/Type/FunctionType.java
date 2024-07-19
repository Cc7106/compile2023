package IR.Type;

import java.util.ArrayList;

public class FunctionType implements Type {

    //记录参数 返回类型
    private final ArrayList<Type> params;
    private final Type returnType;

    public FunctionType(ArrayList<Type> params, Type returnType) {
        this.params = params;
        this.returnType = returnType;
    }

    public ArrayList<Type> getParams() {
        return params;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return returnType.toString();
    }

    @Override
    public Type getElementType() {
        //乱来的
        return returnType;
    }
}
