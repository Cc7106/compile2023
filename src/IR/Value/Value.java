package IR.Value;

import IR.Type.*;

import java.util.ArrayList;

public class Value {
    private String name;
    private Type type;
    public static int REG_NUM_COUNT = 0;  //基本块中的寄存器号

    public Value(String name, Type type) {
        this.name = name;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getIntConst() {
        return 0;
    }
    public Value getElement(int index) {
        return null;
    }

}
