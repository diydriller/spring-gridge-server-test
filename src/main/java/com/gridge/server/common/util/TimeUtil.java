package com.gridge.server.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {
    public static LocalDate stringToLocalDate(String string) {
        return LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
    }
    public static String currentDateTimeString() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
}
