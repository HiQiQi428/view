package org.luncert.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static Date parse(String dateStr) throws ParseException {
        return sdf.parse(dateStr);
    }

}