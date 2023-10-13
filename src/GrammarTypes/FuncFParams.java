package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class FuncFParams extends Node {
    // FuncFParams → FuncFParam { ',' FuncFParam }
    private ArrayList<FuncFParam> funcFParamList;

    private ArrayList<Token> commasList;

    public FuncFParams(ArrayList<FuncFParam> funcFParamList) {
        //一个
        this.funcFParamList = funcFParamList;
        this.commasList = null;
    }

    public FuncFParams(ArrayList<FuncFParam> funcFParamList, ArrayList<Token> commaList) {
        //多个
        this.funcFParamList = funcFParamList;
        this.commasList = commaList;
    }

    public String toString() {
        ArrayList<Node> tempConvertList = new ArrayList<>(funcFParamList);
        return declCommasString(tempConvertList, commasList, null, "FuncFParams");

    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(funcFParamList.get(0).toString());
        if (!commasList.isEmpty()) {
            int n = commasList.size();
            for (int i = 0; i < n; i++) {
                output.add(commasList.get(i).toString());
                output.add(funcFParamList.get(i + 1).toString());
            }
        }
        output.add("<FuncFParams>");
        return output.toString();
    }

     */
}
