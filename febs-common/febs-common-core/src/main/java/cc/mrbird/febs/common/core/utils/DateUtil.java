package cc.mrbird.febs.common.core.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author MrBird
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";
    public static final String FULL_TIME = "yyyyMMddHHmmssSSS";
    public static final String FULL_TIME_1 = "yyyy-MM-dd";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static Long getMills() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化时间，格式为 yyyyMMddHHmmss
     *
     * @param localDateTime LocalDateTime
     * @return 格式化后的字符串
     */

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    // 判断是否为昨天
    public static Boolean isToday(Timestamp someDay) {
        Calendar todayDate = Calendar.getInstance();

        Calendar oldDate = Calendar.getInstance();
        oldDate.setTime(someDay);

        int today = todayDate.get(Calendar.DAY_OF_MONTH);
        int oldday = oldDate.get(Calendar.DAY_OF_MONTH);

        if (today - 1 == oldday) {
            return true;
        } else {
            return false;
        }

    }

    // 获取30天后的日期
    public static Calendar getNDayToDate(int i) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, i);

        // String endDate = new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
        return now;
    }

    // 获取今天的日期加时间
    public static Timestamp getNowDateTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    // 获取今天的日期
    public static String getNowdateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // 获取昨天的日期
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    // 获取明天的日期
    public static String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrow = sdf.format(calendar.getTime());
        return tomorrow;
    }

    public static String getYestoryNDateTime(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    // 获取当前日期的3个月前的日期
    public static String getYestoryThreeDate(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    public static String getYestoryNDate(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    public static Date getNowdateToDate() {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(new Date());
            d = new SimpleDateFormat("yyyy-MM-dd").parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    //获取日期中的天数
    public static int getDay(Date datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    // 获取今天的日期加时间
    public static String getNowdateTimeToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取当月的总天数
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getDaysOfMonth(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date strToDate = getStrToDate(date);
        cal.setTime(strToDate);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }



    private static Date getStrToDate(String dateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(dateStr);
    }


    // 获取第specifiedDay天的arguments天前的日期
    public static String getSpecifiedDayBefore(String specifiedDay, int arguments) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - arguments);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    public static String dateChangeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static Date strOrDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Timestamp dateChangeTimestamp(Date datetime) {
        return new Timestamp(datetime.getTime());
    }

    public static Timestamp stringChangeTimestamp(String datetime) {
        return Timestamp.valueOf(datetime);
    }

    public static String timestampChangeString(Timestamp datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(datetime);
    }

    public static String timestampChangeStringToDate(Timestamp checkTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(checkTime);
    }

    /**
     * 判断某个时间是否在某个时间段内
     *
     * @param begintime 开始时间
     * @param endtime   结束时间
     * @param sdf       时间格式
     * @return
     */
    public static boolean checkday(String begintime, String endtime, SimpleDateFormat sdf) {
        // 当前时间
        Date today = new Date();
        // 格式化格式
        String format = sdf.format(today);
        try {
            // 转换为时间
            Date now = sdf.parse(format);
            // 把开始时间转换为时间
            Date begin = sdf.parse(begintime);
            // 把结束时间转换为时间
            Date end = sdf.parse(endtime);
            // 比较时间大小 当前时间是否大于等于开始时间
            boolean b = now.getTime() >= begin.getTime();
            // 当前时间是否小于等于结束检查时间
            boolean e = now.getTime() <= end.getTime();

            // 判断是否满足条件
            if (b && e) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断某个时间是否在某个时间段内
     *
     * @param date      判断时间
     * @param begintime 开始时间
     * @param endtime   结束时间
     * @param sdf       时间格式
     * @return
     */
    public static boolean checkdate(String date, String begintime, String endtime, SimpleDateFormat sdf) {
        // 格式化格式
        try {
            Date parse = sdf.parse(date);
            String format = sdf.format(parse);
            // 转换为时间
            Date now = sdf.parse(format);
            // 把开始时间转换为时间
            Date begin = sdf.parse(begintime);
            // 把结束时间转换为时间
            Date end = sdf.parse(endtime);
            // 比较时间大小 当前时间是否大于等于开始时间
            boolean b = now.getTime() >= begin.getTime();
            // 当前时间是否小于等于结束检查时间
            boolean e = now.getTime() <= end.getTime();

            // 判断是否满足条件
            if (b && e) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static int getNowYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

    public static int getNowMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 判断当前日期是否在周五到周日
     */
    public static boolean checkweek() {
        // 获取当前时间为星期几
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        if (w == 0 || w == 5 || w == 6) { // 周五周六周日
            return true;
        }
        return false;
    }

    /**
     * 判断当前日期是否为周末
     */
    public static boolean checkweeks() {
        // 获取当前时间为星期几
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        if (w == 0 || w == 6) { // 周六周日
            return true;
        }
        return false;
    }

    /**
     * 判断当前日期是否为每月最后三天
     */
    // days为传入的最后多少天参数
    public static boolean getLastDays(int days) {
        boolean flag = true;

        String s = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String strDate = df.format(date);

        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i <= days; i++) {
            // 取当前月的下一个月
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 1);
            // 取当前月的下一个月往前推i天
            calendar.add(Calendar.DATE, -i);
            Date theDate = calendar.getTime();
            s = df.format(theDate);
            // strDate为当前日期
            if (strDate.equals(s)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取某个日期的前N天日期
     *
     * @param date 指定日期
     * @param day  前几天
     * @return 前几天的日期
     * @throws ParseException
     */
    public static String getBeforeDayToDate(String date, Integer day) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 指定一个日期
        Date riqi = dateFormat.parse(date);
        // 对 calendar 设置为 date 所定的日期
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(riqi);
        // 如果是后退几天，就写 -天数 例如：
        rightNow.add(Calendar.DAY_OF_MONTH, -day);
        // 进行时间转换
        String format = dateFormat.format(rightNow.getTime());
        return format;
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param localDateTime LocalDateTime
     * @param format        格式
     * @return 格式化后的字符串
     */
    public static String formatFullTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param date   Date
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param date Date
     * @return 格式化后的字符串
     */
    public static String getDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化 CST类型的时间字符串
     *
     * @param date   CST类型的时间字符串
     * @param format 格式
     * @return 格式化后的字符串
     * @throws ParseException 异常
     */
    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return getDateFormat(usDate, format);
    }

    /**
     * 格式化 CST类型的时间字符串
     *
     * @param date   CST类型的时间字符串
     * @param format 格式
     * @return 格式化后的字符串
     * @throws ParseException 异常
     */
    public static String formatTime(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return getDateFormat(usDate, FULL_TIME_SPLIT_PATTERN);
    }

    /**
     * 格式化 Instant
     *
     * @param instant Instant
     * @param format  格式
     * @return 格式化后的字符串
     */
    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 判断当前时间是否在指定时间范围
     *
     * @param from 开始时间
     * @param to   结束时间
     * @return 结果
     */
    public static boolean between(LocalTime from, LocalTime to) {
        LocalTime now = LocalTime.now();
        return now.isAfter(from) && now.isBefore(to);
    }

    public static String getBeginWeek() {
        return getBeginWeek(new Date());

    }

    public static String getBeginWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setMinTimeOfDay(calendar);
        return getDateFormat(calendar.getTime());
    }

    public static String getBeginWeek(String date) {
        return getBeginWeek(new Date(date));
    }

    private static void setMaxTimeOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    private static void setMinTimeOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static String getEndWeek() {
        return getEndWeek(new Date());
    }

    public static String getEndWeek(String date) {
        return getEndWeek(new Date(date));
    }

    public static String getEndWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        setMaxTimeOfDay(calendar);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getDateFormat(calendar.getTime());
    }

    public static String getBeginDate() {
        return getBeginDate(new Date());

    }

    /**
     * 获取当月开始时间戳
     *
     * @return
     */
    public static String getMonthStartTime() {
        return getMonthStartTime(new Date());
    }

    /**
     * 获取当月开始时间戳
     *
     * @return
     */
    public static String getMonthStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return getDateFormat(calendar.getTime());
    }

    /**
     * 获取当月的结束时间戳
     *
     * @return
     */
    public static String getMonthEndTime() {
        return getMonthEndTime(new Date());
    }

    /**
     * 获取当月的结束时间戳
     *
     * @return
     */
    public static String getMonthEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return getDateFormat(calendar.getTime());
    }

    public static String getBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return getDateFormat(calendar.getTime());
    }

    public static String getEndDate() {
        return getEndDate(new Date());
    }

    public static String getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return getDateFormat(calendar.getTime());
    }
}
