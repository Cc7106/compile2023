package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class VarDecl implements Node {
    // VarDecl → BType VarDef { ',' VarDef } ';'
    private Btype btype;
    private ArrayList<VarDef> varDef;
    private ArrayList<Token> commasList;
    private Token semicn;

    public VarDecl(Btype btype, ArrayList<VarDef> varDef, ArrayList<Token> commasList, Token semicn) {
        this.btype = btype;
        this.varDef = varDef;
        this.commasList = commasList;
        this.semicn = semicn;
    }

    public String toString () {
        StringJoiner output = new StringJoiner("\n");
        output.add(btype.toString());

        ArrayList<Node> tempConvertList = new ArrayList<>(varDef);
        output.add(declCommasString(tempConvertList, commasList, semicn, "VarDecl"));

        return output.toString();
    }
    /*
    public String toString () {
        StringJoiner output = new StringJoiner("\n");
        output.add(btype.toString());
        output.add(varDef.get(0).toString());
        if (!commasList.isEmpty()) {
            int n = commasList.size();
            for (int i = 0; i < n; i++) {
                output.add(commasList.get(i).toString());
                output.add(varDef.get(i + 1).toString());
            }
        }
        output.add(semicn.toString());

        output.add("<VarDecl>");
        return output.toString();
    }

     */
}
