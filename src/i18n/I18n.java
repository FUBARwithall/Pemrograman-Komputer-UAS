package i18n;

import java.util.*;

public class I18n {
    private ResourceBundle bundle;

    public I18n(String lang) {
        Locale locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public String get(String key) {
        return bundle.getString(key);
    }
}
