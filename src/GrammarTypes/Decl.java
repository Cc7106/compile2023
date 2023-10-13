package GrammarTypes;

import java.util.StringJoiner;

public class Decl extends Node {
    // Decl → ConstDecl
    // Decl → VarDecl
    private ConstDecl constDecl;
    private VarDecl varDecl;

    public Decl(ConstDecl constDecl) {
        this.constDecl = constDecl;
        this.varDecl = null;
    }

    public Decl(VarDecl varDecl) {
        this.constDecl = null;
        this.varDecl = varDecl;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (constDecl != null) {
            output.add(constDecl.toString());
        } else {
            output.add(varDecl.toString());
        }

        return output.toString();
    }

}
