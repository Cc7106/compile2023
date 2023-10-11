package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class MulExp implements Node {
    // MulExp → UnaryExp
    // MulExp → UnaryExp ('*' | '/' | '%') MulExp
    private UnaryExp unaryExp;
    private Token op;
    private MulExp mulExp;

    public MulExp(UnaryExp unaryExp) {
        this.unaryExp = unaryExp;
        this.op = null;
        this.mulExp = null;
    }

    public MulExp(UnaryExp unaryExp, Token op, MulExp mulExp) {
        this.unaryExp = unaryExp;
        this.op = op;
        this.mulExp = mulExp;
    }

    public String toString() {
        return expString(unaryExp,op,mulExp,"MulExp");
    }
    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(unaryExp.toString());
        output.add("<MulExp>");
        if (op != null) {
            output.add(op.toString());
            output.add(mulExp.toString());
        }

        return output.toString();
    }

     */
}
