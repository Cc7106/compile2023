package GrammarTypes;

import Token.Token;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Stmt extends Node {
    // Stmt → LVal '=' Exp ';'
    // Stmt → [Exp] ';'
    // Stmt → Block
    // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    // 'break' ';' | 'continue' ';'
    // 'return' [Exp] ';'
    // LVal '=' 'getint''('')'';'
    // 'printf''('FormatString{','Exp}')'';'
    private LVal lVal;
    private Token equal;
    private Exp exp;
    private Block block;
    private Token LParent;
    private Token RParent;
    private Token ifTk;
    private Token elseTk;
    private Token forTk;
    private Token breakTk;
    private Token continueTk;
    private Token returnTk;
    private Token getIntTk;
    private Token printfTk;
    private Token strcon;
    private Token semicn1;
    private Token semicn2;
    private Stmt stmt1;
    private Stmt stmt2;
    private ForStmt forStmt1;
    private ForStmt forStmt2;
    private Cond cond;
    private ArrayList<Exp> expList;
    private ArrayList<Token> commasList;

    public Stmt(LVal lval, Token equal, Exp exp, Token semicn) {
        // LVal '=' Exp ';'
        this.lVal = lval;
        this.equal = equal;
        this.exp = exp;
        this.semicn1 = semicn;
    }

    public Stmt(Exp exp, Token semicn) {
        // Exp ';'
        this.exp = exp;
        this.semicn1 = semicn;
    }

    public Stmt(Token semicn) {
        //  ';'
        this.semicn1 = semicn;
    }

    public Stmt(Block block) {
        //  Block
        this.block = block;
    }

    public Stmt(Token ifTk, Token LParent, Cond cond, Token RParent, Stmt stmt) {
        //  'if' '(' Cond ')' Stmt
        this.ifTk = ifTk;
        this.LParent = LParent;
        this.cond = cond;
        this.RParent = RParent;
        this.stmt1 = stmt;
    }

    public Stmt(Token ifTk, Token LParent, Cond cond, Token RParent, Stmt stmt, Token elseTk, Stmt stmt2) {
        //  'if' '(' Cond ')' Stmt 'else' Stmt
        this.ifTk = ifTk;
        this.LParent = LParent;
        this.cond = cond;
        this.RParent = RParent;
        this.stmt1 = stmt;
        this.elseTk = elseTk;
        this.stmt2 = stmt2;

    }

    public Stmt(Token forTk, Token LParent, ForStmt forStmt1, Token semicn1, Cond cond, Token semicn2, ForStmt forStmt2, Token RParent, Stmt stmt) {
        // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        this.forTk = forTk;
        this.LParent = LParent;
        this.forStmt1 = forStmt1;
        this.semicn1 = semicn1;
        this.cond = cond;
        this.semicn2 = semicn2;
        this.forStmt2 = forStmt2;
        this.RParent = RParent;
        this.stmt1 = stmt;
    }

    public Stmt(Token brkOrCont, Token semicn) {
        //'break' ';' | 'continue' ';'
        if (brkOrCont.getToken().equals("break")) {
            this.breakTk = brkOrCont;
        } else {
            this.continueTk = brkOrCont;
        }
        this.semicn1 = semicn;
    }

    public Stmt(Token returnTk, Exp exp, Token semicn) {
        // 'return' [Exp] ';'
        this.returnTk = returnTk;
        this.exp = exp;
        this.semicn1 = semicn;
    }

    public Stmt(LVal lVal, Token equal, Token getIntTk, Token LParent, Token RParent, Token semicn) {
        //  LVal '=' 'getint''('')'';'
        this.lVal = lVal;
        this.equal = equal;
        this.getIntTk = getIntTk;
        this.LParent = LParent;
        this.RParent = RParent;
        this.semicn1 = semicn;
    }

    public Stmt(Token printfTk, Token LParent, Token strcon, Token RParent, Token semicn1) {
        // 'printf''('FormatString')'';'
        this.printfTk = printfTk;
        this.LParent = LParent;
        this.strcon = strcon;
        this.RParent = RParent;
        this.semicn1 = semicn1;
    }

    public Stmt(Token printfTk, Token LParent, Token strcon, ArrayList<Token> commasList, ArrayList<Exp> expList, Token RParent, Token semicn1) {
        // 'printf''('FormatString{','Exp}')'';'
        this.printfTk = printfTk;
        this.LParent = LParent;
        this.strcon = strcon;
        this.commasList = commasList;
        this.expList = expList;
        this.RParent = RParent;
        this.semicn1 = semicn1;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (ifTk != null) {
            //'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            output.add(ifTk.toString());  //if
            output.add(LParent.toString());  //'('
            output.add(cond.toString());  //cond
            output.add(RParent.toString()); // ')'
            output.add(stmt1.toString()); // stmt
            if (elseTk != null) {
                output.add(elseTk.toString()); //else
                output.add(stmt2.toString()); //stmt
            }

        } else if (forTk != null) {
            //'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
            output.add(forTk.toString()); //for
            output.add(LParent.toString()); //'('
            if (forStmt1 != null) {
                output.add(forStmt1.toString()); //forStmt
            }
            output.add(semicn1.toString()); //';'
            if (cond != null) {
                output.add(cond.toString()); //cond
            }
            output.add(semicn2.toString()); //';'
            if (forStmt2 != null) {
                output.add(forStmt2.toString()); //forStmt
            }
            output.add(RParent.toString()); //')'
            output.add(stmt1.toString()); //stmt

        } else if (breakTk != null) {
            output.add(breakTk.toString()); //break
            output.add(semicn1.toString()); //';'

        } else if (continueTk != null) {
            output.add(continueTk.toString()); //continue
            output.add(semicn1.toString()); //';'

        } else if (returnTk != null) {
            //'return' [Exp] ';'
            output.add(returnTk.toString());  //return
            if (exp != null) {
                output.add(exp.toString()); //exp
            }
            output.add(semicn1.toString()); //';'

        } else if (printfTk != null) {
            // 'printf' '('FormatString { ',' Exp } ')' ';'
            output.add(printfTk.toString()); //printf
            output.add(LParent.toString()); //'('
            output.add(strcon.toString());  //formatString
            if (!commasList.isEmpty()) {
                int n = commasList.size();
                for (int i = 0; i < n; i++) {
                    output.add(commasList.get(i).toString()); //','
                    output.add(expList.get(i).toString());  //exp
                }
            }
            output.add(RParent.toString()); //')'
            output.add(semicn1.toString()); //';'

        } else if (block != null) {
            output.add(block.toString()); //block

        } else if (lVal != null) {
            //LVal '=' Exp ';'
            //LVal '=' 'getint' '(' ')' ';'
            output.add(lVal.toString());  //lval
            output.add(equal.toString()); //'='
            if (exp != null) {
                output.add(exp.toString()); //exp
            } else {
                output.add(getIntTk.toString()); //getint
                output.add(LParent.toString()); //'('
                output.add(RParent.toString()); //')'
            }
            output.add(semicn1.toString()); //';'

        } else {
            //[Exp] ';'
            if (exp != null) {
                output.add(exp.toString()); //exp
            }
            output.add(semicn1.toString()); //';'
        }

        output.add("<Stmt>");
        return output.toString();

    }


}
