import GrammarTypes.CompUnit;
import Token.Token;
import Error.Error;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;


public class IOFiles {
    private String inputCode;
    private FileWriter writer;

    public IOFiles() throws IOException {
        this.inputCode = read();
        //this.writer = new FileWriter(new File("output.txt"));
        this.writer = new FileWriter(new File("error.txt"));
    }

    public String read() throws IOException {
        Path path = Paths.get("testfile.txt");
        Scanner in = new Scanner(path);
        StringJoiner inputText = new StringJoiner("\n");
        while (in.hasNextLine()) {
            inputText.add(in.nextLine());
        }
        in.close();
        return inputText.toString();
    }

//    public void writeToFile(ArrayList<Token> tokenArrayList) throws IOException {
//        for (Token token: tokenArrayList) {
//            writer.write(token.toString() + "\n");
//        }
//        writer.flush();
//        writer.close();
//    }

    public void writeToFile2(CompUnit compUnit) throws IOException {
        writer.write(compUnit.toString());
        writer.flush();
        writer.close();
    }
    public void writeToFile3(ArrayList<Error> errors) throws IOException {
        for (Error error: errors) {
            writer.write(error.toString());
        }
        writer.flush();
        writer.close();;
    }

    public String getInputCode() {
        return inputCode;
    }

}
