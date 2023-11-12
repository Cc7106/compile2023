package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class Number extends Node {
    //Number → IntConst
    private Token intConst;

    public Number(Token intConst) {
        this.intConst = intConst;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(intConst.toString());
        output.add("<Number>");
        return output.toString();
    }

    public Token getRParamType() {
        return intConst;
    }
}
