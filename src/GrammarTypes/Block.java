package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Block implements Node {
    //Block → '{' { BlockItem } '}'
    private Token LBrace;
    private ArrayList<BlockItem> blockItemsList;
    private Token RBrace;

    public Block(Token LBrace, ArrayList<BlockItem> blockItemsList, Token RBrace) {
        this.LBrace = LBrace;
        this.blockItemsList = blockItemsList;
        this.RBrace = RBrace;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(LBrace.toString());
        for (BlockItem blockItem: blockItemsList) {
            output.add(blockItem.toString());
        }
        output.add(RBrace.toString());
        output.add("<Block>");
        return output.toString();
    }
}
