import java.io.IOException;
import Error.*;

public class Compiler {

    public static void main(String[] args) throws IOException {
        IOFiles ioFiles = new IOFiles();
        Lexer lexer = new Lexer(ioFiles);
        //lexer.printResult();
        Parser parser = new Parser(lexer.getTokenList());
        ioFiles.writeToFile3(parser.getErrorsList());

    }
}
