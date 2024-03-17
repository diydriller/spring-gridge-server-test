package com.gridge.server.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static LocalDate stringToLocalDate(String string) {
        return LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
    }
}
