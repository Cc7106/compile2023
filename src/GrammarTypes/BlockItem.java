package GrammarTypes;

import java.util.StringJoiner;

public class BlockItem implements Node {
    //BlockItem → Decl
    //BlockItem → Stmt

    private Decl decl;
    private Stmt stmt;

    public BlockItem(Decl decl) {
        this.decl = decl;
        this.stmt = null;
    }

    public BlockItem(Stmt stmt) {
        this.decl = null;
        this.stmt = stmt;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (decl != null) {
            output.add(decl.toString());
        } else {
            output.add(stmt.toString());
        }

        return output.toString();
    }
}
