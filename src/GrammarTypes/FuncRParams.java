package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class FuncRParams implements Node {
    //FuncRParams → Exp { ',' Exp }

    private ArrayList<Exp> expsList;
    private ArrayList<Token> commasList;

    public FuncRParams(ArrayList<Exp> expsList, ArrayList<Token> commasList) {
        //
        this.expsList = expsList;
        this.commasList = commasList;
    }
    public String toString() {

        ArrayList<Node> tempConvertList = new ArrayList<>(expsList);
        return declCommasString(tempConvertList, commasList, null, "FuncRParams");

    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(expsList.get(0).toString());
        if (!commasList.isEmpty()) {
            int n = commasList.size();
            for (int i = 0; i < n; i++) {
                output.add(commasList.get(i).toString());
                output.add(expsList.get(i + 1).toString());
            }
        }
        output.add("<FuncRParams>");
        return output.toString();
    }

     */
}
