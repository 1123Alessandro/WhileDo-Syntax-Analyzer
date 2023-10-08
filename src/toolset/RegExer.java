package toolset;

import java.util.regex.*;

public class RegExer {

    // TODO: find the matching pattern in the string
    public String[] findPattern(String pattern, String text) {
        // System.out.println("==================================================");
        // System.out.println("Running findPattern()...");
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        // System.out.println("Pattern ::\t" + p.pattern());
        Matcher m = p.matcher(text);

        // TODO: count the number of matches
        int matches = 0;
        while (m.find()) {
            matches++;
            // System.out.println("Match #" + matches + " ::\t" + m.group());
        }

        String[] arr = new String[matches];
        m.reset();
        for (int i = 0; i < matches ; i++) {
            m.find();
            arr[i] = m.group();
        }

        return arr;
    }

    // TODO: replace every recognized pattern in a text with an input
    public String replace(String pattern, String substitute, String text) {
        // System.out.println("==================================================");
        // System.out.println("Running replace()...");
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        return m.replaceAll(substitute);
    }
}
