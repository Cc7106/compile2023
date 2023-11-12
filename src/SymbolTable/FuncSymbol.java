package SymbolTable;

import GrammarTypes.*;
import Token.*;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    //type: 0->void 1->int
    private int funcType;
    private int paramNum;
    private ArrayList<VarSymbol> funcFParamSymbol;
    private ArrayList<FuncFParam> funcFParamList;

    public FuncSymbol(Node node) {
        // 需记录
        // 函数名字，函数类型，函数参数数量，函数的各种参数

        super(node);
        if (node.getFuncType().getTokenType().equals(TokenType.INTTK)) {
            this.funcType = 1;
        } else {
            this.funcType = 0;
        }

        if (node.getFuncFParams() != null && node.getFuncFParams().getFuncFParamList() != null) {
            this.funcFParamList = node.getFuncFParams().getFuncFParamList();

            paramNum = funcFParamList.size();
            funcFParamSymbol = new ArrayList<>();
            for (FuncFParam funcFParam: funcFParamList) {
                funcFParamSymbol.add(new VarSymbol(funcFParam,0));
            }
        } else {
            paramNum = 0;
            funcFParamSymbol = new ArrayList<>();
        }

    }

    public int getFuncType() {
        return funcType;
    }

    public int getParamNum() {
        return paramNum;
    }

    public ArrayList<VarSymbol> getFuncFParamSymbol() {
        return funcFParamSymbol;
    }

    public ArrayList<FuncFParam> getFuncFParamList() {
        return funcFParamList;
    }
}
