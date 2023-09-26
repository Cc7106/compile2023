import java.util.HashMap;

public class Token {
    private TokenType tokenType;
    private String token;

    private int num;

    private int lineNum;

    private HashMap<String, TokenType> reservedWordsMap = new HashMap<String, TokenType>() {{
        put("main", TokenType.MAINTK);
        put("const", TokenType.CONSTTK);
        put("int", TokenType.INTTK);
        put("break", TokenType.BREAKTK);
        put("continue", TokenType.CONTINUETK);
        put("if", TokenType.IFTK);
        put("else", TokenType.ELSETK);
        put("for", TokenType.FORTK);
        put("getint", TokenType.GETINTTK);
        put("printf", TokenType.PRINTFTK);
        put("return", TokenType.RETURNTK);
        put("void", TokenType.VOIDTK);
        }
    };

    private HashMap<String, TokenType> allSymbolsMap = new HashMap<String, TokenType>() {
        {
            put("&&", TokenType.AND);
            put("||", TokenType.OR);

            put("+", TokenType.PLUS);
            put("-", TokenType.MINU);
            put("*", TokenType.MULT);
            put("/", TokenType.DIV);
            put("%", TokenType.MOD);

            put("<", TokenType.LSS);
            put("<=", TokenType.LEQ);
            put(">", TokenType.GRE);
            put(">=", TokenType.GEQ);
            put("=", TokenType.ASSIGN);
            put("==", TokenType.EQL);
            put("!", TokenType.NOT);
            put("!=", TokenType.NEQ);

            put("(", TokenType.LPARENT);
            put(")", TokenType.RPARENT);
            put("[", TokenType.LBRACK);
            put("]", TokenType.RBRACK);
            put("{", TokenType.LBRACE);
            put("}", TokenType.RBRACE);

            put(";", TokenType.SEMICN);
            put(",", TokenType.COMMA);
        }
    };

    //符号构造器
    public Token(String symbol, int lineNum) {
        this.tokenType = allSymbolsMap.get(symbol);
        this.token = symbol;
        this.lineNum = lineNum;
    }

    //变量 保留字 数字 构造器
    public Token(int type, String string, int lineNum) {
        if (type == 0) {
            //变量 / 保留字
            this.tokenType = reservedWordsMap.getOrDefault(string, TokenType.IDENFR);
            this.token = string;
            this.lineNum = lineNum;
        } else if (type == 1) {
            //数字
            this.tokenType = TokenType.INTCON;
            this.token = string;
            this.num = Integer.parseInt(string);
            this.lineNum = lineNum;
        } else if (type == 2) {
            this.tokenType = TokenType.STRCON;
            this.token = string;
            this.lineNum = lineNum;
        }
    }

    public Token(TokenType tokenType, String token, int lineNum) {
        this.tokenType = tokenType;
        this.token = token;
        this.lineNum = lineNum;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getToken() {
        return token;
    }

    public int getNum() {
        return num;
    }

    public int getLineNum() {
        return lineNum;
    }

    @Override
    public String toString() {
        return tokenType.toString() + " " + token;
    }
}
