package GrammarTypes;
import Token.Token;

import java.util.StringJoiner;

public class EqExp extends Node {
    // EqExp → RelExp
    // EqExp -> RelExp ('==' | '!=') EqExp
    private RelExp relExp;
    private Token relation; // '==' | '!='
    private EqExp eqExp;

    public EqExp(RelExp relExp) {
        this.relExp = relExp;
        this.relation = null;
        this.eqExp = null;
    }

    public EqExp(RelExp relExp, Token relation, EqExp eqExp) {
        this.relExp = relExp;
        this.relation = relation;
        this.eqExp = eqExp;
    }

    public String toString() {
        return expString(relExp, relation ,eqExp,"EqExp");
    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(relExp.toString());
        output.add("<EqExp>");
        if (relation != null) {
            output.add(relation.toString());
            output.add(eqExp.toString());
        }
        return output.toString();
    }

     */

    public RelExp getRelExp() {
        return relExp;
    }

    public Token getRelation() {
        return relation;
    }

    public EqExp getEqExp() {
        return eqExp;
    }
}
