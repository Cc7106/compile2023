package SymbolTable;

import GrammarTypes.FuncDef;
import GrammarTypes.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private int curBlockId;
    private ArrayList<HashMap<String,Symbol>> varSymbolsMapList;
    private HashMap<String, Symbol> functionsMap;
    private int inLoop; // 0->not in loop  >0 -> in loop
    private int curFuncType; //-1-> void且出错 0->void 1->int

    public SymbolTable() {
        this.curBlockId = 0;
        this.varSymbolsMapList = new ArrayList<>();
        this.varSymbolsMapList.add(new HashMap<>());
        this.functionsMap = new HashMap<>();
        this.inLoop = 0;
    }

    public void setInLoop() { //在循环语块中
        inLoop++;
    }

    public void setOutLoop() { //退出当前循环语块
        inLoop--;
    }

    public Boolean isInLoop() { //当前是否处在循环中
        return inLoop > 0;
    }

    public void addNewBlock() { //添加新的作用域
        curBlockId++;
        varSymbolsMapList.add(new HashMap<>());
    }

    public void quitBlock() { //退出当前作用域
        varSymbolsMapList.remove(curBlockId);
        curBlockId--;
    }

    public Boolean isDefined(String tokenName) { //是否被声明过（当前或之前） -> 为了使用
        for (int i = curBlockId; i >= 0; i--) {
            HashMap<String, Symbol> varSymbolsMap = varSymbolsMapList.get(i);
            if (varSymbolsMap.containsKey(tokenName)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isFuncDefined(String tokenName) {
        return functionsMap.containsKey(tokenName);
    }

    public VarSymbol getVarSymbol(String tokenName) {
        for (int i = curBlockId; i >= 0; i--) {
            HashMap<String, Symbol> varSymbolsMap = varSymbolsMapList.get(i);
            if (varSymbolsMap.containsKey(tokenName)) {
                return (VarSymbol) varSymbolsMap.get(tokenName);
            }
        }
        return null;
    }

    public FuncSymbol getFuncSymbol(String tokenName) {
        if (functionsMap.get(tokenName) != null) {
            return (FuncSymbol) functionsMap.get(tokenName);
        }
        return null;
    }

    public Boolean canDefine(String tokenName) { //在当前作用域是否被声明过 -> 为了新的声明
        return !varSymbolsMapList.get(curBlockId).containsKey(tokenName);
    }

    public void addVar(Symbol symbol) {
        varSymbolsMapList.get(curBlockId).put(symbol.getTokenName(), symbol);
    }

    public void addFunction(Symbol symbol) {
        functionsMap.put(symbol.getTokenName(), symbol);
    }

    public void setCurFuncType(int type) {
        curFuncType = type;
    }

    public Boolean isCurVoidFunc() {
        return curFuncType == 0;
    }

}
