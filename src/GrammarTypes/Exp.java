package GrammarTypes;

import java.util.StringJoiner;
import Token.Token;

public class Exp extends Node {
    //Exp → AddExp
    private AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(addExp.toString());
        output.add("<Exp>");
        return output.toString();
    }

    public Token getRParamType() {
        return addExp.getRParamType();
    }

    public int getDimension() {
        return addExp.getDimension();
    }


}
