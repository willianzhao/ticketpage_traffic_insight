package org.willian.data.pvinsight.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by weilzhao on 3/15/15.
 */
public class ProjectUtils {

    public static final String CONSTANT_PARAM_DATE_PATTERN = "YYYYMMDD";
    private static final Logger logger = LogManager.getLogger();

    public static String cleanupStr(String str) {
        if (str != null) {
            return str.trim().replace("\n", "").replace("\t", "");
        } else {
            return str;
        }

    }

    public static Date convertToDate(String strDate) throws ParseException {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat(CONSTANT_PARAM_DATE_PATTERN);
        String cleanedStr = cleanupStr(strDate);
        if (cleanedStr != null) {
            return simpleFormatter.parse(cleanedStr);
        } else {
            logger.error("The date string is null");
            throw new ParseException("Date string is null", -100);
        }
    }
}
