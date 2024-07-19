package IR;

import IR.Value.Function;
import IR.Value.GlobalVar;

import java.util.ArrayList;

public class IRModule {

    private ArrayList<GlobalVar> globalVarsList;
    private ArrayList<Function> functionsList;

    public IRModule() {
        globalVarsList = new ArrayList<>();
        functionsList = new ArrayList<>();
    }

    public void addFunction(Function function) {
        functionsList.add(function);
    }

    public Function findFunction(String name) {
        for (Function function: functionsList) {
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    public void addGlobalVar(GlobalVar globalVar) {
        globalVarsList.add(globalVar);
    }

    public ArrayList<GlobalVar> getGlobalVarsList() {
        return globalVarsList;
    }

    public ArrayList<Function> getFunctionsList() {
        return functionsList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GlobalVar globalVar: globalVarsList) {
            sb.append(globalVar + "\n");
        }
        sb.append("\n");
        for (Function function: functionsList) {
            sb.append(function + "\n");
        }
        return sb.toString();
    }

    public Function getFunctionByName(String funcName) {
        for (Function function: functionsList) {
            if (function.getName().equals("@"+funcName)) {
                return function;
            }
        }
        return null;
    }
}
