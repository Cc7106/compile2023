package GrammarTypes;
import Token.Token;

import java.util.StringJoiner;

public class LOrExp extends Node {

    //LOrExp → LAndExp
    //LOrExp → LAndExp '||' LOrExp
    private LAndExp lAndExp;
    private Token or; // '||'
    private LOrExp lOrExp;

    public LOrExp(LAndExp lAndExp) {
        this.lAndExp = lAndExp;
        this.or = null;
        this.lOrExp = null;
    }

    public LOrExp(LAndExp lAndExp, Token or, LOrExp lOrExp) {
        this.lAndExp = lAndExp;
        this.or = or;
        this.lOrExp = lOrExp;
    }

    public String toString() {
        return expString(lAndExp,or,lOrExp,"LOrExp");
    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(lAndExp.toString());
        output.add("<LOrExp>");
        if (or != null) {
            output.add(or.toString());
            output.add(lOrExp.toString());
        }

        return output.toString();
    }

     */

    public LAndExp getlAndExp() {
        return lAndExp;
    }

    public LOrExp getlOrExp() {
        return lOrExp;
    }
}
