package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class RelExp extends Node {
    //RelExp → AddExp
    //RelExp → AddExp ('<' | '>' | '<=' | '>=') RelExp

    private AddExp addExp;
    private Token relation;
    private RelExp relExp;

    public RelExp(AddExp addExp) {
        this.addExp = addExp;
        this.relation = null;
        this.relExp = null;
    }

    public RelExp(AddExp addExp, Token relation, RelExp relExp) {
        this.addExp = addExp;
        this.relation = relation;
        this.relExp = relExp;
    }

    public String toString() {
        return expString(addExp,relation,relExp,"RelExp");
    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(addExp.toString());
        output.add("<RelExp>");
        if (relation != null) {
            output.add(relation.toString());
            output.add(relExp.toString());
        }

        return output.toString();
    }

     */

    public AddExp getAddExp() {
        return addExp;
    }

    public Token getRelation() {
        return relation;
    }

    public RelExp getRelExp() {
        return relExp;
    }
}
