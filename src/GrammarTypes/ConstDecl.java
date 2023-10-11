package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class ConstDecl implements Node {
    // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    private Token constTk;
    private Btype btype;
    private ArrayList<ConstDef> constDef;
    private ArrayList<Token> commasList;
    private Token semicn;

    public ConstDecl(Token constTk, Btype btype, ArrayList<ConstDef> constDef, ArrayList<Token> commasList, Token semicn) {
        //多个声明有，
        this.constTk = constTk;
        this.btype = btype;
        this.constDef = constDef;
        this.commasList = commasList;
        this.semicn = semicn;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(constTk.toString());
        output.add(btype.toString());

        ArrayList<Node> tempConvertList = new ArrayList<>(constDef);
        output.add(declCommasString(tempConvertList, commasList, semicn, "ConstDecl"));

        return output.toString();
    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(constTk.toString());
        output.add(btype.toString());
        output.add(constDef.get(0).toString());

        if (!commasList.isEmpty()) {
            int n = commasList.size();
            for (int i = 0; i < n; i++) {
                output.add(commasList.get(i).toString());
                output.add(constDef.get(i + 1).toString());
            }
        }
        output.add(semicn.toString());
        output.add("<ConstDecl>");
        return output.toString();
    }

     */
}
