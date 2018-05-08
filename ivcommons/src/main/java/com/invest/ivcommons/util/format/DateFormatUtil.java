package com.invest.ivcommons.util.format;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xugang on 2017/8/3.
 */
public final class DateFormatUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateFormatUtil.class);

    public static final String PATTERN_UTC_NO_ZONE_WITHMILLIS = "yyyy-MM-dd'T'HH:mm:ss.S";
    public static final String PATTERN_UTC_NO_ZONE_WITHSECOND = "yyyy-MM-dd'T'HH:mm:ss";

    public enum DATETIME_PROTOCOL {
        RFC1123, ISO8861;
    }

    public static Date convertStr2Date(String datetimeTxt, DATETIME_PROTOCOL protocol) {

        if (DATETIME_PROTOCOL.RFC1123.equals(protocol)) {
            DateTimeFormatter rfc1123DateTimeFormatter = DateTimeFormat
                    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZoneUTC().withLocale(Locale.ENGLISH);
            DateTime dt = rfc1123DateTimeFormatter.parseDateTime(datetimeTxt);
            return dt.toDate();

        } else if (DATETIME_PROTOCOL.ISO8861.equals(protocol)) {

            DateTimeFormatter iso8861Formatter = ISODateTimeFormat.dateTime().withZoneUTC();
            DateTime dt = iso8861Formatter.parseDateTime(datetimeTxt);
            return dt.toDate();
        }
        return null;
    }

    public static String convertDate2Str(Date date, DATETIME_PROTOCOL protocol) {
        if (DATETIME_PROTOCOL.RFC1123.equals(protocol)) {
            // Change back , the date string should be GMT format
            DateTimeFormatter rfc1123DateTimeFormatter = DateTimeFormat
                    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZoneUTC().withLocale(Locale.ENGLISH);

            return rfc1123DateTimeFormatter.print(date.getTime());

        } else if (DATETIME_PROTOCOL.ISO8861.equals(protocol)) {

//            DateTimeFormatter ISO8861_FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC(); //2017-08-03T06:36:16.608Z
            DateTimeFormatter ISO8861_FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC();
//            DateTimeFormatter ISO8861_FORMATTER = ISODateTimeFormat.dateTime().withOffsetParsed(); //2017-08-03T14:45:00.656+08:00
            return ISO8861_FORMATTER.print(date.getTime());
        }
        return null;
    }

    public static String convertDate2Str(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date convertStr2Date(String datetimeTxt, String pattern) {
        if (StringUtils.isBlank(datetimeTxt)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(datetimeTxt);
        } catch (Exception e) {
            logger.error("日期转换失败,datetimeTxt=" + datetimeTxt + ",pattern=" + pattern, e);
        }
        return null;
    }

    public static void main(String[] args) {
        DateTime now = new DateTime();
        String nowstring = DateFormatUtil.convertDate2Str(now.toDate(), DATETIME_PROTOCOL.ISO8861);
        System.out.println(nowstring);

        Date test = DateFormatUtil.convertStr2Date("2017-08-03T09:15:41.167Z", DATETIME_PROTOCOL.ISO8861);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        System.out.println(simpleDateFormat.format(test)); //2017-08-03 17:15:41.167
//        String s = "2017-08-03T09:15:41.167";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.S"); //java.lang.IllegalArgumentException: Illegal pattern character 'T'
//        System.out.println(simpleDateFormat.format(now.toDate()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        String time = sdf.format(new Date());
        System.out.println(time);

        System.out.println(sdf.format(DateFormatUtil.convertStr2Date("2017-08-03T09:15:41.167", PATTERN_UTC_NO_ZONE_WITHSECOND)));
    }
}
