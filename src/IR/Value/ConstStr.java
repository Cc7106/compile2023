package IR.Value;

import IR.Type.ArrayType;
import IR.Type.IntegerType;
import IR.Type.PointerType;
import IR.Type.Type;

public class ConstStr extends User {
    private String strContent;
    private int length;
    private static int strIndexCounter = 0;
    private int strIndex;
//    public ConstStr(String strConst, int length) {
//        //处理转义字符 \n->\0a + 加回开关引号 + 加终结字符
//        super("c\"" + strConst.replace("\\n","\\0A") + "\\00\"",
//                new PointerType(new ArrayType(new IntegerType(8), length)));
//        this.strContent = "c\"" + strConst.replace("\\n", "\\0a") + "\\00\"";
//        this.strIndex = strIndexCounter++;
//        this.length = length; //加上终结字符
//    }

    //----修改
    public ConstStr(String strConst, int length) {
        //处理转义字符 \n->\0a + 加回开关引号 + 加终结字符
        super("c\"" + strConst.replace("\\n","\\0A") + "\\00\"",
                new ArrayType(new IntegerType(8), length));
        this.strContent = "c\"" + strConst.replace("\\n", "\\0a") + "\\00\"";
        this.strIndex = strIndexCounter++;
        this.length = length; //加上终结字符
    }

    public String getStrContent() {
        return strContent;
    }

    public int getLength() {
        return length;
    }

    public int getStrIndex() {
        return strIndex;
    }

    // content : Hello\n
    // [7xi8] c"Hello\n\00"
    public String toString() {
       // return "[" + length + " x " + ((PointerType)this.getType()).getTargetType() + " " + strConst;
        //return ((PointerType)this.getType()).getTargetType() + " " + this.getStrContent();
        return (this.getType()) + " " + this.getStrContent();
    }
}
