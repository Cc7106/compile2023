import java.io.IOException;
import Error.*;
import IR.Visitor;

public class Compiler {

    public static void main(String[] args) throws IOException {
        IOFiles ioFiles = new IOFiles();
        Lexer lexer = new Lexer(ioFiles);
        //lexer.printResult();
        Parser parser = new Parser(lexer.getTokenList());
        //ioFiles.writeToFile2(parser.getCompUnit());
        ioFiles.writeToFile3(parser.getErrorsList());
        if (parser.getErrorsList().isEmpty()) {
            Visitor visitor = new Visitor();
            visitor.visitCompUnit(parser.getCompUnit());
            ioFiles.writeToFile4(visitor.getIrModule());
        }


    }
}
