package com.ootd.be.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static class YMD {

        public static final String format = "yyyyMMdd";
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        public static String format(LocalDateTime dateTime) {
            return dateTime.format(formatter);
        }

        public static LocalDate from(String dateTime) {
            return LocalDate.from(formatter.parse(dateTime));
        }

        public static LocalDateTime from(String dateTime, boolean endOfDay) {
            LocalDateTime localDate = from(dateTime).atStartOfDay();
            return endOfDay ? localDate.plusDays(1).minusNanos(1) : localDate;
        }
    }

}
