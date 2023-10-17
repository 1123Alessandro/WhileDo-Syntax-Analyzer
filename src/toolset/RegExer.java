package toolset;

import java.util.regex.*;

public class RegExer {

    // Finds a substring in a given string using a regex pattern
    public String[] findPattern(String pattern, String text) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(text);

        int matches = 0;
        while (m.find()) {
            matches++;
        }

        String[] arr = new String[matches];
        m.reset();
        for (int i = 0; i < matches ; i++) {
            m.find();
            arr[i] = m.group();
        }

        return arr;
    }

    // Replaces a certain substring in a given string identified by a regex pattern
    public String replace(String pattern, String substitute, String text) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        return m.replaceAll(substitute);
    }

    // From a given while code snippet, it extracts and returns the while condition
    public String extractWhileCondition(String pattern, String whileLoop) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(whileLoop);

        if (m.find()) {
            return m.group(1).trim(); // Include the outer parentheses
        }

        return null;
    }
}
