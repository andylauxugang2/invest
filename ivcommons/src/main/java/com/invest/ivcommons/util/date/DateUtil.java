package com.invest.ivcommons.util.date;

import com.invest.ivcommons.util.format.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xugang on 2016/11/1.
 */
public final class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_FORMAT_DATE_COMMON = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DATE_MONTH_COMMON = "yyyy-MM";
    public static final String DATE_FORMAT_DATETIME_COMMON = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_DATETIME_FORMAT_MISECOND = "yyyy-MM-dd HH:mm:ss.S";

    public static Date getEndOfDay(Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    public static Date getStartOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static Date safeParseDateErrorNull(String dateString, String... patterns) {
        try {
            return DateUtils.parseDate(dateString, patterns);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 将日期转成yyyyMMdd格式
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * @param pBeginTime
     * @param pEndTime
     * @return
     * @throws ParseException
     */
    public static Integer timeDiffForDay(Date pBeginTime, Date pEndTime) {
        Long beginL = pBeginTime.getTime();
        Long endL = pEndTime.getTime();
        Integer day = Integer.parseInt(String.valueOf((endL - beginL) / 86400000));
        return day;

    }

    /**
     * 计算当前时间到凌晨的时间间隔 秒级
     *
     * @return
     */
    public static long timeMorningInterval() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        // 计算剩余时间 23:59:59
        long interval = (23 * 60 * 60 + 59 * 60 + 59) - (hour * 60 * 60 + minute * 60 + second * 60);
        return interval;
    }

    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }

    /**
     * 校验日期格式是否正确，
     *
     * @param date
     * @param format 如："yyyy-MM-dd"
     * @return
     */
    public static boolean isDateFormatValid(String date, String format) {
        boolean result = true;
        SimpleDateFormat rule = new SimpleDateFormat(format);
        rule.setLenient(false);
        try {
            rule.parse(date);
        } catch (ParseException e) {
            result = false;
        }
        return result;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date     日期
     * @param parttern 日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String parttern) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        String dateString = StringUtils.EMPTY;
        try {
            dateString = getDateFormat(parttern).format(date);
        } catch (Exception e) {
            logger.error("日期操作异常", e);
        }
        return dateString;
    }

    public static String dateToString(Date date) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        String dateString = StringUtils.EMPTY;
        try {
            dateString = getDateFormat(DATE_FORMAT_DATETIME_COMMON).format(date);
        } catch (Exception e) {
            logger.error("日期操作异常", e);
        }
        return dateString;
    }

    /**
     * 将日期字符串装还成日期
     *
     * @param time
     * @param parttern
     * @return
     */
    public static Date stringToDate(String time, String parttern) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        try {
            return getDateFormat(parttern).parse(time);
        } catch (Exception e) {
            logger.error("日期操作异常", e);
        }
        return null;
    }

    /**
     * 时间加减操作
     *
     * @param date
     * @param amount
     * @param field
     * @return
     */
    public static Date dateAdd(Date date, int amount, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * 从当前时间进行时间加减操作
     *
     * @param amount
     * @param field
     * @return
     */
    public static Date dateAddFromToday(int amount, int field) {
        return dateAdd(new Date(), amount, field);
    }

    public static Date getCurrentDatetime() {
        return new Date(System.currentTimeMillis());
    }

    public static int yearBetween(Date time1, Date time2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(time1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(time2);

        return Math.abs(cal1.getWeekYear() - cal2.getWeekYear());
    }

    /**
     * 如果d1>d2返回 月数差 否则返回0
     *
     * @param bigTime
     * @param smallTime
     * @return
     */
    public static int monthBetween(Date bigTime, Date smallTime) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(bigTime);
        c2.setTime(smallTime);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) yearInterval--;
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) monthInterval--;
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    public static int dayBetween(Date time1, Date time2) {
        if (null == time1 || null == time2) {
            return -1;
        }

        long intervalMilli = Math.abs(time2.getTime() - time1.getTime());
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

    public static Date truncate2Day(Date date) {
        String str = dateToString(date, DATE_FORMAT_DATE_COMMON);
        return stringToDate(str, DATE_FORMAT_DATE_COMMON);
    }

    public static void main(String[] args) {
        Date start = DateFormatUtil.convertStr2Date("2017-09-13T20:17:40", DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHSECOND);
        int days = DateUtil.dayBetween(start, new Date(System.currentTimeMillis()));
        System.out.println(days);

        Date startMonth = DateFormatUtil.convertStr2Date("2016-01-14T04:57:40.473", DateFormatUtil.PATTERN_UTC_NO_ZONE_WITHMILLIS);
        int months = monthBetween(new Date(System.currentTimeMillis()), startMonth);
        System.out.println(months);

        Date now = getCurrentDatetime();
        System.out.println(dateToString(dateAdd(now, 90, Calendar.DATE), DATE_FORMAT_DATETIME_COMMON));


        Date date2 = stringToDate("2015-11-11 12:01:12.123", DATE_FORMAT_DATETIME_FORMAT_MISECOND);
        System.out.println(dateToString(date2, DATE_FORMAT_DATETIME_FORMAT_MISECOND));
    }
}