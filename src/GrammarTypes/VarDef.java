package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class VarDef extends Node {
    // VarDef → Ident { '[' ConstExp ']' }
    // VarDef → Ident { '[' ConstExp ']' } '=' InitVal

    private Token ident;
    private Token LBrack1;
    private ConstExp constExp1;
    private Token RBrack1;
    private Token LBrack2;
    private ConstExp constExp2;
    private Token RBrack2;
    private  Token equal;
    private InitVal initVal;

    public VarDef(Token ident) {
        this.ident = ident;
        this.LBrack1 = null;
        this.constExp1 = null;
        this.RBrack1 = null;
        this.LBrack2 = null;
        this.constExp2 = null;
        this.RBrack2 = null;
        this.equal = null;
        this.initVal = null;
    }

    public VarDef(Token ident, Token lBrack1, ConstExp constExp1, Token rBrack1) {
        this.ident = ident;
        this.LBrack1 = lBrack1;
        this.constExp1 = constExp1;
        this.RBrack1 = rBrack1;
        this.LBrack2 = null;
        this.constExp2 = null;
        this.RBrack2 = null;
        this.equal = null;
        this.initVal = null;
    }

    public VarDef(Token ident, Token lBrack1, ConstExp constExp1, Token rBrack1, Token lBrack2, ConstExp constExp2, Token rBrack2) {
        this.ident = ident;
        this.LBrack1 = lBrack1;
        this.constExp1 = constExp1;
        this.RBrack1 = rBrack1;
        this.LBrack2 = lBrack2;
        this.constExp2 = constExp2;
        this.RBrack2 = rBrack2;
        this.equal = null;
        this.initVal = null;
    }

    public VarDef(Token ident, Token equal, InitVal initVal) {
        this.ident = ident;
        this.LBrack1 = null;
        this.constExp1 = null;
        this.RBrack1 = null;
        this.LBrack2 = null;
        this.constExp2 = null;
        this.RBrack2 = null;
        this.equal = equal;
        this.initVal = initVal;
    }

    public VarDef(Token ident, Token lBrack1, ConstExp constExp1, Token rBrack1, Token equal, InitVal initVal) {
        this.ident = ident;
        this.LBrack1 = lBrack1;
        this.constExp1 = constExp1;
        this.RBrack1 = rBrack1;
        this.LBrack2 = null;
        this.constExp2 = null;
        this.RBrack2 = null;
        this.equal = equal;
        this.initVal = initVal;
    }

    public VarDef(Token ident, Token lBrack1, ConstExp constExp1, Token rBrack1, Token lBrack2, ConstExp constExp2, Token rBrack2, Token equal, InitVal initVal) {
        this.ident = ident;
        this.LBrack1 = lBrack1;
        this.constExp1 = constExp1;
        this.RBrack1 = rBrack1;
        this.LBrack2 = lBrack2;
        this.constExp2 = constExp2;
        this.RBrack2 = rBrack2;
        this.equal = equal;
        this.initVal = initVal;
    }

    public String toString() {
        return defString(ident, LBrack1, constExp1, RBrack1, LBrack2,
                constExp2, RBrack2, equal, initVal, "VarDef");

    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(ident.toString());
        if (LBrack1 != null) {
            output.add(LBrack1.toString());
            output.add(constExp1.toString());
            output.add(RBrack1.toString());
        }
        if (LBrack2 != null) {
            output.add(LBrack2.toString());
            output.add(constExp2.toString());
            output.add(RBrack2.toString());
        }
        if (equal != null) {
            output.add(equal.toString());
            output.add(initVal.toString());
        }

        output.add("<VarDef>");
        return output.toString();
    }

     */

    @Override
    public Token getIdent() {
        return ident;
    }

    public ConstExp getConstExp1() {
        return constExp1;
    }

    public ConstExp getConstExp2() {
        return constExp2;
    }

    public InitVal getInitVal() {
        return initVal;
    }

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
