package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class UnaryExp implements Node {
    //UnaryExp → PrimaryExp
    //UnaryExp → Ident '(' [FuncRParams] ')'
    //UnaryExp → UnaryOp UnaryExp
    private PrimaryExp primaryExp;
    private Token ident;
    private Token LParent;
    private FuncRParams funcRParams;
    private Token RParent;

    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public  UnaryExp(PrimaryExp primaryExp) {
        this.primaryExp = primaryExp;
        this.ident = null;
        this.LParent = null;
        this.funcRParams = null;
        this.RParent = null;
        this.unaryOp = null;
        this.unaryExp = null;
    }
    public UnaryExp(Token ident, Token lParent, FuncRParams funcRParams, Token rParent) {
        this.primaryExp = null;
        this.ident = ident;
        this.LParent = lParent;
        this.funcRParams = funcRParams;
        this.RParent = rParent;
        this.unaryOp = null;
        this.unaryExp = null;
    }

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.primaryExp = null;
        this.ident = null;
        this.LParent = null;
        this.funcRParams = null;
        this.RParent = null;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (primaryExp != null) {
            output.add(primaryExp.toString());
        } else if (ident != null) {
            output.add(ident.toString());
            output.add(LParent.toString());
            if (funcRParams != null) {
                output.add(funcRParams.toString());
            }
            output.add(RParent.toString());
        } else {
            output.add(unaryOp.toString());
            output.add(unaryExp.toString());
        }

        output.add("<UnaryExp>");
        return output.toString();
    }
}
