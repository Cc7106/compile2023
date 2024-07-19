package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Node {
    //private static final ArrayList<Error> errorsList = new ArrayList<>();
    public String toString() {
        return "";
    }

    public String declCommasString(ArrayList<Node> list, ArrayList<Token> commasList, Token semicn, String type) {
        StringJoiner output = new StringJoiner("\n");
        output.add(list.get(0).toString());

        if (!commasList.isEmpty()) {
            int n = commasList.size();
            for (int i = 0; i < n; i++) {
                output.add(commasList.get(i).toString());
                output.add(list.get(i + 1).toString());
            }
        }

        if (semicn != null) {
            output.add(semicn.toString());
        }
        output.add("<" + type + ">");

        return output.toString();
    }

   public String defString(Token ident, Token LBrack1, Node exp1, Token RBrack1, Token LBrack2,
                            Node exp2, Token RBrack2, Token equal, Node initVal, String type) {
       StringJoiner output = new StringJoiner("\n");
       output.add(ident.toString());
       if (LBrack1 != null) {
           output.add(LBrack1.toString());
           output.add(exp1.toString());
           output.add(RBrack1.toString());
       }
       if (LBrack2 != null) {
           output.add(LBrack2.toString());
           output.add(exp2.toString());
           output.add(RBrack2.toString());
       }
       if (equal != null) {
           output.add(equal.toString());
           output.add(initVal.toString());
       }

       output.add("<" + type + ">");
       return output.toString();
   }

    public String expString(Node exp1, Token midToken, Node exp2, String expType) {
        StringJoiner output = new StringJoiner("\n");

        if (midToken != null) {
            output.add(exp2.toString());
            output.add(midToken.toString());
            output.add(exp1.toString());
        } else {
            output.add(exp1.toString());
        }
        output.add("<" + expType + ">");
        return output.toString();
    }

    public String initValString(Node exp, Token LBrace, ArrayList<Node> list, ArrayList<Token> commasList, Token RBrace, String type) {
        StringJoiner output = new StringJoiner("\n");
        if (exp != null) {
            output.add(exp.toString());
        } else {
            output.add(LBrace.toString());
            output.add(list.get(0).toString());
            if (!commasList.isEmpty()) {
                int n = commasList.size();
                for (int i = 0; i < n; i++) {
                    output.add(commasList.get(i).toString());
                    output.add(list.get(i + 1).toString());
                }
            }
            output.add(RBrace.toString());
        }
        output.add("<" + type + ">");
        return output.toString();
    }

//    public void addError(Error error) {
//        errorsList.add(error);
//    }

    public ArrayList<Exp> getExpList() {
        return null;
    }

    public FuncFParams getFuncFParams() {
        return null;
    }

    public ArrayList<FuncFParam> getFuncFParamList() {
        return null;
    }

    public Token getIdent() {
        return null;
    }

    public Token getFuncTypeToken() {
        return null;
    }

    public int getDimension() {
        return 0;
    }

    public Token getBtype() {
        return null;
    }

    public Block getBlock() {
        return null;
    }

    public Token getRParamType() {
        return null;
    }



}
