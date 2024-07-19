package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class UnaryOp extends Node {
    //UnaryOp → '+' | '−' | '!'

    private Token op;

    public UnaryOp(Token op) {
        this.op = op;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");

        output.add(op.toString());
        output.add("<UnaryOp>");
        return output.toString();
    }

    public Token getOp() {
        return op;
    }
}
