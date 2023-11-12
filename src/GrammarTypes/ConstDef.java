package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class ConstDef extends Node {
    // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    private Token ident;
    private Token LBrack1;
    private ConstExp ConstExp1;
    private Token RBrack1;
    private Token LBrack2;
    private ConstExp ConstExp2;
    private Token RBrack2;
    private Token equal;
    private ConstInitVal constInitVal;

    public ConstDef(Token ident, Token LBrack1, ConstExp constExp1, Token RBrack1,
                    Token LBrack2, ConstExp constExp2, Token RBrack2, Token equal,
                    ConstInitVal constInitVal) {
        this.ident = ident;
        this.LBrack1 = LBrack1;
        this.ConstExp1 = constExp1;
        this.RBrack1 = RBrack1;
        this.LBrack2 = LBrack2;
        this.ConstExp2 = constExp2;
        this.RBrack2 = RBrack2;
        this.equal = equal;
        this.constInitVal = constInitVal;
    }

    public String toString() {
        return defString(ident, LBrack1, ConstExp1, RBrack1, LBrack2,
                ConstExp2, RBrack2, equal, constInitVal, "ConstDef");

    }
    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(ident.toString());
        if (LBrack1 != null) {
            output.add(LBrack1.toString());
            output.add(ConstExp1.toString());
            output.add(RBrack1.toString());
        }
        if (LBrack2 != null) {
            output.add(LBrack2.toString());
            output.add(ConstExp2.toString());
            output.add(RBrack2.toString());
        }
        output.add(equal.toString());
        output.add(constInitVal.toString());
        output.add("<ConstDef>");

        return output.toString();
    }

     */

    @Override
    public Token getIdent() {
        return ident;
    }



    public ConstExp getConstExp1() {
        return ConstExp1;
    }


    public ConstExp getConstExp2() {
        return ConstExp2;
    }

    public ConstInitVal getConstInitVal() {
        return constInitVal;
    }

    @Override
    public int getDimension() {
        if (LBrack1 == null) {
            return 0;
        } else if (LBrack2 == null) {
            return 1;
        } else {
            return 2;
        }
    }
}
