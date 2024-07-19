package SymbolTable;

import GrammarTypes.Node;
import Token.Token;


public class Symbol {
    private Node node;
    private Token ident;
    private String tokenName;
    private int tableId;


    public Symbol(Token ident) {
        //for varSymbol的func用
        node = null;
        this.ident = ident;
        this.tokenName = ident.getToken();
    }
    public Symbol(Node node) {
        this.node = node;
        this.ident = node.getIdent();
        this.tokenName = ident.getToken();
    }

    public Symbol(Node node, Token ident, int tableId, int type) {
        this.node = node;
        this.ident = ident;
        this.tokenName = ident.getToken();
        this.tableId = tableId;
    }

    public Node getNode() {
        return node;
    }

    public Token getIdent() {
        return ident;
    }

    public String getTokenName() {
        return tokenName;
    }

    public int getTableId() {
        return tableId;
    }

}
