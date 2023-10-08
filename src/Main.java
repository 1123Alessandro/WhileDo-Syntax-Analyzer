import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        CodeReader obj = new CodeReader("test.txt");
        String text = obj.output();
        System.out.println("Original Text:------------------------------------");
        System.out.println(text);
        System.out.println("--------------------------------------------------");

        // Pattern p = Pattern.compile("\n");
        // Matcher m = p.matcher(text);
        // m.find();
        // System.out.println(m.group());
        // System.out.println(m.replaceAll(""));


        RegExer re = new RegExer();
        // System.out.println(rr.findPattern("t.+e", text).group());
        String[] matches = re.findPattern("(t.+e)|(h.*d)", text);
        for (String s : matches) {
            System.out.println(s);
        }

        String hhh = re.replace("(t.+e)|(h.*d)", "000", text);
        System.out.println(hhh);
        // rr.findPattern("true", text);
    }
}
