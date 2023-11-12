package SymbolTable;

import GrammarTypes.*;
import Token.*;

public class VarSymbol extends Symbol {
    //type: 0->const, 1->int 2->function
    private int varType;
    private int dimension; // 0->a , 1->a[] , 2->a[][]

    public VarSymbol(Node node, Token ident, int tableId, int type, int dimension) {
        super(node, ident, tableId, type);
        this.dimension = dimension;
    }

    public VarSymbol(Node node, int situation) {
        //funcFParam
        super(node);
        if (node instanceof FuncFParam) {
            varType = 1;
        }
        dimension = node.getDimension();
    }

    public VarSymbol(FuncType funcType, Token ident) {
        //func
        super(ident);
        varType = 2;
        if (funcType.getFuncType().getTokenType().equals(TokenType.VOIDTK)) {
            dimension = -1;
        } else {
            dimension = 0;
        }
    }

    public VarSymbol(Node node, Btype btype) {
        super(node);
        varType = 1;
        this.dimension = node.getDimension();
    }

    public VarSymbol(Node node) {
        super(node);
        if (node instanceof ConstDef) {
            varType = 0;
        } else if (node instanceof VarDef) {
            varType = 1;
        }
        this.dimension = node.getDimension();
    }

    public int getVarType() {
        return varType;
    }

    public int getDimension() {
        return dimension;
    }
}
