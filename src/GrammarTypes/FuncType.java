package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class FuncType extends Node {
    //FuncType → 'void' | 'int'
    private Token funcType;

    public FuncType(Token funcType) {
        this.funcType = funcType;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");

        output.add(funcType.toString());
        output.add("<FuncType>");
        return output.toString();
    }

    public Token getFuncType() {
        return funcType;
    }

}
