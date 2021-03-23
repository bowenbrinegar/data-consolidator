package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    public boolean isValidDate(String input) throws ParseException {
        return input.equals(df.format(df.parse(input)));
    }

    public static boolean gte(String inputDate, String pathDate) throws ParseException {
        Date d1 = df.parse(inputDate);
        Date d2 = df.parse(pathDate);
        return d1.compareTo(d2) <= 0;
    }
}
