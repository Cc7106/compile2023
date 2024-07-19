package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class ConstInitVal extends Node {
    // ConstInitVal → ConstExp
    // ConstInitVal → '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
    private ConstExp constExp;
    private Token LBrace;
    private ArrayList<ConstInitVal> constInitValList;
    private ArrayList<Token> commasList;
    private Token RBrace;

    public ConstInitVal(ConstExp constExp) {
        //常量表达式初值
        this.constExp = constExp;
        this.LBrace = null;
        this.constInitValList = null;
        this.commasList = null;
        this.RBrace = null;
    }

    public ConstInitVal(Token LBrace, ArrayList<ConstInitVal> constInitValList, ArrayList<Token> commasList, Token RBrace) {
        //一维数组初值 二维数组初值
        this.constExp = null;
        this.LBrace = LBrace;
        this.constInitValList = constInitValList;
        this.commasList = commasList;
        this.RBrace = RBrace;
    }

    public String toString() {
        ArrayList<Node> tempConvertList = new ArrayList<>();
        if (constExp == null) {
            tempConvertList.addAll(constInitValList);
        }
        return initValString(constExp, LBrace, tempConvertList, commasList, RBrace, "ConstInitVal");

    }

    public ConstExp getConstExp() {
        return constExp;
    }

    public ArrayList<ConstInitVal> getConstInitValList() {
        return constInitValList;
    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (constExp != null) {
            output.add(constExp.toString());
        } else {
            output.add(LBrace.toString());
            output.add(constInitValList.get(0).toString());

            if (!commasList.isEmpty()) {
                int n = commasList.size();
                for (int i = 0; i < n; i++) {
                    output.add(commasList.get(i).toString());
                    output.add(constInitValList.get(i + 1).toString());
                }
            }
            output.add(RBrace.toString());

        }

        output.add("<ConstInitVal>");
        return output.toString();
    }

     */
}
