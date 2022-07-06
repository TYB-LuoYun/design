package top.anets.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author LuoYun
 * @since 2022/7/5 9:52
 */
public class DateHelp {
    private static final String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    //每一个线程
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new
            ThreadLocal<SimpleDateFormat>();

    private static final Object object = new Object();


    /**
     * 获取SimpleDateFormat
     *
     * @param pattern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        long day = 0;
        try {
            Date date = getDateFormat("yyyy-MM-dd").parse(sj1);
            Date mydate = getDateFormat("yyyy-MM-dd").parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = getDateFormat("yyyy-MM-dd").parse(strDate, pos);
        return strtodate;
    }

    // 上月第一天
    public static String getPreviousMonthDayBegin() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为上月的1号
        return getDateFormat("yyyy-MM-dd").format(lastDate.getTime()) + " 00:00:00";
    }

    // 获得上月最后一天的日期
    public static String getPreviousMonthDayEnd() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = getDateFormat("yyyy-MM-dd").format(lastDate.getTime()) + " 23:59:59";
        return str;
    }

    // 获取当月第一天
    public static String getCurrentMonthDayBegin() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        return getDateFormat("yyyy-MM-dd").format(lastDate.getTime()) + " 00:00:00";
    }

    // 计算当月最后一天,返回字符串
    public static String getCurrentMonthDayEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return getDateFormat("yyyy-MM-dd").format(lastDate.getTime()) + " 23:59:59";
    }

    // 获取当天时间
    public static String getToday() {
        Date now = new Date();
        String hehe = getDateFormat("yyyy-MM-dd").format(now);
        return hehe;
    }

    // 获取当天时间
    public static String getYesterday() {
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DAY_OF_YEAR, -1);
        String hehe = getDateFormat("yyyy-MM-dd").format(cd.getTime());
        return hehe;
    }

    // 获取当天时间
    public static String getTomday() {
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DAY_OF_YEAR, +1);
        String hehe = getDateFormat("yyyy-MM-dd").format(cd.getTime());
        return hehe;
    }

    // 获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    // 获得当前日期与本周一相差的天数
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK); // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    // 获得本周一的日期
    public static String getCurrentWeekDayBegin() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday + " 00:00:00";
    }

    // 获得本周星期日的日期
    public static String getCurrentWeekDayEnd() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday + " 23:59:59";
    }

    // 获得相应周的周六的日期
    public static String getSaturday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 5);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday;
    }

    // 获得上周星期一的日期
    public static String getPreviousWeekDayBegin() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 7);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday + " 00:00:00";
    }

    // 获得上周星期日的日期
    public static String getPreviousWeekDayEnd() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 1);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday + " 23:59:59";
    }

    // 获得下周星期一的日期
    public static String getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday;
    }

    // 获得下周星期日的日期
    public static String getNextSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        String preMonday = getDateFormat("yyyy-MM-dd").format(monday);
        return preMonday;
    }

    private static int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    // 获得下个月第一天的日期
    public static String getNextMonthFirst() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = getDateFormat("yyyy-MM-dd").format(lastDate.getTime());
        return str;
    }

    // 获得下个月最后一天的日期
    public static String getNextMonthEnd() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = getDateFormat("yyyy-MM-dd").format(lastDate.getTime());
        return str;
    }

    // 获得明年最后一天的日期
    public static String getNextYearEnd() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = getDateFormat("yyyy-MM-dd").format(lastDate.getTime());
        return str;
    }

    // 获得明年第一天的日期
    public static String getNextYearFirst() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = getDateFormat("yyyy-MM-dd").format(lastDate.getTime());
        return str;

    }

    // 获得本年有多少天
    private static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    // 获得本年第一天的日期
    public static String getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        String preYearDay = getDateFormat("yyyy-MM-dd").format(yearDay);
        return preYearDay + " 00:00:00";
    }

    // 获得本年最后一天的日期 *
    public static String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31 23:59:59";
    }

    // 获得上年第一天的日期 *
    public static String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-01-01 00:00:00";
    }

    // 获得上年最后一天的日期
    public static String getPreviousYearEnd() {
        int yearPlus = getYearPlus();
        int MaxYear = 0; //一年最大天数
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * (-1)
                + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        String preYearDay = getDateFormat("yyyy-MM-dd").format(yearDay);
        return preYearDay + " 23:59:59";
    }

    // 获得本季度
    public static String getThisSeasonTime(int month) {
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        String start_days = "01";// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days
                + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }

    public static String getThisSeasonStartTime() {
        Calendar lastDate = Calendar.getInstance();
        int month = lastDate.get(Calendar.MONTH)+1;
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        String start_days = "01";// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate + " 00:00:00";
    }

    public static String getThisSeasonEndTime() {
        Calendar lastDate = Calendar.getInstance();
        int month = lastDate.get(Calendar.MONTH)+1;
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate + " 23:59:59";
    }

    public static String getLastSeasonTime() {
        Calendar lastDate = Calendar.getInstance();
        int month = lastDate.get(Calendar.MONTH);
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        season -= 1;
        if (season == 0){
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        if (season == 4){
            years_value -= 1;
        }
        String start_days = "01";// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days
                + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }


    public static String getLastSeasonStartTime() {
        Calendar lastDate = Calendar.getInstance();
        int month = lastDate.get(Calendar.MONTH);
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        season -= 1;
        if (season == 0){
            season = 4;
        }
        int start_month = array[season - 1][0];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        if (season == 4){
            years_value -= 1;
        }
        String start_days = "01";// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate + " 00:00:00";
    }


    public static String getLastSeasonEndTime() {
        Calendar lastDate = Calendar.getInstance();
        int month = lastDate.get(Calendar.MONTH);
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        season -= 1;
        if (season == 0){
            season = 4;
        }
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        if (season == 4){
            years_value -= 1;
        }
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate + " 23:59:59";
    }

    public static String getLastSeasonTime(int month) {
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        season -= 1;
        if (season == 0){
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        if (season == 4){
            years_value -= 1;
        }
        String start_days = "01";// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days
                + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year
     *            年
     * @param month
     *            月
     * @return 最后一天
     */
    private static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year
     *            年
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 传入时间-8小时
     * @param date
     * @return
     */
    public static String getDateForEs(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -8);
        date = cal.getTime();
        return getISO8601Timestamp(date);

    }

    /**
     * 传入时间-8小时
     * @param date
     * @return
     */
    public static String getDateForEs(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(strToDate(date));
        cal.add(Calendar.HOUR, -8);
        return getISO8601Timestamp(cal.getTime());

    }

    /**
     *  utc转换
     * @param date
     * @return
     */
    public static String getISO8601Timestamp(Date date) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");

        SimpleDateFormat df = null;
        df = threadLocal.get();
        if (df == null){
            df = new SimpleDateFormat(format);
        }

        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;

    }
}
