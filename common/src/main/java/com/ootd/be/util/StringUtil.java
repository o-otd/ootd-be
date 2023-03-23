package com.ootd.be.util;

import org.slf4j.helpers.MessageFormatter;

public class StringUtil {

    public static String format(String format, Object...args) {
        if (args == null || args.length < 1) {return format;}
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }


}
