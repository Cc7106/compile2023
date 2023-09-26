import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer(new IOFiles());
        lexer.printResult();
    }
}
