package i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault());

    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        System.out.println("Locale changed to: " + locale);
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public static String get(String key) {
        return bundle.getString(key);
    }
}
