package one.hyro.lib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    public static String colorize(String message) {
        return message.replace("&", "§");
    }

    public static String formatIsoDate(Date date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return isoFormat.format(date);
    }
}
