package com.ootd.be.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public enum FORMATTER {
        YMD("yyyyMMdd"),
        ;

        final String format;
        final DateTimeFormatter formatter;

        FORMATTER(String format) {
            this.format = format;
            this.formatter = DateTimeFormatter.ofPattern(format);
        }

        public String to(LocalDateTime dateTime) {
            return dateTime.format(this.formatter);
        }

        public LocalDateTime from(String dateTime) {
            return LocalDateTime.from(formatter.parse(dateTime));
        }
    }

}
