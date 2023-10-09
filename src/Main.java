import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    // DONE: parse the main structure of the while loop
    public static String[] findWhile(RegExer re, String text) {
        String pattern = "while [(].+[)] [{].*[}]";
        String[] matches = re.findPattern(pattern, text);
        return matches;
    }

    // TODO: parse the logic area of the while loop
    public static boolean findLogic(RegExer re, String text) {
        String pattern = "while\\s*(\\([^)]*\\))\\s*\\{";
        String m = re.extractWhileCondition(pattern, text);
        boolean flag = false;

        m = m.replaceAll("\\s+", "");
        System.out.println(m);

        String firstP = m.substring(0, 1);
        String secondP = m.substring(m.length() - 1);
        //System.out.println(firstP);
        //System.out.println(secondP);
        if(firstP.equals("(") && secondP.equals(")")) {
            flag = true;
        }

        // Check if each part contains a variable name followed by a comparison operator
        if(m.matches("[a-zA-Z_][a-zA-Z0-9_]*[<>!=]=?[<>]?[0-9]+")) {
            flag = true;
        }
        else if(m.matches("[a-zA-Z_][a-zA-Z0-9_]*[<>!=]=?[<>]?[a-zA-Z_][a-zA-Z0-9_]*")) {
            flag = true;
        }

        return flag;

        //return matches;
    }

    public static String validityChecker(boolean flag) {
        if(flag == true) {
            return "Valid Syntax";
        }
        else {
            return "Invalid Syntax";
        }
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
        /*String[] matches = findWhile(re, text);
        for (String s : matches) {
            System.out.println(s);
        }*/
        /*String[] matches = findLogic(re, text);
        for (String s : matches) {
            System.out.println(s);
        }*/
        
        String conclu = validityChecker(findLogic(re, text));
        System.out.println("Valid or not: " + conclu);

    }
}
