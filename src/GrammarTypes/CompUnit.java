package GrammarTypes;

import java.util.ArrayList;
import java.util.StringJoiner;

public class CompUnit extends Node {
    // CompUnit → {Decl} {FuncDef} MainFuncDef
    private ArrayList<Decl> declList;
    private ArrayList<FuncDef> funcDefList;
    private MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> declList, ArrayList<FuncDef> funcDefList, MainFuncDef mainFuncDef) {
        this.declList = declList;
        this.funcDefList = funcDefList;
        this.mainFuncDef = mainFuncDef;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (!declList.isEmpty()) {
            for (Decl decl: declList) {
                output.add(decl.toString());
            }
        }
        if (!funcDefList.isEmpty()) {
            for (FuncDef funcDef: funcDefList) {
                output.add(funcDef.toString());
            }
        }
        output.add(mainFuncDef.toString());
        output.add("<CompUnit>\n");
        return output.toString();
    }

}
