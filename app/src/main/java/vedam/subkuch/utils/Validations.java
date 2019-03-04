package vedam.subkuch.utils;

public class Validations {
    public static boolean isFieldEmpty(String text) {
        if (text.trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
