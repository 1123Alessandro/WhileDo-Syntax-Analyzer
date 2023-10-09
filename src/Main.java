import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    // DONE: parse the main structure of the while loop
    // DONE: detect all while loops within the same level
    public static String[] findWhile(RegExer re, String text) {
        // String pattern = "(while [(].+[)] [{].+[}](?=.+[{]))|((?<=[}].+)while [(].+[)] [{].+[}])";
        String pattern = "while[\s\n]+[(].+[)][\s\n]+[{].+[}]";
        String[] matches = re.findPattern(pattern, text);
        return matches;
    }

    // TODO: parse the logic area of the while loop
    public static String[] findLogic(RegExer re, String text) {
        return null;
    }

    // TODO: parse the content area of the while loop to include nested whiles
    public static String[] findContent(RegExer re, String text) {
        return null;
    }

    public static void main(String[] args) throws IOException {
        CodeReader obj = new CodeReader("test.txt");
        String text = obj.output();
        System.out.println("Original code:------------------------------------");
        System.out.println(text);
        System.out.println("--------------------------------------------------");

        // TODO: pattern of the whole program
        RegExer re = new RegExer();

        // TODO: get all while loops
        // TODO: detect nested while loops
        String[] matches = findWhile(re, text);
        for (String s : matches) {
            System.out.println(s);
            System.out.println("===");
        }

    }
}
