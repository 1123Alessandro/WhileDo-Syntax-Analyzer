import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    // DONE: parse the main structure of the while loop
    // DONE: detect all while loops within the same level
    public static String[] findWhile(RegExer re, String text) {
        String pattern = "while[\\s]*[(][^()]+[)][\\s]*[{]([^{}]+)*[}]";
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
            "[\\s]*!*[a-zA-Z_$]+[$\\w]*[\\s]*", // single bool var
            "!*[a-zA-Z_$]+[$\\w]*[\\s]*(==|!=|&&|[|]{2})[\\s]*!*[a-zA-Z_$]+[$\\w]*", // bool var to bool var
            "-*([a-zA-Z_$]+[$\\w]*|[0-9]+)[\\s]*(==|!=|<=|>=|<|>)[\\s]*-*([a-zA-Z_$]+[$\\w]*|[0-9]+)", // int var/constant to int var/constant
        };
        String pattern = "[(](";
        for (int i = 0; i < pats.length; i++){
            pattern += (i == pats.length-1) ? pats[i] : (pats[i] + "|");
        }
        pattern += ")[)]";

        return m.matches(pattern);
    }

    // TODO: parse the content area of the while loop to include nested whiles
    public static boolean findContent(RegExer re, String text) {
        // get the content of the while loop
        String whilePattern = "while[\\s]*[(][^()]+[)][\\s]*(?<content>[{]([^{}]+)*[}])";
        // String whilePattern = "while[\\s]+[(][^()]+[)][\\s]+[{](?<content>[^{}]+)*[}]";
        Pattern p = Pattern.compile(whilePattern, Pattern.DOTALL);
        Matcher m = p.matcher(text);
        if (!m.find()) return false;
        text = m.group("content");
        // System.out.println("CONTENT ==================== \n" + text);

        String[] pats = {
            "[\\s]*;[\\s]*", // just a semicolon
            // "[\\s]*([a-zA-Z]+.)*[a-zA-Z]+[(][\"'\\w]*[)][\\s]*;[\\s]*", // calling functions
            "[\\s]*[a-zA-Z]+[\\s]*[a-zA-Z_$]+[\\w]*[\\s]*(=|[+]=|-=|[*]=|/=)[\\s]*([a-zA-Z_$]+[\\w]*|[0-9]+)[\\s]*;[\\s]*", // variable declaration with assignment
            "[\\s]*[a-zA-Z_$]+[\\s]*(=|[+]=|-=|[*]=|/=)[\\s]*([a-zA-Z]+|[0-9]+)[\\s]*;[\\s]*", // variable assignment
            "[\\s]*[a-zA-Z]+[\\s]*[a-zA-Z_$]+[$\\w]*[\\s]*;[\\s]*", // variable declaration
            // "[\\s]*(int|float|char|double|long)?[\\s]+[a-zA-Z_$]+[\\s]*(=|-=|[*]=|/=)[\\s]*([a-zA-Z_$]+|[0-9]+);[\\s]*",
            // "[\\s]*",
            // ".*",
        };
        String pattern = "[{](";
        for (int i = 0; i < pats.length; i++) {
            pattern += (i == pats.length-1) ? pats[i] : (pats[i] + "|");
        }
        pattern += ")*[\\s]*[}]";
        // System.out.println("Pattern ==================== " + pattern);
        // System.out.println(text);
        // String[] matches = re.findPattern(pattern, text);
        // String[] uu = re.findPattern(pattern, text);
        // for (String s: uu) {
        //     System.out.println("---");
        //     System.out.println(s);
        // }
        return text.matches(pattern);
    }

    public static boolean checkWhile(RegExer re, String text) {
        System.out.println("Running checkWhile()...");
        String[][] errors = {
            {"\\([^()]*\\)[\\s]*[{][^{}]*[}]", "No while keyword found"},
            {"while[\\s]+[^()]+\\)[\\s]*[{][^{}]*[}]", "Unbalanced Parenthesis"},
            {"while[\\s]*\\([^()]+[\\s]*[{][^{}]*[}]", "Unbalanced Parenthesis"},
            {"while[\\s]+[^()]*[\\s]*[{][^{}]*[}]", "Invalid argument"},
            {"while[\\s]*\\([^()]+\\)[\\s]*[\\S]+[\\s]*[{][^{}]*[}]", "Statement/s outside of while block"},
            {"while[\\s]+[\\S]*[\\s]*[{][^{}]*[}]", "No acceptable while condition found"},
        };

        boolean found = false;
        for (String[] error: errors) {
            String[] matches = re.findPattern(error[0], text);
            if (matches.length > 0) {
                found = true;
                // System.out.println("Found :: " + matches.length);
                for (String s: matches) {
                    System.out.println(re.replace("[\\s&&[^ ]]+", "", s));
                    System.out.println("Error: " + error[1]);
                    System.out.println();
                }
            }
        }
        return found;
    }

    public static boolean checkLogic(RegExer re, String text) {
        String whilePattern = "while\\s*\\((?<logic>[^()]*)\\)\\s*\\{";
        String m = re.extractWhileCondition(whilePattern, text);
        // System.out.println(m);

        String[][] errors = {
            {"[\\s\\W]+!+[0-9]+[\\s\\W]+", "Incompatible types"},
            {"[\\s]+-*[a-zA-Z_$]+[\\s]+", "Not a logical statement"},
            {"[\\s]*(!*|-*)[0-9]+[$\\w]+\\s*", "Improper variable name"},
            {"!+\\s*[\\w]+\\s*(<=|>=|<|>)\\s*!*\\s*[\\w]+", "Incorrect operators"},
            {"!*\\s*[\\w]+\\s*(<=|>=|<|>)\\s*!+\\s*[\\w]+", "Incorrect operators"},
            {"-+\\s*[\\w]+\\s*(&&|[|]{2})\\s*-*\\s*[\\w]+", "Incorrect operators"},
        };

        boolean found = false;
        for (String[] error: errors) {
            String[] matches = re.findPattern(error[0], m);
            if (matches.length > 0) {
                found = true;
                for (String s: matches) {
                    System.out.println(s);
                    System.out.println("Error: " + error[1]);
                    System.out.println();
                }
            }
        }

        return found;
    }

    public static String[] checkContent(RegExer re, String text) {
        String whilePattern = "while[\\s]+[(][^()]+[)][\\s]+[{](?<content>[^{}]+)*[}]";
        Pattern p = Pattern.compile(whilePattern, Pattern.DOTALL);
        Matcher m = p.matcher(text);
        if (!m.find()) return null;
        text = m.group("content");

        // System.out.println(text);
        // System.out.println();
        String[][] errors = {
            // Variable declaration with assignment
            {"[\\s]*[0-9]+[a-zA-Z]*[\\s]*[0-9]+[a-zA-Z_$]*[\\w]*[\\s]*[\\W]+[\\s]*([0-9]+[a-zA-Z_$]+)[\\s]*;[\\s]*", "Not an assignment statement"},
        };

        for (int i = 0; i < errors.length; i++) {
            String[] matches = re.findPattern(errors[i][0], text);
            System.out.println("Length :: " + matches.length);
            if (matches.length > 0)
                for (String s: matches) {
                    System.out.println(re.replace("[\\s&&[^ ]]+", "", s));
                    System.out.println("Error: " + errors[i][1]);
                    System.out.println();
                }
        }
        return null;
    }

    public static String validityChecker(boolean flag) {
        if(flag == true) {
            return "Valid Syntax";
        }
        else {
            return "Invalid Syntax";
        }
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
            // System.out.println(s);
            boolean content = findContent(re, s);
            boolean logic = findLogic(re, s);
            // if (!content) checkContent(re, s);
            if (!logic) checkLogic(re, s);
            System.out.println("============================== content: " + content);
            System.out.println("============================== logic: " + logic);
        }

        if (matches.length < 1)
            checkWhile(re, text);


    }
}
