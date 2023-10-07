package toolset;

import java.io.*;

public class CodeReader {

    private String fileName;
    private FileReader fr;

    public CodeReader(String file) throws IOException {
        this.fileName = file;
        this.fr = new FileReader(file);
    }

    public String output() throws IOException {
        String s = "";
        for (int i = this.fr.read(); i != -1; i = this.fr.read()) {
            s += (char) i;
        }
        return s;
    }
}
