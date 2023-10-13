package GrammarTypes;

import java.util.StringJoiner;

public class Cond extends Node {
    //Cond → LOrExp
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(lOrExp.toString());
        output.add("<Cond>");
        return output.toString();
    }
}
