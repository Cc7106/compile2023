package Error;

import GrammarTypes.*;
import SymbolTable.*;
import Token.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorHandler {
    private ArrayList<Error> errorsList;
    private SymbolTable symbolTable;

    public ErrorHandler(SymbolTable symbolTable) {
        this.errorsList = new ArrayList<>();
        this.symbolTable = symbolTable;
    }

    public void addError(int errorLine, ErrorType errorType) {
        //errorsList.add(new Error(errorLine, errorType));
        addToErrorsList(new Error(errorLine, errorType));
    }

    public void checkErrorA(Token strcon) {
        String string = strcon.getToken();
        boolean gotError = false;
        for (int i = 1; i < string.length() - 1; i++) {
            if (string.charAt(i) == 32 || string.charAt(i) == 33) {
                continue;
            }
            if (string.charAt(i) >= 40 && string.charAt(i) <= 126) {
                if (string.charAt(i) == 92) { // '\n'
                    if (string.charAt(i+1) != 'n') {
                        gotError = true;
                        break;
                    } else {
                        i++;
                    }
                }
                continue;
            }
            if (string.charAt(i) == '%' && string.charAt(i+1) == 'd' ) { //'%d'
                i++;
                continue;
            }
            gotError = true;
            break;
        }

        if (gotError) {
            //errorsList.add(new Error(strcon.getLineNum(), ErrorType.a));
            addToErrorsList(new Error(strcon.getLineNum(), ErrorType.a));
        }

    }

    public void checkErrorL(Stmt stmt, int printfLine) {
        //printf中格式字符与表达式个数不匹配
        int expNum, fStringNum = 0;
        expNum = stmt.getExpList().size();

        String strcon = stmt.getStrcon();
        Pattern pattern = Pattern.compile("%d");
        Matcher matcher = pattern.matcher(strcon);
        while(matcher.find()) {
            fStringNum++;
        }

        if (fStringNum != expNum) {
            //errorsList.add(new Error(printfLine, ErrorType.l));
            addToErrorsList(new Error(printfLine, ErrorType.l));
        }
    }

    public Boolean checkErrorB(String tokenName, int identLine) {
        if (symbolTable.canDefine(tokenName)) {
            return true;
        } else {
            //errorsList.add(new Error(identLine, ErrorType.b));
            addToErrorsList(new Error(identLine, ErrorType.b));
            return false;
        }
    }

//    public void checkErrorG(FuncDef funcDef, int RBraceLine) {
//        if (funcDef.getFuncType().getTokenType().equals(TokenType.INTTK)) {
//            ArrayList<BlockItem> blockItemsList = funcDef.getBlock().getBlockItemsList();
//            int n = blockItemsList.size();
//            BlockItem lastBlockItem = blockItemsList.get(n-1);
//            if (lastBlockItem.getStmt() == null) {
//                errorsList.add(new Error(RBraceLine, ErrorType.g));
//            } else {
//                Stmt stmt = lastBlockItem.getStmt();
//                if (stmt.getReturnTk() == null) {
//                    errorsList.add(new Error(RBraceLine, ErrorType.g));
//                } else if (stmt.getExpList() == null || stmt.getExpList().isEmpty()) {
//                    errorsList.add(new Error(RBraceLine, ErrorType.g));
//                }
//            }
//        }
//    }

    public void checkErrorG(Node node, int RBraceLine) {
        //funcDef(int) / mainFuncDef
        if (node instanceof FuncDef funcDef) {
            if (funcDef.getFuncTypeToken().getTokenType().equals(TokenType.VOIDTK)) {
                return;
            }
        }
        ArrayList<BlockItem> blockItemsList = node.getBlock().getBlockItemsList();
        int n = blockItemsList.size();
        if (n != 0) {
            BlockItem lastBlockItem = blockItemsList.get(n-1);
            if (lastBlockItem.getStmt() == null) {
                //errorsList.add(new Error(RBraceLine, ErrorType.g));
                addToErrorsList(new Error(RBraceLine, ErrorType.g));
            } else {
                Stmt stmt = lastBlockItem.getStmt();
                if (stmt.getReturnTk() == null) {
                    //errorsList.add(new Error(RBraceLine, ErrorType.g));
                    addToErrorsList(new Error(RBraceLine, ErrorType.g));
                }
//            } else if (stmt.getExpList() == null || stmt.getExpList().isEmpty()) {
//                errorsList.add(new Error(RBraceLine, ErrorType.g));
//            }
            }
        } else {
            //errorsList.add(new Error(RBraceLine, ErrorType.g));
            addToErrorsList(new Error(RBraceLine, ErrorType.g));
        }


    }

    public void checkErrorH(LVal lVal, int lValLine) {
        String tokenName = lVal.getIdent().getToken();
        if (symbolTable.isDefined(tokenName)) {
            VarSymbol symbol = symbolTable.getVarSymbol(tokenName);
            if (symbol.getVarType() == 0) {
                //errorsList.add(new Error(lValLine, ErrorType.h));
                addToErrorsList(new Error(lValLine, ErrorType.h));
            }
        }
    }

    public void checkErrorC(String tokenName, int lValLine, int type) {
        // 0 -> var 1->func
        if (!symbolTable.isDefined(tokenName)) {
            //errorsList.add(new Error(lValLine, ErrorType.c));
            addToErrorsList(new Error(lValLine, ErrorType.c));
            return;
        }
        if (type == 1 && symbolTable.getFuncSymbol(tokenName) == null) {

            //errorsList.add(new Error(lValLine, ErrorType.c));
            addToErrorsList(new Error(lValLine, ErrorType.c));
        }
    }

    public Boolean checkErrorD(String tokenName, FuncRParams funcRParams, int lValLine) {
        ArrayList<Exp> expsList = new ArrayList<>();
        if (symbolTable.getFuncSymbol(tokenName) != null) {
            FuncSymbol funcSymbol = symbolTable.getFuncSymbol(tokenName);
            if (funcRParams != null) {
                expsList = funcRParams.getExpsList();
            }
            if(funcSymbol.getParamNum() != expsList.size()) {
                //errorsList.add(new Error(lValLine, ErrorType.d));
                addToErrorsList(new Error(lValLine, ErrorType.d));
                return false;
            }
            return true;
        }
        return false;

    }

    public void checkErrorE(String tokenName, FuncRParams funcRParams, int lValLine) {
        ArrayList<Exp> expsList = new ArrayList<>();
        if (symbolTable.getFuncSymbol(tokenName) != null) {
            FuncSymbol funcSymbol = symbolTable.getFuncSymbol(tokenName);
            if (funcRParams != null) {
                expsList = funcRParams.getExpsList();
            }
            int paramNum = funcSymbol.getParamNum();
            for (int i = 0; i < paramNum; i++) {
                int symbolDim = funcSymbol.getFuncFParamSymbol().get(i).getDimension();

                Exp exp = expsList.get(i);
                Token ident = exp.getRParamType();
                int expDim = exp.getDimension();
                if(!matchDim(symbolDim, ident, expDim)) {
                    //errorsList.add(new Error(lValLine, ErrorType.e));
                    addToErrorsList(new Error(lValLine, ErrorType.e));
                    break;
                }
            }
        }
    }

    public Boolean matchDim(int symbolDim, Token ident, int expDim) {
        int actualExpDim = -1; //真正被传入的维数
        if (ident.getTokenType().equals(TokenType.INTCON)) {
            actualExpDim = 0;
        } else {
            if (symbolTable.isDefined(ident.getToken()) && expDim != -1) { //调用的不是函数
                int identDim = symbolTable.getVarSymbol(ident.getToken()).getDimension();
                actualExpDim = identDim - expDim;
            } else if (symbolTable.isFuncDefined(ident.getToken()) && expDim == -1) {
                //调用的是函数 需要考虑是int 或 void类型
                //funcType 0-> void 1->int
                if (symbolTable.getFuncSymbol(ident.getToken()).getFuncType() == 0) {
                    actualExpDim = -1;
                } else {
                    actualExpDim = 0;
                }
            }
        }
        return symbolDim == actualExpDim;

    }

    public void checkErrorM(int tokenLine) {
        if (!symbolTable.isInLoop()) {
            //errorsList.add(new Error(tokenLine, ErrorType.m));
            addToErrorsList(new Error(tokenLine, ErrorType.m));
        }
    }

    public void checkErrorF(Exp exp, int returnLine) {
        if (symbolTable.isCurVoidFunc() && exp != null) {
            //errorsList.add(new Error(returnLine, ErrorType.f));
            addToErrorsList(new Error(returnLine, ErrorType.f));
        }
    }

    public ArrayList<Error> getErrorsList() {
        return errorsList;
    }

    public void dltErrorsInFunc(int startLine, int endLine) {
        ArrayList<Error> uselessErrors = new ArrayList<>();
        for (Error error : errorsList) {
            if (error.line >= startLine && error.line <= endLine) {
                uselessErrors.add(error);
            }
        }
        errorsList.removeAll(uselessErrors);
    }

    public void addToErrorsList(Error error) {
        for (Error error1: errorsList) {
            if (error1.line == error.line) {
                return;
            }
        }
        int i;
        for (i = 0; i < errorsList.size(); i++) {
            if (error.line < errorsList.get(i).line) {
                break;
            }
        }
        errorsList.add(i,error);
    }
}
