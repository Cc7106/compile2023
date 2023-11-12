package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class FuncDef extends Node {
    // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    private FuncType funcType;
    private Token ident;
    private Token LParent;
    private FuncFParams funcFParams;
    private Token RParent;
    private Block block;

    public FuncDef(FuncType funcType, Token ident, Token lParent, Token rParent, Block block) {
        //无形参
        this.funcType = funcType;
        this.ident = ident;
        this.LParent = lParent;
        this.funcFParams = null;
        this.RParent = rParent;
        this.block = block;
    }

    public FuncDef(FuncType funcType, Token ident, Token lParent, FuncFParams funcFParams, Token rParent, Block block) {
        //有形参
        this.funcType = funcType;
        this.ident = ident;
        this.LParent = lParent;
        this.funcFParams = funcFParams;
        this.RParent = rParent;
        this.block = block;
    }

    public FuncDef(FuncType funcType, Token ident, Token lParent, FuncFParams funcFParams, Token rParent) {
        //有形参
        this.funcType = funcType;
        this.ident = ident;
        this.LParent = lParent;
        this.funcFParams = funcFParams;
        this.RParent = rParent;
        this.block = null;
    }

    public void addBlock(Block block) {
        this.block = block;
    }
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(funcType.toString());
        output.add(ident.toString());
        output.add(LParent.toString());
        if (funcFParams != null) {
            output.add(funcFParams.toString());
        }
        output.add(RParent.toString());
        output.add(block.toString());

        output.add("<FuncDef>");
        return output.toString();
    }

    public FuncFParams getFuncFParams() {
        return funcFParams;
    }

    public Token getIdent() {
        return ident;
    }

    public Token getFuncType() {
        return funcType.getFuncType();
    }

    public Block getBlock() {
        return block;
    }

}
