package com.ootd.be.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    public static class YMDHM {

        public static final String format = "yyyy.MM.dd. HH:mm";
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        public static String format(LocalDateTime dateTime) {
            return dateTime.format(formatter);
        }

    }

    public static long dayDiff(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
    }

    public static void main(String[] args) {
        long test1 = dayDiff(LocalDateTime.now(), LocalDateTime.now().plusDays(3).minusHours(1));
        System.out.println("test1 = " + test1);
        long test2 = dayDiff(LocalDateTime.now(), LocalDateTime.now().plusDays(3).plusHours(1));
        System.out.println("test2 = " + test2);
        long test3 = dayDiff(LocalDateTime.now(), LocalDateTime.now().plusDays(3));
        System.out.println("test3 = " + test3);
    }



}
