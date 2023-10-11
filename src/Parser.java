import GrammarTypes.*;
import GrammarTypes.Number;
import Token.Token;
import Token.TokenType;

import java.util.ArrayList;
import java.util.Objects;

public class Parser {
    private ArrayList<Token> allTokenList;
    private Token curToken;
    private int curPos = 0;
    private CompUnit compUnit;

    public Parser(ArrayList<Token> allTokenList) {
        this.allTokenList = allTokenList;
        this.curToken = allTokenList.get(0);
        compUnit = parseCompUnit();
    }

    private void nextToken() {
        curPos++;
        if (curPos < allTokenList.size()) {
            curToken = allTokenList.get(curPos);
        }
    }

    private Token setTokenAndNext() {
        Token token = curToken;
        nextToken();
        return token;
    }

    private Token preRead() {
        if (curPos + 1 < allTokenList.size()) {
            return allTokenList.get(curPos + 1);
        }
        return null;
    }

    private Token prePreRead() {
        if (curPos + 2 < allTokenList.size()) {
            return allTokenList.get(curPos + 2);
        }
        return null;
    }

    public CompUnit parseCompUnit() {
        //CompUnit -> {Decl} {FuncDef} MainFuncDef

        ArrayList<Decl> declList = new ArrayList<>();
        ArrayList<FuncDef> funcDefList = new ArrayList<>();
        MainFuncDef mainFuncDef;

        while (!isMainFunc()) {
            if (curToken.getTokenType().equals(TokenType.INTTK) &&
                    Objects.requireNonNull(preRead()).getTokenType().equals(TokenType.IDENFR)) {

                //int ident
                if (Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.LPARENT)) {
                    // '('
                    funcDefList.add(parseFuncDef());
                } else if (Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.ASSIGN) ||
                        Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.SEMICN) ||
                        Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.LBRACK) ||
                        Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.COMMA)) {
                    // '[' || '=' || ',' || ';'
                    declList.add(parseDecl());
                } else {
                    error("compUnit");
                }
            } else if (curToken.getTokenType().equals(TokenType.CONSTTK)) {
                //const
                declList.add(parseDecl());
            } else if (curToken.getTokenType().equals(TokenType.VOIDTK)) {
                //void
                funcDefList.add(parseFuncDef());
            } else {
                error("compUnit");
            }
        }
        mainFuncDef = parseMainFuncDef();
        return new CompUnit(declList, funcDefList, mainFuncDef);
    }

    private MainFuncDef parseMainFuncDef() {
        //MainFuncDef -> 'int' 'main' '(' ')' Block
        Token intTk, mainTk, LParent, RParent;
        Block block;

        if (curToken.getTokenType().equals(TokenType.INTTK)) {
            intTk = setTokenAndNext();
        } else {
           error("mainFuncDef miss int");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.MAINTK)) {
            mainTk = setTokenAndNext();
        } else {
            error("mainFuncDef miss main");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.LPARENT)) {
            LParent = setTokenAndNext();
            if (curToken.getTokenType().equals(TokenType.RPARENT)) {
                RParent = setTokenAndNext();
            } else {
                error("mainFuncDef miss )");
                return null;
            }
        } else {
            error("mainFuncDef miss (");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.LBRACE)) {
            block = parseBlock();
        } else {
            error("mainFuncDef miss block");
            return null;
        }
        return new MainFuncDef(intTk, mainTk, LParent, RParent, block);
    }

    private Boolean isMainFunc() {
        // int main '('
        if (curToken.getTokenType().equals(TokenType.INTTK) &&
                Objects.requireNonNull(preRead()).getTokenType().equals(TokenType.MAINTK) &&
                Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.LPARENT)) {
            return true;
        } else {
            return false;
        }
    }

    public Decl parseDecl() {
        // Decl → ConstDecl
        // Decl → VarDecl
        if (curToken.getTokenType().equals(TokenType.CONSTTK)) {
            ConstDecl constDecl = parseConstDecl();
            return new Decl(constDecl);
        } else if (curToken.getTokenType().equals(TokenType.INTTK)) {
            VarDecl varDecl = parseVarDecl();
            return new Decl(varDecl);
        } else {
            error("parseDecl");
            return null;
        }
    }


    private ConstDecl parseConstDecl() {
        //ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        Token constTk, semicn;
        Btype btype;
        ArrayList<ConstDef> constDefList = new ArrayList<>();
        ArrayList<Token> commasList = new ArrayList<>();
        if (curToken.getTokenType().equals(TokenType.CONSTTK)) {
            constTk = setTokenAndNext();
            if (curToken.getTokenType().equals(TokenType.INTTK)) {
                btype = parseBtype();
                constDefList.add(parseConstDef());

                while (curToken.getTokenType().equals(TokenType.COMMA)) {
                    commasList.add(setTokenAndNext());
                    constDefList.add(parseConstDef());
                }

                if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                    semicn = setTokenAndNext();
                    return new ConstDecl(constTk,btype,constDefList,commasList,semicn);
                } else {
                    error("constDecl miss ;");
                    return null;
                }
            } else {
                error("constDecl miss int");
                return null;
            }
        } else {
            error("ConstDecl");
            return null;
        }
    }

    private VarDecl parseVarDecl() {
        //VarDecl -> BType VarDef { ',' VarDef } ';'
        Btype btype;
        ArrayList<VarDef> varDefList = new ArrayList<>();
        ArrayList<Token> commasList = new ArrayList<>();
        Token semicn;
        if (curToken.getTokenType().equals(TokenType.INTTK)) {
            btype = parseBtype();
            varDefList.add(parseVarDef());

            while (curToken.getTokenType().equals(TokenType.COMMA)) {
                commasList.add(setTokenAndNext());
                varDefList.add(parseVarDef());
            }

            if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                semicn = setTokenAndNext();
                return new VarDecl(btype,varDefList,commasList,semicn);
            } else {
                error("varDecl miss ;");
                return null;
            }
        } else {
           error("varDecl");
            return null;
        }
    }

    private VarDef parseVarDef() {
        // VarDef → Ident { '[' ConstExp ']' }
        // VarDef → Ident { '[' ConstExp ']' } '=' InitVal

        Token ident, LBrack1 = null, RBrack1 = null, LBrack2 = null, RBrack2 = null, assign = null;
        ConstExp constExp1 = null, constExp2 = null;
        InitVal initVal = null;

        if (curToken.getTokenType().equals(TokenType.IDENFR)) {
            ident = setTokenAndNext();

            //一维数组
            if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                LBrack1 = setTokenAndNext(); //'['
                constExp1 = parseConstExp();
                if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                    RBrack1 = setTokenAndNext();  //']'
                } else {
                    error("varDef miss ]");
                    return null;
                }
            }

            //二维数组
            if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                LBrack2 = setTokenAndNext(); //'['
                constExp2 = parseConstExp();
                if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                    RBrack2 = setTokenAndNext(); //']'
                } else {
                    error("varDef miss ]");
                    return null;
                }
            }
            
            if (curToken.getTokenType().equals(TokenType.ASSIGN)) {
                assign = setTokenAndNext();
                initVal = parseInitVal();
            }
            return new VarDef(ident,LBrack1,constExp1,RBrack1,LBrack2,constExp2,RBrack2,assign,initVal);
        } else {
            error("varDef");
            return null;
        }
    }

    private InitVal parseInitVal() {
        //InitVal -> Exp | '{' [ InitVal { ',' InitVal } ] '}'
        Exp exp;
        Token LBrace, RBrace;
        ArrayList<InitVal> initValsList = new ArrayList<>();
        ArrayList<Token> commasList = new ArrayList<>();

        if (!curToken.getTokenType().equals(TokenType.LBRACE)) {
            exp = parseExp();
            return new InitVal(exp);
        } else {
            //'{' [ InitVal { ',' InitVal } ] '}
            LBrace = setTokenAndNext(); //'{'
            initValsList.add(parseInitVal());
            while (curToken.getTokenType().equals(TokenType.COMMA)) {
                commasList.add(setTokenAndNext()); //','
                initValsList.add(parseInitVal());
            }

            if (curToken.getTokenType().equals(TokenType.RBRACE)) {
                RBrace = setTokenAndNext(); //'}'
                return new InitVal(LBrace, initValsList, commasList, RBrace);
            } else {
                error("InitVal less }");
                return null;
            }
        }
    }


    private ConstDef parseConstDef() {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        Token ident, LBrack1 = null, RBrack1 = null, LBrack2 = null, RBrack2 = null, assign;
        ConstExp constExp1 = null, constExp2 = null;
        ConstInitVal constInitVal;

        if (curToken.getTokenType().equals(TokenType.IDENFR)) {
            ident = setTokenAndNext();

            //一维数组
            if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                LBrack1 = setTokenAndNext(); //'['
                constExp1 = parseConstExp();
                if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                    RBrack1 = setTokenAndNext(); //']'
                } else {
                    error("constDef miss ]");
                    return null;
                }
            }

            //二维数组
            if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                LBrack2 = setTokenAndNext();  //'['
                constExp2 = parseConstExp();
                if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                    RBrack2 = setTokenAndNext();  // ']'
                } else {
                    error("constDef miss ]");
                    return null;
                }
            }

            if (curToken.getTokenType().equals(TokenType.ASSIGN)) {
                assign = setTokenAndNext();
                constInitVal = parseConstInitVal();
            } else {
                error("constDef miss =");
                return null;
            }
            return new ConstDef(ident, LBrack1, constExp1, RBrack1, LBrack2, constExp2, RBrack2, assign, constInitVal);


        } else {
            error("constDef");
            return null;
        }

    }

    private ConstInitVal parseConstInitVal() {
        // ConstInitVal → ConstExp
        // ConstInitVal → '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        ConstExp constExp;
        Token LBrace, RBrace;
        ArrayList<ConstInitVal> constInitValsList = new ArrayList<>();
        ArrayList<Token> commasList = new ArrayList<>();

        if (!curToken.getTokenType().equals(TokenType.LBRACE)) {
            constExp = parseConstExp();
            return new ConstInitVal(constExp);
        } else {
            //'{' [ ConstInitVal { ',' ConstInitVal } ] '}'
            LBrace = setTokenAndNext(); //'{'
            constInitValsList.add(parseConstInitVal());
            while (curToken.getTokenType().equals(TokenType.COMMA)) {
                commasList.add(setTokenAndNext()); //','
                constInitValsList.add(parseConstInitVal());
            }

            if (curToken.getTokenType().equals(TokenType.RBRACE)) {
                RBrace = setTokenAndNext(); //'}'
                return new ConstInitVal(LBrace, constInitValsList, commasList, RBrace);
            } else {
                error("ConstInitVal less }");
                return null;
            }
        }
    }

    private ConstExp parseConstExp() {
        //ConstExp -> AddExp
        AddExp addExp = parseAddExp();
        return new ConstExp(addExp);
    }

    private FuncDef parseFuncDef() {
        //FuncDef -> FuncType Ident '(' [FuncFParams] ')' Block
        FuncType funcType;
        Token ident, LParent, RParent;
        FuncFParams funcFParams = null;
        Block block;

        if (curToken.getTokenType().equals(TokenType.VOIDTK) || curToken.getTokenType().equals(TokenType.INTTK)) {
            funcType = parseFuncType();
        } else {
            error("FuncDef");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.IDENFR)) {
            ident = setTokenAndNext();
        } else {
            error("FuncDef");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.LPARENT)) {
            LParent = setTokenAndNext(); // '('

            if (curToken.getTokenType().equals(TokenType.INTTK)) {
                funcFParams = parseFuncFParams();
            }

            if (curToken.getTokenType().equals(TokenType.RPARENT)) {
                RParent = setTokenAndNext(); //')'
            } else {
                error("funcDef miss )");
                return null;
            }
        } else {
            error("funcDef miss (");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.LBRACE)) {
            block = parseBlock();
        } else {
            error("funcDef miss block");
            return null;
        }

        return new FuncDef(funcType,ident,LParent,funcFParams,RParent,block);
    }

    private FuncFParams parseFuncFParams() {
        // FuncFParams -> FuncFParam { ',' FuncFParam }
        ArrayList<FuncFParam> funcFParamList = new ArrayList<>();
        ArrayList<Token> commasList = new ArrayList<>();

        if (curToken.getTokenType().equals(TokenType.INTTK)) {
            funcFParamList.add(parseFuncFParam());

            while (curToken.getTokenType().equals(TokenType.COMMA)) {
                commasList.add(setTokenAndNext());
                funcFParamList.add(parseFuncFParam());
            }
            return new FuncFParams(funcFParamList, commasList);
        } else {
            error("funcFParams");
            return null;
        }
    }

    private FuncFParam parseFuncFParam() {

        // FuncFParam → BType Ident
        // FuncFParam → BType Ident '[' ']'
        // FuncFParam → BType Ident '[' ']''[' ConstExp ']'
        Btype btype;
        Token ident, LBrack1 = null, RBrack1 = null, LBrack2 = null, RBrack2 = null;
        ConstExp constExp = null;

        if (curToken.getTokenType().equals(TokenType.INTTK)) {
            btype = parseBtype();

            if (curToken.getTokenType().equals(TokenType.IDENFR)) {
                ident = setTokenAndNext();

                //一维数组
                if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                    LBrack1 = setTokenAndNext();
                    if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                        RBrack1 = setTokenAndNext();
                    } else {
                        error("funcFParam miss ]");
                        return null;
                    }
                }

                //二维数组
                if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                    LBrack2 = setTokenAndNext();
                    constExp = parseConstExp();
                    if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                        RBrack2 = setTokenAndNext();
                    } else {
                        error("funcFParam miss ]");
                        return null;
                    }
                }
            } else {
                error("funcFParam");
                return null;
            }
            return new FuncFParam(btype,ident, LBrack1, RBrack1, LBrack2, constExp, RBrack2);
        } else {
            error("funcFParam");
            return null;
        }
    }

    private FuncType parseFuncType() {
        // FuncType -> 'void' | 'int'
        Token type;
        if (curToken.getTokenType().equals(TokenType.VOIDTK) ||
                curToken.getTokenType().equals(TokenType.INTTK) ) {
            type = setTokenAndNext();
            return new FuncType(type);
        } else {
            error("funcType");
            return null;
        }
    }

    private Block parseBlock() {
        //Block -> '{' { BlockItem } '}'
        Token LBrace, RBrace;
        ArrayList<BlockItem> blockItemsList = new ArrayList<>();
        if (curToken.getTokenType().equals(TokenType.LBRACE)) {
            LBrace = setTokenAndNext();  //'{'

            while (!curToken.getTokenType().equals(TokenType.RBRACE)) {
                blockItemsList.add(parseBlockItem());
            }
            RBrace = setTokenAndNext();  //'}'
            return new Block(LBrace, blockItemsList, RBrace);
        }
        error("block");
        return null;
    }

    private BlockItem parseBlockItem() {
        // BlockItem -> Decl | Stmt
        Decl decl;
        Stmt stmt;
        if (curToken.getTokenType().equals(TokenType.CONSTTK) ||
                curToken.getTokenType().equals(TokenType.INTTK)) {
            decl = parseDecl();
            return new BlockItem(decl);
        } else {
            stmt = parseStmt();
            return new BlockItem(stmt);
        }
    }

    private Stmt parseStmt() {
        //Stmt -> 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        //        | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        //        | 'break' ';' | 'continue' ';'
        //        | 'return' [Exp] ';'
        //        | 'printf' '('FormatString { ',' Exp } ')' ';'
        //         | Block
        //
        //          | LVal '=' Exp ';'
        //          | LVal '=' 'getint' '(' ')' ';'
        //          | [Exp] ';'
        //

        if (curToken.getTokenType().equals(TokenType.IFTK)) {
            //'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            return IfStmt();

        } else if (curToken.getTokenType().equals(TokenType.FORTK)) {
            //'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
            return ForStmt();

        } else if (curToken.getTokenType().equals(TokenType.BREAKTK) || curToken.getTokenType().equals(TokenType.CONTINUETK)) {
            //'break' ';' | 'continue' ';'
            Token token = setTokenAndNext();
            Token semicn;
            if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                semicn = setTokenAndNext();
            } else {
                error("break/continue stmt miss ;");
                return null;
            }
            return new Stmt(token, semicn);

        } else if (curToken.getTokenType().equals(TokenType.RETURNTK)) {
            //'return' [Exp] ';'
            Token returnTk, semicn;
            Exp exp = null;
            returnTk = setTokenAndNext();
            if (!curToken.getTokenType().equals(TokenType.SEMICN)) {
                exp = parseExp();
            }
            if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                semicn = setTokenAndNext();
            } else {
                error("break stmt miss ;");
                return null;
            }
            return new Stmt(returnTk, exp, semicn);

        } else if (curToken.getTokenType().equals(TokenType.PRINTFTK)) {
            // 'printf' '('FormatString { ',' Exp } ')' ';'
            return printfStmt();

        } else if (curToken.getTokenType().equals(TokenType.LBRACE)) {
            // Block
            Block block = parseBlock();
            return new Stmt(block);
        }

        /*
         LVal '=' Exp ';'
         LVal '=' 'getint' '(' ')' ';'
         [Exp] ';'
        */

        if (doLValinStmt()) { //往后扫有出现‘=’
            // LVal '=' Exp ';'
            // LVal '=' 'getint' '(' ')' ';'
            LVal lVal = parseLVal();
            Token assign;
            if (curToken.getTokenType().equals(TokenType.ASSIGN)) {
                assign = setTokenAndNext();
            } else {
                error("lvalStmt miss =");
                return null;
            }

            if (curToken.getTokenType().equals(TokenType.GETINTTK)) {
                Token getintTk, LParent, RParent, semicn;
                getintTk = setTokenAndNext();
                if (curToken.getTokenType().equals(TokenType.LPARENT) &&
                        Objects.requireNonNull(preRead()).getTokenType().equals(TokenType.RPARENT) &&
                        Objects.requireNonNull(prePreRead()).getTokenType().equals(TokenType.SEMICN)) {
                    LParent = setTokenAndNext();
                    RParent = setTokenAndNext();
                    semicn = setTokenAndNext();
                    return new Stmt(lVal, assign, getintTk,LParent, RParent, semicn);
                } else {
                    error("getintStmt miss sth");
                    return null;
                }
            } else {
                Exp exp = parseExp();
                if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                    Token semicn = setTokenAndNext();
                    return new Stmt(lVal, assign, exp, semicn);
                } else {
                    error("lvalExp stmt miss ;");
                    return null;
                }
            }

        } else {
            // [Exp] ';'
            Exp exp = null;
            Token semicn;
            if (!curToken.getTokenType().equals(TokenType.SEMICN)) {
                exp = parseExp();
            }
            if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                semicn = setTokenAndNext();
            } else {
                error("ExpStmt miss ;");
                return null;
            }
            return new Stmt(exp,semicn);
        }
    }

    private Stmt IfStmt() {
        //'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        Token ifTk, LParent, RParent;
        Token elseTk;
        Cond cond;
        Stmt stmt1, stmt2;
        ifTk = setTokenAndNext();
        if (curToken.getTokenType().equals(TokenType.LPARENT)) {
            LParent = setTokenAndNext();
            cond = parseCond();
            if (curToken.getTokenType().equals(TokenType.RPARENT)) {
                RParent = setTokenAndNext();
                stmt1 = parseStmt();
            } else {
                error("ifStmt miss )");
                return null;
            }
        } else {
            error("ifStmt miss (");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.ELSETK)) {
            //有else的情况
            elseTk = setTokenAndNext();
            stmt2 = parseStmt();
            return new Stmt(ifTk, LParent, cond, RParent, stmt1, elseTk, stmt2);
        }
        return new Stmt(ifTk,LParent,cond,RParent,stmt1);
    }

    private Stmt ForStmt() {
        //'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        Token forTk, LParent, semicn1, semicn2, RParent;
        ForStmt forStmt1 = null, forStmt2 = null;
        Cond cond = null;
        Stmt stmt;

        forTk = setTokenAndNext();
        if (curToken.getTokenType().equals(TokenType.LPARENT)) {
            LParent = setTokenAndNext();

            if (!curToken.getTokenType().equals(TokenType.SEMICN)) {
                forStmt1 = parseForStmt();
            }

            if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                semicn1 = setTokenAndNext(); //';'
            } else {
                error("forStmt miss ;-1");
                return null;
            }

            if (!curToken.getTokenType().equals(TokenType.SEMICN)) {
                cond = parseCond();
            }

            if (curToken.getTokenType().equals(TokenType.SEMICN)) {
                semicn2 = setTokenAndNext(); //';'
            } else {
                error("forStmt miss ;-2");
                return null;
            }

            if (!curToken.getTokenType().equals(TokenType.RPARENT)) {
                forStmt2 = parseForStmt();
            }

            if (curToken.getTokenType().equals(TokenType.RPARENT)) {
                RParent = setTokenAndNext(); //')'
            } else {
                error("forStmt miss )");
                return null;
            }
            stmt = parseStmt();
        } else {
            error("forStmt miss (");
            return null;
        }
        return new Stmt(forTk, LParent, forStmt1, semicn1, cond, semicn2, forStmt2, RParent, stmt);
    }

    private Stmt printfStmt() {
        // 'printf' '('FormatString { ',' Exp } ')' ';'
        Token printfTk, LParent, strcon, RParent, semicn;
        ArrayList<Token> commasList = new ArrayList<>();
        ArrayList<Exp> expList = new ArrayList<>();

        printfTk = setTokenAndNext();
        if (curToken.getTokenType().equals(TokenType.LPARENT)) {
            LParent = setTokenAndNext();
        } else {
            error("printStmt miss (");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.STRCON)) {
            strcon = setTokenAndNext();
        } else {
            error("printStmt miss strcon");
            return null;
        }

        while (curToken.getTokenType().equals(TokenType.COMMA)) {
            commasList.add(setTokenAndNext());
            expList.add(parseExp());
        }

        if (curToken.getTokenType().equals(TokenType.RPARENT)) {
            RParent = setTokenAndNext();
        } else {
            error("printStmt miss )");
            return null;
        }

        if (curToken.getTokenType().equals(TokenType.SEMICN)) {
            semicn = setTokenAndNext();
        } else {
            error("printStmt miss ;");
            return null;
        }
        return new Stmt(printfTk,LParent,strcon,commasList,expList,RParent,semicn);
    }

    private Cond parseCond() {
        LOrExp lOrExp = parseLOrExp();
        return new Cond(lOrExp);
    }

    private LOrExp parseLOrExp() {
        //LOrExp -> LAndExp | LAndExp '||' LOrExp
        LAndExp lAndExp;
        Token or;
        LOrExp lOrExp;
        lAndExp = parseLAndExp();
        if (curToken.getTokenType().equals(TokenType.OR)) {
            or = setTokenAndNext();
            lOrExp = parseLOrExp();
            return new LOrExp(lAndExp, or, lOrExp);
        }
        return new LOrExp(lAndExp);
    }

    private LAndExp parseLAndExp() {
        //LAndExp -> EqExp |EqExp '&&' LAndExp
        EqExp eqExp;
        Token and;
        LAndExp lAndExp;
        eqExp = parseEqExp();
        if (curToken.getTokenType().equals(TokenType.AND)) {
            and = setTokenAndNext();
            lAndExp = parseLAndExp();
            return new LAndExp(eqExp, and, lAndExp);
        }
        return new LAndExp(eqExp);
    }

    private EqExp parseEqExp() {
        //EqExp -> RelExp |  RelExp ('==' | '!=') EqExp
        RelExp relExp;
        Token token;
        EqExp eqExp;
        relExp = parseRelExp();
        if (curToken.getTokenType().equals(TokenType.EQL) ||
                curToken.getTokenType().equals(TokenType.NEQ)) {
            token = setTokenAndNext();
            eqExp = parseEqExp();
            return new EqExp(relExp, token, eqExp);
        }
        return new EqExp(relExp);

    }

    private RelExp parseRelExp() {
        //RelExp -> AddExp |AddExp ('<' | '>' | '<=' | '>=') RelExp
        AddExp addExp;
        Token token;
        RelExp relExp;
        addExp = parseAddExp();
        if (curToken.getTokenType().equals(TokenType.GEQ) || curToken.getTokenType().equals(TokenType.GRE) ||
                curToken.getTokenType().equals(TokenType.LEQ) || curToken.getTokenType().equals(TokenType.LSS) ) {
            token = setTokenAndNext();
            relExp = parseRelExp();
            return new RelExp(addExp, token, relExp);
        }
        return new RelExp(addExp);
    }

    private ForStmt parseForStmt() {
        //LVal '=' Exp
        LVal lVal;
        Token assign;
        Exp exp;
        lVal = parseLVal();
        if (curToken.getTokenType().equals(TokenType.ASSIGN)) {
            assign = setTokenAndNext();
        } else {
            error("forStmt miss =");
            return null;
        }
        exp = parseExp();
        return new ForStmt(lVal,assign,exp);
    }

    private Boolean doLValinStmt() {
        // LVal '=' Exp ';'
        // LVal '=' 'getint' '(' ')' ';'
        // [Exp] ';'
        int temp = 0;
        while (curPos + temp < allTokenList.size() &&
                !allTokenList.get(curPos + temp).getTokenType().equals(TokenType.SEMICN)) {
            //';'前有没有出现‘=’
            if (allTokenList.get(curPos + temp).getTokenType().equals(TokenType.ASSIGN)) {
                //do LVal
                return true;
            }
            temp++;
        }
        return false;
    }

    private Btype parseBtype() {
        //int
        Token intTk;
        if (curToken.getTokenType().equals(TokenType.INTTK)) {
            intTk = setTokenAndNext();
            return new Btype(intTk);
        } else {
            error("Btype");
            return null;
        }
    }

    public PrimaryExp parsePrimaryExp() {
        // PrimaryExp → '(' Exp ')'
        // PrimaryExp → LVal
        // PrimaryExp → Number
        if (curToken.getTokenType().equals(TokenType.LPARENT)) {
            // '(' Exp ')'
            Token LParent = setTokenAndNext();
            Exp exp = parseExp();
            if (curToken.getTokenType().equals(TokenType.RPARENT)) {
                Token RParent = setTokenAndNext();
                return new PrimaryExp(LParent,exp,RParent);
            } else {
                error("PrimaryExp less )");
                return null;
            }
        } else if (curToken.getTokenType().equals(TokenType.IDENFR)) {
            //LVal
            LVal lVal = parseLVal();
            return new PrimaryExp(lVal);
        } else if (curToken.getTokenType().equals(TokenType.INTCON)) {
            //Number
            Number number = parseNumber();
            return new PrimaryExp(number);
        } else {
            error("primaryExp");
            return null;
        }

    }

    private LVal parseLVal() {
        if (curToken.getTokenType().equals(TokenType.IDENFR)) {
            Token ident = setTokenAndNext();
            Token LBrack1, RBrack1, LBrack2, RBrack2;
            Exp exp1, exp2;

            //一维数组
            if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                LBrack1 = setTokenAndNext(); //'['
                exp1 = parseExp();
                if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                    RBrack1 = setTokenAndNext(); //']'

                    //二维数组
                    if (curToken.getTokenType().equals(TokenType.LBRACK)) {
                        LBrack2 = setTokenAndNext(); //'['
                        exp2 = parseExp();
                        if (curToken.getTokenType().equals(TokenType.RBRACK)) {
                            RBrack2 = setTokenAndNext(); //']'
                            return new LVal(ident, LBrack1, exp1, RBrack1, LBrack2, exp2, RBrack2);
                        } else {
                            error("LVal less ]");
                            return null;
                        }
                    }

                    return new LVal(ident,LBrack1,exp1,RBrack1);
                } else {
                    error("LVal less ]");
                    return null;
                }
            }
            return new LVal(ident);
        } else {
            error("LVal");
            return null;
        }
    }

    private Exp parseExp() {
        //Exp → AddExp
        AddExp addExp = parseAddExp();
        return new Exp(addExp);
    }

    private AddExp parseAddExp() {
        //AddExp → MulExp
        //AddExp → MulExp ('+' | '−') AddExp

        MulExp mulExp = parseMulExp();

        if (curToken.getTokenType().equals(TokenType.PLUS) || curToken.getTokenType().equals(TokenType.MINU)) {
            //MulExp ('+' | '−') AddExp
            Token op = setTokenAndNext();
            AddExp addExp = parseAddExp();
            return new AddExp(mulExp, op, addExp);
        }
        return new AddExp(mulExp);
    }

    private MulExp parseMulExp() {
        // MulExp → UnaryExp
        // MulExp → UnaryExp ('*' | '/' | '%') MulExp

        UnaryExp unaryExp = parseUnaryExp();
        if (curToken.getTokenType().equals(TokenType.MULT) ||
                curToken.getTokenType().equals(TokenType.DIV) ||
                curToken.getTokenType().equals(TokenType.MOD)) {
            //UnaryExp ('*' | '/' | '%') MulExp
            Token op = setTokenAndNext();
            MulExp mulExp = parseMulExp();
            return new MulExp(unaryExp, op, mulExp);
        }
        return new MulExp(unaryExp);
    }

    private UnaryExp parseUnaryExp() {
        //UnaryExp → PrimaryExp
        //UnaryExp → Ident '(' [FuncRParams] ')'
        //UnaryExp → UnaryOp UnaryExp

        if (curToken.getTokenType().equals(TokenType.IDENFR) &&
                Objects.requireNonNull(preRead()).getTokenType().equals(TokenType.LPARENT)) {
            // Ident '(' [FuncRParams] ')'
            Token ident, LParent, RParent;
            FuncRParams funcRParams = null;
            ident = setTokenAndNext();
            LParent = setTokenAndNext();  //'('
            if (!curToken.getTokenType().equals(TokenType.RPARENT)) {
                funcRParams = parseFuncRParams();
            }
            if (curToken.getTokenType().equals(TokenType.RPARENT)) {
                RParent = setTokenAndNext(); //')'
                return new UnaryExp(ident, LParent, funcRParams, RParent);
            } else {
                error("UnaryExp less )");
                return null;
            }
        } else if (curToken.getTokenType().equals(TokenType.PLUS) || curToken.getTokenType().equals(TokenType.MINU)
                || curToken.getTokenType().equals(TokenType.NOT)) {
            //UnaryOp UnaryExp
            UnaryOp unaryOp = parseUnaryOp();
            UnaryExp unaryExp = parseUnaryExp();
            return new UnaryExp(unaryOp, unaryExp);
        } else if (curToken.getTokenType().equals(TokenType.LPARENT) || curToken.getTokenType().equals(TokenType.IDENFR)
                || curToken.getTokenType().equals(TokenType.INTCON)) {
            //PrimaryExp
            // - > '(' Exp ')' | LVal | Number
            PrimaryExp primaryExp = parsePrimaryExp();
            return new UnaryExp(primaryExp);
        } else {
            error("unaryExp");
            return null;
        }
    }

    private FuncRParams parseFuncRParams() {
        // FuncRParams -> Exp { ',' Exp }
        ArrayList<Exp> expsList = new ArrayList<>();
        ArrayList<Token> commasList = new ArrayList<>();

        expsList.add(parseExp());
        while (curToken.getTokenType().equals(TokenType.COMMA)) {
            commasList.add(setTokenAndNext());
            expsList.add(parseExp());
        }
        return new FuncRParams(expsList, commasList);
    }

    private UnaryOp parseUnaryOp() {
        //UnaryOp -> '+' | '−' | '!'
        Token unaryOp;
        if (curToken.getTokenType().equals(TokenType.PLUS) ||
                curToken.getTokenType().equals(TokenType.MINU) ||
                curToken.getTokenType().equals(TokenType.NOT)) {
            unaryOp = setTokenAndNext();
            return new UnaryOp(unaryOp);
        } else {
            error("unaryOp");
            return null;
        }
    }


    public Number parseNumber() {
        // Number -> IntConst
        if (curToken.getTokenType().equals(TokenType.INTCON)) {
            Token number = setTokenAndNext();
            return new Number(number);
        } else {
            error("Number");
            return null;
        }
    }

    private void error(String type) {
        System.out.println("error " + type);
    }

    public CompUnit getCompUnit() {
        return compUnit;
    }
}
