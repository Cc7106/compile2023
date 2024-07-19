package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class AddExp extends Node {
    //AddExp → MulExp
    //AddExp → MulExp ('+' | '−') AddExp

    private MulExp mulExp;
    private Token op;
    private AddExp addExp;

    public AddExp(MulExp mulExp) {
        this.mulExp = mulExp;
        this.op = null;
        this.addExp = null;
    }

    public AddExp(MulExp mulExp, Token op, AddExp addExp) {
        this.mulExp = mulExp;
        this.op = op;
        this.addExp = addExp;
    }

    /*
    public String toString() {

        StringJoiner output = new StringJoiner("\n");
        output.add(mulExp.toString());
        output.add("<AddExp>");
        if (op != null) {
            output.add(op.toString());
            output.add(addExp.toString());
        }
        return output.toString();
    }
     */

    @Override
    public String toString() {
        return expString(mulExp,op,addExp,"AddExp");
    }

    public Token getRParamType() {
        return mulExp.getRParamType();
    }

    @Override
    public int getDimension() {
        return mulExp.getDimension();
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public MulExp getMulExp() {
        return mulExp;
    }

    public Token getOp() {
        return op;
    }
}
