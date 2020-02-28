package com.sky.wordmem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 9/30/19
 */
@Slf4j
public class DatetimeUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    private static final String DETAIL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String MONTH_DAY_KEY_FORMAT = "MMdd";

    // HH是24小时制，hh是12小时制
    private static final String MONTH_DAY_HOUR_MINUTE_KEY_FORMAT = "MMddHHmm";

    public static String dateToUtcStringUntilDay(Date datetime) {
        datetime = DateUtils.addHours(datetime, -8);
        return dateToString(datetime, DEFAULT_DATE_FORMAT);
    }

    public static String dateToUtcStringUntilSecond(Date datetime) {
        datetime = DateUtils.addHours(datetime, -8);
        return dateToString(datetime, DETAIL_DATE_FORMAT);
    }

    public static String dateToString(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                DEFAULT_DATE_FORMAT);
        return sdf.format(datetime);
    }

    public static Date dateToUtcUntilDay(Date datetime) {
        datetime = stringToDate(dateToUtcStringUntilDay(datetime), DEFAULT_DATE_FORMAT);
        return datetime;
    }
    /**
     * convert date to string
     *
     * @param datetime
     * @param format
     * @return java.lang.String
     * @date 9/30/19 7:31 PM
     */
    public static String dateToString(Date datetime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(datetime);
    }
//    public static String getLastDayStr() {
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DATE, -1);
//        Date date = c.getTime();
//        return dateToString(date);
//    }
//
//    public static String dateToUtcDetailString(Date datetime) {
//        datetime = DateUtils.addHours(datetime, -8);
//        return dateToString(datetime, DETAIL_DATE_FORMAT);
//    }
//
//    public static String dateToDetailString(Date datetime) {
//        return dateToString(datetime, DETAIL_DATE_FORMAT);
//    }
//
//

//

//    }
//
    /**
     * convert string to datetime
     * if parse error, return null
     *
     * @param datetimeStr
     * @param format
     * @return java.util.Date
     * @date 9/30/19 7:30 PM
     */
    public static Date stringToDate(String datetimeStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date result = null;
        if (StringUtils.isEmpty(datetimeStr)) {
            return null;
        }
        try {
            result = sdf.parse(datetimeStr);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return result;
    }
//
//    public static String getMonthDayKey() {
//        return dateToString(new Date(), MONTH_DAY_KEY_FORMAT);
//    }
//
//    public static String getMonthDayHourMiniteKey() {
//        return dateToString(new Date(), MONTH_DAY_HOUR_MINUTE_KEY_FORMAT);
//    }
//
//    public static Date stringToDate(String datetimeStr) {
//        return stringToDate(datetimeStr, DEFAULT_DATE_FORMAT);
//    }
//
//    public static Long dateToSeconds(Date createTime) {
//        if (createTime == null) {
//            return null;
//        }
//        return createTime.getTime() / 1000;
//    }
//
//    public static Date millsToDate(Long l) {
//        return new Date(l);
//    }
}

