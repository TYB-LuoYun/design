package top.anets.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期操作辅助类
 *
 * @author gxw
 * @version $Id: DateUtil.java, v 0.1 2017年4月14日 上午8:58:11 gxw
 */
public final class DateUtil {
    public static final String PATTERN_DATE_CHINA = "yyyy年MM月dd日 HH:mm:ss";

    private DateUtil() {
    }

    public static Map<String, ThreadLocal<SimpleDateFormat>> dateFormatMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    private static final Object lock = new Object();

    public static final String PATTERN1 = "yyyyMMdd";
    public static final String PATTERN2 = "yyyyMM";
    public static final String PATTERN3 = "yyyy";
    public static final String PATTERN4 = "yyMMdd";
    public static final String PATTERN5 = "yyMM";
    public static final String PATTERN6 = "yy";

    /**
     * 获取sdf
     *
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> threadSdf = dateFormatMap.get(pattern);

        //双重判断加synchronized 确保不会向map中重复添加相同格式的SimpleDateFormat
        if (threadSdf == null) {
            synchronized (lock) {
                threadSdf = dateFormatMap.get(pattern);
                if (threadSdf == null) {

                    threadSdf = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    dateFormatMap.put(pattern, threadSdf);
                }
            }
        }
        return threadSdf.get();
    }

    //两个线程安全的Calendar类
    private static ThreadLocal<Calendar> safeCal1 = new ThreadLocal<Calendar>() {
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    };

    private static ThreadLocal<Calendar> safeCal2 = new ThreadLocal<Calendar>() {
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    };

    public static SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd");

    public static SimpleDateFormat sdfLongTiem = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    static SimpleDateFormat sdfLongTime = new SimpleDateFormat("yyyyMMddHHmmss");

    static SimpleDateFormat sdfLongTimePlusMill = new SimpleDateFormat(
            "yyyyMMddHHmmssSSSS");

    public static final String PATTERN = "yyyy-MM-dd";

    public static final String PATTERN_SHORT_DATA = "yyyyMMdd";

    public static final String PATTERN_SEC = "yyyyMMddHHmmss";

    public static final String PATTERN_MILSEC = "yyyyMMddHHmmssSSSS";

    public static final String PATTERN_DATE = "yyyy-MM-dd";

    public static final String PATTERN_TIME_STAMP = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_HOUR_MIN = "HH:mm";

    public static final String PATTERN_DATE_HOUR_MIN = "yyyyMMdd HH:mm";

    public static final String PATTERN_CHINA_DATE = "MM月dd日 ";

    public static final String PATTERN_CHINA_WEEK = "E";

    private static long DAY_IN_MILLISECOND = 0x5265c00L;

//    public static String getOrderNo() {
//        Date date = new Date();
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        return df.format(date) + DataUtil.rand(100);
//    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static final String format(Object date) {
        return format(date, PATTERN);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 线程安全格式化日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date safeString2Date(String dateStr, String pattern) {

        if (dateStr == null || dateStr.equals("")) {
            throw new RuntimeException("str date null");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = DateUtil.PATTERN_DATE;
        }

        Date result = null;
        try {
            result = parse(dateStr, pattern);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 线程安全日期格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * 线程安全解析日期字符串
     *
     * @param dateString
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateString);
    }

    /**
     * 线程安全日期字符串格式转换
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static String formatData(String dateString) throws ParseException {
        return format(getSdf(PATTERN_SHORT_DATA).parse(dateString));
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static final String getDate() {
        return format(new Date());
    }

    /**
     * 获取日期时间
     *
     * @return
     */
    public static final String getDateTime() {
        return format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取日期
     *
     * @param pattern
     * @return
     */
    public static final String getDateTime(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 日期计算
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static final Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 字符串转换为日期:不支持yyM[M]d[d]格式
     *
     * @param date
     * @return
     */
    public static final Date stringToDate(String date) {
        if (date == null) {
            return null;
        }
        String separator = String.valueOf(date.charAt(4));
        String pattern = "yyyyMMdd";
        if (!separator.matches("\\d*")) {
            pattern = "yyyy" + separator + "MM" + separator + "dd";
            if (date.length() < 10) {
                pattern = "yyyy" + separator + "M" + separator + "d";
            }
        } else if (date.length() < 8) {
            pattern = "yyyyMd";
        }
        pattern += " HH:mm:ss.SSS";
        pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @param @param  str
     * @param @param  strFormat
     * @param @return
     * @return Date
     * @throws
     * @Title: stringToDate
     * @Description: 字符串以指定格式转化为Date
     */
    public static Date stringToDate(String str, String strFormat) {
        SimpleDateFormat sdFormat = new SimpleDateFormat(strFormat);
        Date date = new Date();
        try {
            date = sdFormat.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * @param @param  dt
     * @param @return
     * @return String
     * @throws
     * @Title: dateToString
     * @Description: Date转换为字符串 yyyy-MM-dd HH:mm
     */
    public static String dateToString(Date dt) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = "";
        try {
            if (dt != null) {
                str = sdFormat.format(dt);
            }
        } catch (Exception e) {
            return "";
        }
        if ("1900-01-01 00:00".equals(str)) {
            str = "";
        }

        return str;
    }

    /**
     * @param @param  dt
     * @param @param  strFormat
     * @param @return
     * @return String
     * @throws
     * @Title: dateToString
     * @Description: Date按指定格式转换为字符串
     */
    public static String dateToString(Date dt, String strFormat) {
        SimpleDateFormat sdFormat = new SimpleDateFormat(strFormat);
        String str = "";
        try {
            str = sdFormat.format(dt);
        } catch (Exception e) {
            return "";
        }
        if ("1900-01-01".equals(str)) {
            str = "";
        }

        return str;
    }

    public static Date addYear(Date start, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.YEAR, offset);
        return c.getTime();
    }

    /**
     * 间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getDayBetween(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        long n = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (n / (60 * 60 * 24 * 1000L));
    }

    /**
     * 距今天数
     *
     * @param startMillis
     * @return
     */
    public static final Integer getDayBetween(long startMillis) {
        Calendar current = Calendar.getInstance();
        long n = current.getTimeInMillis() - startMillis;
        return (int) (n / (60 * 60 * 24 * 1000L));
    }

    /**
     * 间隔秒数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getSecondBetween(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        long n = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (n / (1000L));
    }

    /**
     * 间隔月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n = (year2 - year1) * 12;
        n = n + month2 - month1;
        return n;
    }

    /**
     * 间隔月，多一天就多算一个月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n = (year2 - year1) * 12;
        n = n + month2 - month1;
        int day1 = start.get(Calendar.DAY_OF_MONTH);
        int day2 = end.get(Calendar.DAY_OF_MONTH);
        if (day1 <= day2) {
            n++;
        }
        return n;
    }

    /**
     * 获取当前日期为日期型
     *
     * @return 当前日期，java.util.Date类型
     */
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        Date d = cal.getTime();
        return d;
    }

    /**
     * 获取统计时间点，如刚好是整点则在原时间点-1小时
     * <p>如统计时间为7点01分，则时间点为8点</p>
     *
     * @param endTime 结束时间
     * @return
     */
    public static int getTimeBreak(Date endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endTime);
        if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0) {
            cal.add(Calendar.HOUR, -1);
        }
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到当前年份值:1900
     *
     * @return String
     * @throws Exception
     */
    public static String getNowYear() throws Exception {
        String nowYear = "";
        try {
            String strTemp = getNowLongTime();
            nowYear = strTemp.substring(0, 4);
            return nowYear;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 得到当前月份值:12
     *
     * @return String
     * @throws Exception
     */
    public static String getNowMonth() throws Exception {
        String nowMonth = "";
        try {
            String strTemp = getNowLongTime();
            nowMonth = strTemp.substring(4, 6);
            return nowMonth;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 得到当前日期值:30
     *
     * @return String
     * @throws Exception
     */
    public static String getNowDay() throws Exception {
        String nowDay = "";
        try {
            String strTemp = getNowLongTime();
            nowDay = strTemp.substring(6, 8);
            return nowDay;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 得到当前小时值:23
     *
     * @return String
     * @throws Exception
     */
    public static String getNowHour() throws Exception {
        String nowHour = "";
        try {
            String strTemp = getNowPlusTimeMill();
            nowHour = strTemp.substring(8, 10);
            return nowHour;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Descrption:取得当前日期时间,格式为:YYYYMMDDHHMISS
     *
     * @return String
     * @throws Exception
     */
    public static String getNowLongTime() throws Exception {
        String nowTime = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowTime = format(date, PATTERN_SEC);
            return nowTime;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Descrption:取得当前日期到毫秒极,格式为:yyyyMMddHHmmssSSSS
     *
     * @return String
     * @throws Exception
     */
    public static String getNowPlusTimeMill() throws Exception {
        String nowDate = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowDate = format(date, PATTERN_MILSEC);
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取输入日期的零点 如传入时间为yyyy-mm-dd hh:mm:ss 返回为yyyy-mm-dd 00:00:00
     *
     * @param dt
     * @return
     */
    public static Date getBegOfDay(Date dt) {
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = null;
        try {
            date = dateformat1.parse(dateformat1.format(dt));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * Descrption:取得当前日期,格式为:YYYYMMDD
     *
     * @return String
     * @throws Exception
     */
    public static String getNowShortDate() throws Exception {
        String nowDate = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowDate = format(date, "yyyyMMdd");
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 得到两个日期之间相差的天数+小时+分钟+秒
     *
     * @param arriveTime 小的日期 is not null
     * @param leaveTime  大的日期 is not null
     * @return exp.3天5小时40分钟30秒
     */
    public static String daysHmsBetweenDates(Timestamp arriveTime, Timestamp leaveTime) {
        long diff = leaveTime.getTime() - arriveTime.getTime();
        int days = (int) (diff / (24 * 60 * 60 * 1000));
        diff %= 24 * 60 * 60 * 1000;
        int hours = (int) (diff / (60 * 60 * 1000));
        diff %= 60 * 60 * 1000;
        int minites = (int) (diff / (60 * 1000));
        diff %= 60 * 1000;
        int second = (int) (diff / 1000);
        String duraction = "";
        if (days != 0) {

            duraction += days + "天";
        }
        if (hours != 0) {

            duraction += hours + "小时";
        }
        if (minites != 0) {

            duraction += minites + "分钟";
        }
        if (second != 0) {

            duraction += second + "秒";
        }
        return duraction;
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 得到两个日期之间相差的天数
     *
     * @param newDate 大的日期
     * @param oldDate 小的日期
     * @return newDate-oldDate相差的天数
     */
    public static int daysBetweenDates(Date newDate, Date oldDate) {
        int days = 0;
        Calendar calo = Calendar.getInstance();
        Calendar caln = Calendar.getInstance();
        calo.setTime(oldDate);
        caln.setTime(newDate);
        int oday = calo.get(Calendar.DAY_OF_YEAR);
        int nyear = caln.get(Calendar.YEAR);
        int oyear = calo.get(Calendar.YEAR);
        while (nyear > oyear) {
            calo.set(Calendar.MONTH, 11);
            calo.set(Calendar.DATE, 31);
            days = days + calo.get(Calendar.DAY_OF_YEAR);
            oyear = oyear + 1;
            calo.set(Calendar.YEAR, oyear);
        }
        int nday = caln.get(Calendar.DAY_OF_YEAR);
        days = days + nday - oday;

        return days;
    }

    /**
     * 得到两个日期之间相差的天数（线程安全）
     *
     * @param newDate 大的日期
     * @param oldDate 小的日期
     * @return newDate-oldDate相差的天数
     */
    public static int safeDaysBetweenDates(Date newDate, Date oldDate) {
        int days = 0;
        safeCal1.get().setTime(oldDate);
        safeCal2.get().setTime(newDate);
        int oday = safeCal1.get().get(Calendar.DAY_OF_YEAR);
        int nyear = safeCal2.get().get(Calendar.YEAR);
        int oyear = safeCal1.get().get(Calendar.YEAR);
        while (nyear > oyear) {
            safeCal1.get().set(Calendar.MONTH, 11);
            safeCal1.get().set(Calendar.DATE, 31);
            days = days + safeCal1.get().get(Calendar.DAY_OF_YEAR);
            oyear = oyear + 1;
            safeCal1.get().set(Calendar.YEAR, oyear);
        }
        int nday = safeCal2.get().get(Calendar.DAY_OF_YEAR);
        days = days + nday - oday;

        return days;

    }

    /**
     * 得到将date增加指定天数后的date
     *
     * @param date       日期
     * @param intBetween 增加的天数
     * @return date 加上intBetween天数后的日期
     */
    public static Date increaseDay(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.DATE, intBetween);
        return calo.getTime();
    }

    /**
     * Returns a Date set to the last possible millisecond of the day, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfDay(Date day) {
        return getEndOfDay(day, Calendar.getInstance());
    }

    public static Date getEndOfDay(Date day, Calendar cal) {
        if (day == null) {
            day = new Date();
        }
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Returns a Date set to the first possible millisecond of the day, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfDay(Date day) {
        return getStartOfDay(day, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the first possible millisecond of the day, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfDay(Date day, Calendar cal) {
        if (day == null) {
            day = new Date();
        }
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * @param date
     * @param days
     * @return DATE 型加具体的天数
     */
    public static Date dateAddDays(Date date, int days) {
        long now = date.getTime() + (long) days * DAY_IN_MILLISECOND;
        return new Date(now);
    }

    public static Date dateDayAdd(Date date, int days) {
        long now = date.getTime() + (long) days * DAY_IN_MILLISECOND;
        return new Date(now);
    }

    /**
     * 获取当前时间所属这个月的第几天
     *
     * @return
     */
    public static int getDayOfMonth() {
        Date date = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int day = ca.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取当前时间所属这年的第几天
     *
     * @return
     */
    public static int getDayOfYear() {
        Date date = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int day = ca.get(Calendar.DAY_OF_YEAR);
        return day;
    }

    /**
     * 获取当月的第一天，例如2016-07-01
     */
    public static String getStringOfFirstDayInMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String temp = sdf.format(date);
        String firstDayInMoth = "";
        firstDayInMoth = temp + "-01";
        return firstDayInMoth;
    }

    /**
     * 日期字符串转Date
     *
     * @param dateStr 日期字符串
     * @param aFormat 日期转换模型
     */
    public static Date parse(String dateStr, SimpleDateFormat aFormat) throws ParseException {
        if (StringUtils.isEmpty(dateStr) || aFormat == null) {
            return null;
        }
        return aFormat.parse(dateStr);
    }

    public static String date2String(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("timestamp null illegal");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间 转换为 yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String changeDateFormat(String date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(PATTERN_STANDARD);//yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf2 = new SimpleDateFormat(PATTERN_SHORT_DATA);
        if (StringUtils.isEmpty(date)) {
            return sdf2.format(new Date());
        }
        try {
            return sdf2.format(sdf1.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return sdf2.format(new Date());
        }
    }


    /**
     * 获取输入日期的某一点 如传入时间为yyyy-mm-dd hh:mm:ss 返回为yyyy-mm-dd 00:00:00 parm 格式为
     * 00:00:00 或 02:00:00
     *
     * @param dt
     * @return
     */
    public static Date getTimeOfDay(Date dt, String parm) {
        if (parm == null || parm.length() <= 0) {
            parm = "00:00:00";
        }
        String time = getDateLong(dt) + " " + parm;
        Date date = string2Date(time, PATTERN_STANDARD);
        return date;
    }

    /**
     * 取得与原日期相差一定天数的日期，返回Date型日期
     *
     * @param date       原日期
     * @param intBetween 相差的天数
     * @return date加上intBetween天后的日期
     */
    public static Date getDateBetween(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.DATE, intBetween);
        return calo.getTime();
    }

    /**
     * 取得与原日期相差一定天数的日期，返回时间戳
     */
    public static long getDateBetween(int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(new Date());
        calo.add(Calendar.DATE, intBetween);
        return calo.getTimeInMillis();
    }

    /**
     * @return String
     * @throws Exception
     */
    public static String getDateLong(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = format(date, PATTERN);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    public static Date string2Date(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            throw new RuntimeException("str date null");
        }
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtil.PATTERN_DATE;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;

        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static Date getBegOfYesterday(Date date) {

        date = date == null ? DateUtil.dateAddDays(new Date(), -1) : date;
        return DateUtil.getBegOfDay(date);
    }

    /**
     * 字符串形式转化为Date类型 String类型按照format格式转为Date类型
     **/
    public static Date fromStringToDate(String format, String dateTime) throws ParseException {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = sdf.parse(dateTime);
        return date;
    }

    /**
     * 得到当前日期，格式yyyy-MM-dd。
     *
     * @return String 格式化的日期字符串
     */
    public static String getYesterday() {
        Date cDate = new Date();
        cDate.setTime(cDate.getTime() - 24 * 3600 * 1000);
        SimpleDateFormat cSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return cSimpleDateFormat.format(cDate);
    }

    /**
     * 得到本月最后一天的日期
     */
    public static String getLastDayOfMonth(String startTime) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date date = sf.parse(startTime);
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(cDay.getTime());
    }

    private static ThreadLocal<Calendar> safeCal3 = new ThreadLocal<Calendar>() {
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    };

    /**
     * 判断传入的时间是否是今天
     *
     * @param date 传入时间 格式为 yyyyMMdd
     * @return
     */
    public static boolean isToday(String date) {
        String current = format(new Date(), "yyyyMMdd");
        if (current.equals(date)) {
            return true;
        }
        return false;
    }

    /**
     * 获取固定年月日时间
     *
     * @param date
     * @return
     */
    public static Date getFixYYYYMMDD(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("timestamp null illegal");
        }
        Calendar calendar = safeCal3.get();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * long的日期格式的计算
     */
    public static int daysBetween(long smdate, long bdate) {
        long between_days = (bdate - smdate) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate) throws ParseException {
        String bdate = getYesterday();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int getage(String smdate) {
        int day = 0;
        try {
            day = daysBetween(smdate);
        } catch (ParseException p) {
            return day;
        }
        return day / 365;
    }

    /**
     * 指定年月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstFixYYYYMM(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        SimpleDateFormat sf = new SimpleDateFormat(PATTERN_STANDARD);
        return sf.format(cal.getTime());
    }

    /**
     * 指定年月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getEndFixYYYYMM(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        SimpleDateFormat sf = new SimpleDateFormat(PATTERN_STANDARD);
        return sf.format(cal.getTime());
    }

    /**
     * 具体时间加多少分钟
     *
     * @param inputDate       时间格式 yyyyMMdd HH:mm
     * @param preTimeInterval
     * @return
     * @throws ParseException
     */
    public static List<String> dateAddMin(String inputDate, int preTimeInterval, int size) {
        List<String> result = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(string2Date(inputDate, PATTERN_DATE_HOUR_MIN));
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_HOUR_MIN);
        for (int i = 0; i <= size; i++) {
            if (i == 0) {
                result.add(format.format(calendar.getTime()));
            } else {
                calendar.add(Calendar.MINUTE, preTimeInterval);
                result.add(format.format(calendar.getTime()));
            }
        }
        return result;
    }

    /**
     * 根据时间戳获取对应格式的时间字符串
     *
     * @param timeMillis
     * @return
     */
    public static String timeMillis2String(long timeMillis, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN_SHORT_DATA;
        }
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        if (timeMillis != 0) {
            cal.setTimeInMillis(timeMillis);
            date = cal.getTime();
        }
        return date2String(date, pattern);
    }


    /**
     * 根据对应格式的时间字符串 获取时间戳
     *
     * @param dateStr
     * @return
     */
    public static long stringDate2Millis(String dateStr, String pattern) {
        if (StringUtils.isNotBlank(dateStr)) {
            Date date = string2Date(dateStr, pattern);
            return date.getTime();
        } else {
            return 0;
        }
    }

    /**
     * 获取隔多少天日期字符串
     *
     * @param strDate 如果为空则获取现在日期的隔天
     *                day  多少天
     * @return
     * @throws ParseException
     */
    public static final String getNextStrDate(String strDate, int day, String pattern) {
        if (pattern == "") {
            pattern = PATTERN_SHORT_DATA;
        }
        Date date = new Date();
        if (StringUtils.isNotBlank(strDate)) {
            date = string2Date(strDate, pattern);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(cal.getTime());
    }

    /**
     * 获取指定时间后7天的信息
     *
     * @param strDate 时间 格式2月20号 星期几
     * @return
     */
    public static String[] getWeekInfo(String strDate, String pattern, int day) {
        String[] week = new String[7];
        Date date = new Date();
        if (StringUtils.isNotBlank(strDate)) {
            date = string2Date(strDate, PATTERN_SHORT_DATA);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (int i = 0; i < week.length; i++) {
            cal.add(Calendar.DATE, day);
            date = cal.getTime();
            week[i] = format(date, pattern);
        }
        return week;
    }

    public static String getOneWeekInfo(String strDate, String pattern, int day) {
        Date date = new Date();
        if (StringUtils.isNotBlank(strDate)) {
            date = string2Date(strDate, PATTERN_SHORT_DATA);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        date = cal.getTime();
        return format(date, pattern);
    }

    public static long getWeekInfo(String strDate, int day) {
        Date date = new Date();
        if (StringUtils.isNotBlank(strDate)) {
            date = string2Date(strDate, PATTERN_SHORT_DATA);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime().getTime();
    }

    /**
     * 获得时间对应的时间戳
     *
     * @param dateStr 时间字符串
     * @param pattern 时间格式
     * @return
     */
    public static String getTimeStamp(String dateStr, String pattern) {

        if (StringUtils.isNotBlank(dateStr)) {
            Date date = string2Date(dateStr, pattern);
            return String.valueOf(date.getTime());
        } else {
            return null;
        }
    }

    public static long getTimeBeforeDays(int days) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.set(Calendar.DATE, days);
        return ca.getTimeInMillis() / 1000;

    }

    /**
     * yyyy-MM-dd HH:mm:ss --> yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String changeFormat(String date) {
        try {
            Date d = sdfLongTiem.parse(date);
            return sdfShort.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss --> 时间戳
     *
     * @param date
     * @return
     */
    public static String changeFormatToTimes(String date) {
        try {
            Date d = sdfLongTiem.parse(date);
            d.getTime();
            return d.getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String changeFormatyyyyMMdd(Date date) {
        return sdfShort.format(date);
    }

    /**
     * 对比当前时间 如果比当前时间晚 返回1 否则0
     *
     * @param time
     * @return
     */
    public static int compareDateWithCur(String time) {
        Date cur = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        String[] timesplit = time.split(":");
        int hourOfDay = Integer.parseInt(timesplit[0]);
        int minute = Integer.parseInt(timesplit[1]);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), +cal.get(Calendar.DATE), hourOfDay, minute);
        if (cur.before(cal.getTime())) {
            return 0;
        }
        return 1;
    }

    /**
     * 今天零点零分零秒的毫秒数
     *
     * @return
     */
    public static long getCurrentBeginMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 今天23点59分59秒的毫秒数
     *
     * @return
     */
    public static long getCurrentEndMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定时间的零点零分零秒的毫秒数
     *
     * @param time 格式 yyyy-MM-dd HH:ss:mm
     * @return
     */
    public static long getTargetDateBeginMillis(String time) {
        if (StringUtils.isEmpty(time)) {
            return getCurrentEndMillis();
        }
        Date date = string2Date(time, PATTERN_STANDARD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定时间的23点59分59秒的毫秒数
     *
     * @param time 格式 yyyy-MM-dd HH:ss:mm
     * @return
     */
    public static long getTargetDateEndMillis(String time) {
        if (StringUtils.isEmpty(time)) {
            return getCurrentBeginMillis();
        }
        Date date = string2Date(time, PATTERN_STANDARD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    /**
     * 指定时间戳增加指定分钟后 与当前时间对比
     *
     * @param millis
     * @param min
     * @return true:大于当前时间 false：小于当前时间
     */
    public static boolean addMinThanCurrent(long millis, int min) {
        Calendar cur = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.add(Calendar.MINUTE, min);
        return cal.getTimeInMillis() > cur.getTimeInMillis() ? true : false;
    }

    /**
     * 增加分钟后的时间戳
     *
     * @param min
     * @return
     */
    public static long getCurAddMinMillis(int min) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, min);
        return cal.getTimeInMillis();
    }

    /**
     * 将时间戳转成 yyyy-MM-dd 格式
     *
     * @param time
     * @return
     */
    public static String getMirthDate(long time) {
        if (time <= 0) {
            return "";
        }
        Date date = new Date(time);
        return date2String(date, PATTERN_DATE);
    }

    /**
     * 按格式获取指定日期
     *
     * @param pattern
     * @return
     */
    public static String getDateTime(long millis, String pattern) {
        Date date = new Date(millis);
        return date2String(date, pattern);
    }

    /**
     * 2个时间戳相差的天数
     *
     * @param millis1
     * @param millis2
     * @return
     */
    public static int getDaysBewteenTwoMillis(long millis1, long millis2) {
        if (millis2 < millis1) {
            long millis3 = millis2;
            millis2 = millis1;
            millis1 = millis3;
        }
        int days = (int) ((millis2 - millis1) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 获取当天指定时间点的时间戳
     *
     * @return
     */
    public static String getTargetDateTime(int hh, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hh);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return date2String(cal.getTime(), pattern);
    }

    /**
     * 获取当前⽇期的0点时间
     */
    public static Date getDateZeroDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }
}