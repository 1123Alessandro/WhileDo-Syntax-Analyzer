import toolset.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    // Checks for the main components of a while loop: the while keyword, the condition, and the commands block
    public static String[] findWhile(RegExer re, String text) {
        String pattern = "while[\\s]*[(][^()]+[)][\\s]*[{]([^{}]+)*[}]";
        String[] matches = re.findPattern(pattern, text);
        return matches;
    }

    // Checks for the basic logic structure of the while condition.
    // Includes relational and logical operators
    public static boolean findLogic(RegExer re, String text) {
        String whilePattern = "while\\s*(\\([^)]*\\))\\s*\\{";
        String m = re.extractWhileCondition(whilePattern, text);
        boolean flag = false;

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

    // Checks the inside of the while loop for any recognizable basic assignment statements
    public static boolean findContent(RegExer re, String text) {
        // get the content of the while loop
        String whilePattern = "while[\\s]*[(][^()]+[)][\\s]*(?<content>[{]([^{}]+)*[}])";
        Pattern p = Pattern.compile(whilePattern, Pattern.DOTALL);
        Matcher m = p.matcher(text);
        if (!m.find()) return false;
        text = m.group("content");

        String[] pats = {
            "[\\s]*;[\\s]*", // just a semicolon
            "[\\s]*[a-zA-Z]+[\\s]*[a-zA-Z_$]+[\\w]*[\\s]*(=|[+]=|-=|[*]=|/=)[\\s]*([a-zA-Z_$]+[\\w]*|[0-9]+)[\\s]*;[\\s]*", // variable declaration with assignment
            "[\\s]*[a-zA-Z_$]+[\\s]*(=|[+]=|-=|[*]=|/=)[\\s]*([a-zA-Z]+|[0-9]+)[\\s]*;[\\s]*", // variable assignment
            "[\\s]*[a-zA-Z]+[\\s]*[a-zA-Z_$]+[$\\w]*[\\s]*;[\\s]*", // variable declaration
            "[\\s]*[a-zA-Z_$]+[\\w]*\\s*([+]{2}|[-]{2})\\s*;\\s*", // variable ++ or --
        };
        String pattern = "[{](";
        for (int i = 0; i < pats.length; i++) {
            pattern += (i == pats.length-1) ? pats[i] : (pats[i] + "|");
        }
        pattern += ")*[\\s]*[}]";
        return text.matches(pattern);
    }

    // if findWhile() fails to match a while loop, this function checks for any recognizable error
    // if the error is not recognized, it will just return false without any error message
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
                for (String s: matches) {
                    System.out.println(re.replace("[\\s&&[^ ]]+", "", s));
                    System.out.println("Error: " + error[1]);
                    System.out.println();
                }
            }
        }
        return found;
    }

    // if findLogic() fails, this function looks for any recognizable errors in the while condition
    public static boolean checkLogic(RegExer re, String text) {
        String whilePattern = "while\\s*\\((?<logic>[^()]*)\\)\\s*\\{";
        String m = re.extractWhileCondition(whilePattern, text);

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

    // returns a string for when a check is valid and when it isn't
    public static String validityChecker(boolean flag) {
        if(flag == true) {
            return "Valid Syntax";
        }
        else {
            return "Invalid Syntax";
        }
    }

    // the entire structure of the program
    // outputs the txt file that has been read
    // then outputs any possible errors it can find
    // as well as the conclusion for the overall code snippet
    public static void main(String[] args) throws IOException {
        CodeReader obj = new CodeReader("test.txt");
        String text = obj.output();
        System.out.println("Original code:------------------------------------");
        System.out.print(text);
        System.out.println("--------------------------------------------------");

        RegExer re = new RegExer();

        String[] matches = findWhile(re, text);
        for (String s : matches) {
            boolean content = findContent(re, s);
            boolean logic = findLogic(re, s);
            if (!logic) checkLogic(re, s);
            if (!content) {
                System.out.println("Error: Syntax of the content is incorrect or outside of our scope");
                System.out.println();
            }
            System.out.println("\n==========\nConclusion: " + validityChecker(content && logic) + "\n==========\n");
        }

        boolean found = true;
        if (matches.length < 1)
            found = checkWhile(re, text);

        if (!found)
            System.out.println("Error: No valid while block found");


    }
}
