package vedam.subkuch;


import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.println(isStringName("F A ."));
    }

    public static boolean isStringName(String str) {
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[a-zA-Z.\\s]*$")));
    }
}