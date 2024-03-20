package com.gridge.server.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {
    public static LocalDate stringToLocalDate(String string) {
        return LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
    }
    public static String currentDateTimeString() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
    public static LocalDateTime startOfDate(LocalDate localDate){
        return localDate.atTime(LocalTime.MIN);
    }
    public static LocalDateTime endOfDate(LocalDate localDate){
        return localDate.atTime(LocalTime.MAX);
    }
}
