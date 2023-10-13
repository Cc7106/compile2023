package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class LAndExp extends Node {
    //LAndExp → EqExp
    //LAndExp → EqExp '&&' LAndExp
    private EqExp eqExp;
    private Token and; //'&&'
    private LAndExp lAndExp;

    public LAndExp(EqExp eqExp) {
        this.eqExp = eqExp;
        this.and = null;
        this.lAndExp = null;
    }

    public LAndExp(EqExp eqExp, Token and, LAndExp lAndExp) {
        this.eqExp = eqExp;
        this.and = and;
        this.lAndExp = lAndExp;
    }

    public String toString() {
        return expString(eqExp,and,lAndExp,"LAndExp");
    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(eqExp.toString());
        output.add("<LAndExp>");
        if (and != null) {
            output.add(and.toString());
            output.add(lAndExp.toString());
        }

        return output.toString();
    }

     */
}
