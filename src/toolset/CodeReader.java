package toolset;

import java.io.*;

// A file reader for text files
public class CodeReader {

    private String fileName;
    private BufferedReader br;

    // Constructor builds an object that reads a file with the inputted file name
    public CodeReader(String file) throws IOException {
        this.fileName = file;
        FileReader fr = new FileReader(file);
        this.br = new BufferedReader(fr);
    }

    // returns a string of the entire file
    public String output() throws IOException {
        String s = "";
        for (String in = this.br.readLine() ; in != null ; in = this.br.readLine()) {
            s += in + "\n";
        }
        return s;
    }
}
