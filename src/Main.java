import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        CodeReader obj = new CodeReader("test.txt");
        String text = obj.output();
        System.out.println("--------------------------------------------------");
        System.out.println(text);
        System.out.println("--------------------------------------------------");

        Pattern p = Pattern.compile("true");
        Matcher m = p.matcher(text);
        m.find();
        // System.out.println(m.group());
        System.out.println();
    }
}
