package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class MainFuncDef implements Node {
    //MainFuncDef → 'int' 'main' '(' ')' Block
    private Token intTk;
    private Token mainTk;
    private Token lParent;
    private Token rParent;
    private Block block;

    public MainFuncDef(Token intTk, Token mainTk, Token lParent, Token rParent, Block block) {
        this.intTk = intTk;
        this.mainTk = mainTk;
        this.lParent = lParent;
        this.rParent = rParent;
        this.block = block;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(intTk.toString());
        output.add(mainTk.toString());
        output.add(lParent.toString());
        output.add(rParent.toString());
        output.add(block.toString());

        output.add("<MainFuncDef>");
        return output.toString();
    }

}
