package bg.sofia.uni.fmi.mjt.splitwise.server.util;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Validator {
    private static final String ALLOWED_SYMBOLS_REGEX = "[a-zA-Z0-9\\-\\.\\_]+";

    public static boolean containsOnlyValidSymbols(String username) {
        return Pattern.compile(ALLOWED_SYMBOLS_REGEX).matcher(username).matches();
    }
}
