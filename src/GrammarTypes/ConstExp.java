package GrammarTypes;

import java.util.StringJoiner;

public class ConstExp implements Node {
    //ConstExp → AddExp
    private AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(addExp.toString());
        output.add("<ConstExp>");

        return output.toString();
    }

}
