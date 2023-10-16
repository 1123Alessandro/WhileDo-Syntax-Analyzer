import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    // DONE: parse the main structure of the while loop
    // DONE: detect all while loops within the same level
    public static String[] findWhile(RegExer re, String text) {
        // String pattern = "(while [(].+[)] [{].+[}](?=.+[{]))|((?<=[}].+)while [(].+[)] [{].+[}])";
        // String pattern = "while[\\s]+[(].+[)][\\s]+[{].+[}]";
        String[] pats = {
            // "[\\s]*[a-zA-Z0-9_$=+-/*\\s;]+[\\s]*",
            "[^{}]+",
        };
        // String pattern = "while[\\s]+[(].+[)][\\s]+[{](";
        String pattern = "while[\\s]+[(][^()]+[)][\\s]+[{](";
        for (int i = 0; i < pats.length; i++) {
            pattern += (i == pats.length-1) ? pats[i] : (pats[i] + "|");
        }
        pattern += ")*[}]";
        String[] matches = re.findPattern(pattern, text);
        return matches;
    }

    // DONE: parse the logic area of the while loop
    // DONE: bugfix regex
    public static boolean findLogic(RegExer re, String text) {
        String whilePattern = "while\\s*(\\([^)]*\\))\\s*\\{";
        String m = re.extractWhileCondition(whilePattern, text);
        // System.out.println("M ========== " + m);
        boolean flag = false;

        // m = m.replaceAll("\\s+", "");
        // System.out.println(m);

        String[] pats = {
            "[\\s]*!*[a-zA-Z_$]+[\\s]*", // single bool var
            "!*[a-zA-Z_$]+[\\s]*(==|!=|&&|[|]{2})[\\s]*!*[a-zA-Z_$]+", // bool var to bool var
            "-*([a-zA-Z_$]+|[0-9]+)[\\s]*(==|!=|<=|>=|<|>)[\\s]*-*([a-zA-Z_$]+|[0-9]+)", // int var/constant to int var/constant
        };
        String pattern = "[(](";
        for (int i = 0; i < pats.length; i++){
            pattern += (i == pats.length-1) ? pats[i] : (pats[i] + "|");
        }
        pattern += ")[)]";

        return m.matches(pattern);
    }

    public static String validityChecker(boolean flag) {
        if(flag == true) {
            return "Valid Syntax";
        }
        else {
            return "Invalid Syntax";
        }
    }

    // DEPRECATED: parse the content area of the while loop to include nested whiles
    public static String[] findContent(RegExer re, String text) {
        String[] pats = {
            "[\\s]*(int|float|char|double|long)?[\\s]+[a-zA-Z_$]+[\\s]*(=|-=|[*]=|/=)[\\s]*([a-zA-Z_$]+|[0-9]+);[\\s]*",
            "[\\s]*"
            // ".*",
        };
        String pattern = "while[\\s]+[(].+[)][\\s]+[{](";
        for (int i = 0; i < pats.length; i++) {
            pattern += (i == pats.length-1) ? pats[i] : (pats[i] + "|");
        }
        pattern += ")[}]";
        String[] matches = re.findPattern(pattern, text);
        return matches;
    }

    public static void main(String[] args) throws IOException {
        CodeReader obj = new CodeReader("test.txt");
        String text = obj.output();
        System.out.println("Original code:------------------------------------");
        System.out.print(text);
        System.out.println("--------------------------------------------------");

        // TODO: pattern of the whole program
        RegExer re = new RegExer();

        // DEPRECATED: get all while loops
        // DEPRECATED: detect nested while loops
        String[] matches = findWhile(re, text);
        for (String s : matches) {
            System.out.println(s);
            System.out.println("==================== Conclusion: " + validityChecker(findLogic(re, s)) + " ====================");
        }

    }
}
