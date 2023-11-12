package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class Btype extends Node {
    private Token intTk;

    public Btype(Token intTk) {
        this.intTk = intTk;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(intTk.toString());
        return output.toString();
    }

    public Token getBtype() {
        return intTk;
    }
}
