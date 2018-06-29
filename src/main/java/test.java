import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Poojan on 6/29/2018.
 */
public class test {
    private static final Pattern datetimePattern = Pattern.compile("(?<YEAR>\\d{4})-(?<MONTH>\\d{2})-(?<DAY>\\d{2}) (?<HOUR>\\d{2}):(?<MIN>\\d{2})");

    public static void main(String[] args) {

        String d = "2014-33-44 44:66";
        Matcher matcher = datetimePattern.matcher(d);
        boolean matches = matcher.matches();
        System.out.println("matches = " + matches);
        System.out.println("matcher = " + matcher.group("YEAR"));
        System.out.println("matcher = " + matcher.group("MONTH"));
        System.out.println("matcher = " + matcher.group("DAY"));
        System.out.println("matcher = " + matcher.group("HOUR"));
        System.out.println("matcher = " + matcher.group("MIN"));


    }
}
