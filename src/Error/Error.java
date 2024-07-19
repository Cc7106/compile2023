package Error;

public class Error {
    int line;
    ErrorType errorType;

    public Error(int line, ErrorType errorType) {
        this.line = line;
        this.errorType = errorType;
    }

    public int getLine() {
        return line;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String toString() {
        return line + " " + errorType + "\n";
    }
}
