package toolset;

import java.io.*;

public class CodeReader {

    private String fileName;
    private BufferedReader br;

    public CodeReader(String file) throws IOException {
        this.fileName = file;
        FileReader fr = new FileReader(file);
        this.br = new BufferedReader(fr);
    }

    public String output() throws IOException {
        String s = "";
        for (String in = this.br.readLine() ; in != null ; in = this.br.readLine()) {
            s += in + "\n";
        }
        return s;
    }
}
