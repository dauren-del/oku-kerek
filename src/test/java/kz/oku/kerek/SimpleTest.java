package kz.oku.kerek;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTest {

    @Test
    public void test() {
        String SCRIPT_REGEX = "^pages.push\\('(/\\w+){7}\\.png\\?time=\\d+&key=\\w+'\\);$";
        Pattern SCRIPT_PATTERN =
                Pattern.compile(SCRIPT_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern PAGE_PATH_PATTERN = Pattern.compile("(/\\w+){7}\\.png\\?time=\\d+&key=\\w+");
        Pattern PAGE_NAME_PATTERN = Pattern.compile("\\w+\\.png");

        String pagePath = "pages.push('/FileStore/dataFiles/49/92/1518279/content/0438.png?time=1588623066352&key=b73e31225ebde3981f8de99248cda1e9');";
        Matcher matcher = PAGE_PATH_PATTERN.matcher(pagePath);
        Matcher pageNameMatcher = PAGE_NAME_PATTERN.matcher(pagePath);

        System.out.println(SCRIPT_PATTERN.matcher(pagePath).matches());
        System.out.println(matcher.find() ? matcher.group() : "not found");
        System.out.println(pageNameMatcher.find() ? pageNameMatcher.group() : "not found");

    }

}
