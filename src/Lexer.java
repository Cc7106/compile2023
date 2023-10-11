import Token.Token;

import java.io.IOException;
import java.util.ArrayList;

public class Lexer {
    private final IOFiles ioFiles;
    private final String source;
    private int curPos;
    private int lineNum;
    private char curChar;
    private String curToken;
    private ArrayList<Token> tokenList;

    public Lexer(IOFiles ioFiles) {
        this.ioFiles = ioFiles;
        this.source = ioFiles.getInputCode();
        this.lineNum = 1;
        this.curPos = 0;
        this.tokenList = new ArrayList<>();
        next();
    }

    private void getChar() {
        curChar = source.charAt(curPos);
        curPos++;
    }

    private void unGetChar() {
        curPos--;
    }

    public void next() {
        lineNum = 1;
        while (curPos < source.length()) {
            curToken = "";

            getChar();
            if (curChar == '\n') {
                lineNum++;
            } else if (curChar == ' ' || curChar == '\t') {
                continue;
            } else if (Character.isLetter(curChar) || curChar == '_') {
                getString();
            } else if (Character.isDigit(curChar)) {
                getNum();
            } else if (curChar == '&' || curChar == '|') {
                getLogicType();
            } else if ( curChar == '<' || curChar == '>' || curChar == '!' || curChar == '=') {
                getRelationType();
            } else if (curChar == '/') {
                getSlashType();
            } else if (curChar == '"') {
                getFormatString();
            } else if (curChar == '+' || curChar == '-' || curChar == '%' || curChar == '*') {
                curToken = Character.toString(curChar);
                tokenList.add(new Token(curToken, lineNum));
            } else if (curChar == ';' || curChar == ',' ||
                    curChar == '(' || curChar == ')' ||
                    curChar == '[' || curChar == ']' ||
                    curChar == '{' || curChar == '}') {
                curToken = Character.toString(curChar);
                tokenList.add(new Token(curToken,lineNum));
            } else {
                error();
            }
        }
    }

    public void getString() {
        // 字母 ｜ 下划线 + （数字 ｜ 字母 ｜ 下划线）
        // 变量名 or 保留字
        curToken = Character.toString(curChar);
        while (curPos < source.length()) {
            getChar();
            if (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '_') {
                curToken += curChar;
            } else {
                unGetChar();
                break;
            }
        }
        tokenList.add(new Token(0,curToken,lineNum));
    }

    public void getNum() {
        curToken = Character.toString(curChar);
        while (curPos < source.length()) {
            getChar();
            if (Character.isDigit(curChar)) {
                curToken += curChar;
            } else {
                unGetChar();
                break;
            }
        }
        tokenList.add(new Token(1,curToken,lineNum));
    }

    public void getLogicType() {
        // &&  or ||
        curToken = Character.toString(curChar);
        getChar();
        if (curChar == curToken.charAt(0)) {
            curToken += curChar;
            tokenList.add(new Token(curToken, lineNum));
        } else {
            //error
            unGetChar();
        }
    }

    public void getRelationType() {
        // < > ! =
        // <= >= != ==
        curToken = Character.toString(curChar);
        getChar();

        if (curChar == '=') {
            curToken += curChar;
        } else {
            unGetChar();
        }
        tokenList.add(new Token(curToken, lineNum));
    }

    public void getSlashType() {
        getChar();
        if (curChar != '*' && curChar != '/') {
            unGetChar();
            tokenList.add(new Token("/",lineNum));
            return;
        }

        if (curChar == '/') {
            //进入 '//' 注释
            while (curPos < source.length()) {
                getChar();
                if (curChar == '\n') {
                    lineNum++;
                    break;
                }
            }
        } else if (curChar == '*') {
            // 进入 '/*  */' 注释
            while (curPos < source.length()) {
                getChar();
                if (curChar == '\n') {
                    lineNum++;
                } else if (curChar == '*') {
                    while (curPos < source.length() && curChar == '*') {
                        getChar();
                    }
                    if (curChar == '/') {
                        break;
                    } else {
                        unGetChar();
                    }
                }
            }
        }
    }


    public void getFormatString() {
        curToken = Character.toString(curChar);
        int lineNumStart = lineNum;
        while (curPos < source.length()) {
            getChar();
            curToken += curChar;
            if (curChar == '\n') {
                lineNum++;
            }
            if (curChar == '"') {
                break;
            }
        }
        tokenList.add(new Token(2, curToken, lineNumStart));
    }

    public void error() { }

//    public void printResult() throws IOException {
//        ioFiles.writeToFile(tokenList);
//    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }
}
