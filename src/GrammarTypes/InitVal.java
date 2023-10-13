package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class InitVal extends Node {
    // InitVal → Exp
    // InitVal → '{' [ InitVal { ',' InitVal } ] '}' // { , , }
    // InitVal → '{' [ InitVal { ',' InitVal } ] '}' // { {} , {}}
    private Exp exp;
    private Token LBrace;
    private ArrayList<InitVal> initValList;
    private ArrayList<Token> commasList;
    private Token RBrace;

    public InitVal(Exp exp) {
        //常量表达式初值
        this.exp = exp;
        this.LBrace = null;
        this.initValList = null;
        this.commasList = null;
        this.RBrace = null;
    }

    public InitVal(Token LBrace, ArrayList<InitVal> initValList, ArrayList<Token> commasList, Token RBrace) {
        //一维数组初值 二维数组初值
        this.exp = null;
        this.LBrace = LBrace;
        this.initValList = initValList;
        this.commasList = commasList;
        this.RBrace = RBrace;
    }

    public String toString() {
        ArrayList<Node> tempConvertList = new ArrayList<>();
        if (exp == null) {
            tempConvertList.addAll(initValList);
        }
        return initValString(exp, LBrace, tempConvertList, commasList, RBrace, "InitVal");

    }

    /*
    public String toString() {
         StringJoiner output = new StringJoiner("\n");
        if (exp != null) {
            output.add(exp.toString());
        } else {
            output.add(LBrace.toString());
            output.add(initValList.get(0).toString());
            if (!commasList.isEmpty()) {
                int n = commasList.size();
                for (int i = 0; i < n; i++) {
                    output.add(commasList.get(i).toString());
                    output.add(initValList.get(i + 1).toString());
                }
            }
            output.add(RBrace.toString());
        }
        output.add("<InitVal>");
        return output.toString();

    }

     */

}
