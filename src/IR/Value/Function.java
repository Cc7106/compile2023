package IR.Value;

import IR.Type.*;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Function extends User {

    private ArrayList<Argument> argumentsList;
    private ArrayList<BasicBlock> basicBlocksList;

    private Boolean isLibrary;

    public Function(String name, FunctionType type, Boolean isLibrary) {
        super("@" + name, type);
        REG_NUM_COUNT = 0;
        this.argumentsList = new ArrayList<>();
        for (Type t: type.getParams()) {
            this.argumentsList.add(new Argument(isLibrary? "": "%"+REG_NUM_COUNT++, t));
        }

        this.basicBlocksList = new ArrayList<>();
        this.isLibrary = isLibrary;

    }

    public void addBasicBlock(BasicBlock block) {
        this.basicBlocksList.add(block);
    }

    public Type getReturnType() {
        return ((FunctionType)this.getType()).getReturnType();
    }

    public ArrayList<Argument> getArgumentsList() {
        return argumentsList;
    }

    //    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        if (isLibrary) {
//            sb.append("declare ");
//        } else {
//            sb.append("define ");
//        }
//        sb.append(this.getType().toString() + " " + this.getName() + " (");
//        if (!argumentsList.isEmpty()) {
//            for (Argument arg: argumentsList) {
//                sb.append(arg.)
//            }
//        }
//        sb.append("){")
//
//    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isLibrary) {
            //declare void @putint(i32)
            sb.append("declare " + this.getType() + " " + this.getName() + "(");
            if (!argumentsList.isEmpty()) {
                sb.append(argumentsList.get(0).getType());
            }
            sb.append(")");
        } else {
            sb.append("\ndefine dso_local " + this.getType() + " " + this.getName() + "(");
            if (!argumentsList.isEmpty()) {
                for (int i = 0; i < argumentsList.size(); i++) {
                    sb.append(argumentsList.get(i).getType() + " " + argumentsList.get(i).getName());
                    if (i + 1 < argumentsList.size()) {
                        sb.append(", ");
                    }
                }
            }
            sb.append(") {\n");
            for (BasicBlock basicBlock: basicBlocksList) {
                sb.append(basicBlock);
            }
            sb.append("}");
        }
        return sb.toString();
    }

    public BasicBlock getBlock(int index) {
        return basicBlocksList.get(index);
    }

}
