package com.ziyun.consume.tools;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarUtils {

    /**
     * 返回指定周几对应的日期， 比如上周日，返回20160710
     *
     * @param nWeek     ,对应现在的周的偏移量
     * @param dayOfWeek ,对应的星期几
     * @return
     */
    public static String getOffSetWeekDay(int nWeek, int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        // n为推迟的周数，0为本周， 1下周，-1向前推迟一周，2下周，依次类推
        cal.add(Calendar.DATE, nWeek * 7);
        // 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        String sunDay = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        return sunDay;
    }

    /**
     * 返回指定月的偏移量对应的日期， 比如上月一号，返回20160710
     *
     * @return
     * @int nWeek
     * ,对应现在的月的偏移量
     * @int dayOfWeek
     * ,对应的月的几号
     */
    public static String getOffSetMonthDay(int month, int dayOfmonth) {
        Calendar cal = Calendar.getInstance();
        // n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
        cal.add(Calendar.MONTH, month);
        // 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_MONTH, dayOfmonth);
        String sunDay = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        return sunDay;
    }

    /**
     * 加减：日：并返回该日是本月的哪一天字符串：格式：23、03
     *
     * @param offset
     * @return
     */
    public static String getOffsetDay(int offset) {

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        c.add(Calendar.DAY_OF_MONTH, offset);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dayStr = day < 10 ? ("0" + String.valueOf(day)) : String
                .valueOf(day);

        return dayStr;
    }

    /**
     * 返回该日是本月的哪一天字符串：格式：23、3
     *
     * @param dayStr 入参数格式：20160710
     * @return
     */
    public static String getDay(String dayStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");// 格式化
        Date date = null;
        try {
            date = format.parse(dayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dayString = String.valueOf(day);
        return dayString;
    }

    /**
     * 加减：年：并返回{到年的}日期字符串：格式：2016
     *
     * @param offset
     * @return
     */
    public static String getOffsetYear(int offset) {

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        c.add(Calendar.YEAR, offset);
        int year = c.get(Calendar.YEAR);

        return String.valueOf(year);
    }

    /**
     * 加减：月：并返回{到月的}日期字符串：格式：201607
     *
     * @param offset
     * @return
     */
    public static String getOffsetYearMonth(int offset) {

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        c.add(Calendar.MONTH, offset);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        String monthStr = month < 10 ? ("0" + String.valueOf(month)) : String
                .valueOf(month);

        return String.valueOf(year) + monthStr;
    }

    /**
     * 加减：月：并返回{到天的}日期字符串：格式：20160701
     *
     * @param offset
     * @return
     */
    public static String getOffsetYearMonthDay(int offset) {

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        c.add(Calendar.MONTH, offset);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String monthStr = month < 10 ? ("0" + String.valueOf(month)) : String
                .valueOf(month);
        String dayStr = day < 10 ? ("0" + String.valueOf(day)) : String
                .valueOf(day);

        return String.valueOf(year) + monthStr + dayStr;
    }

    /**
     * 加减天，对当前日期的偏移量，并返回到天的日期字符串：格式20160701
     *
     * @param offset
     * @return
     */
    public static String getOffsetCurrentDay(int offset) {

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        c.add(Calendar.DAY_OF_MONTH, offset);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String monthStr = month < 10 ? ("0" + String.valueOf(month)) : String
                .valueOf(month);
        String dayStr = day < 10 ? ("0" + String.valueOf(day)) : String
                .valueOf(day);

        return String.valueOf(year) + monthStr + dayStr;
    }

    /**
     * 获取T-1日期的自然周格式：{20160717=星期日, 20160711=星期一, 20160712=星期二, 20160715=星期五,
     * 20160716=星期六, 20160713=星期三, 20160714=星期四}
     *
     * @return
     */
    public static Map<String, String> getWeekdays() {
        Map<String, String> weekMap = new HashMap<String, String>();
        Calendar calendar = Calendar.getInstance();

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        for (int i = 0; i < 7; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd EE");
            String dateStr = dateFormat.format(calendar.getTime());
            String[] arrays = dateStr.split(" ");
            weekMap.put(arrays[0], arrays[1]);
            calendar.add(Calendar.DATE, 1);
        }

        return weekMap;
    }

    /**
     * 根据日期返回对应的星期名称，如输入20160711返回周一
     *
     * @param dateStr
     * @return
     */
    public static String getWeekNameByDate(String dateStr) {
        int year = Integer.valueOf(dateStr.substring(0, 4));
        int month = Integer.valueOf(dateStr.substring(4, 6));
        int day = Integer.valueOf(dateStr.substring(6, 8));
        Calendar calendar = Calendar.getInstance();// 获得一个日历
        calendar.set(year, month - 1, day);// 设置当前时间,月份是从0月开始计算
        int number = calendar.get(Calendar.DAY_OF_WEEK);// 星期表示1-7，是从星期日开始，
        String[] str = {"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return str[number];
    }

    /**
     * 得到当前日期是第几周描述。如20160712是2016年7月第三周。
     *
     * @return
     */
    public static String getCurrentWeekDesc() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        String[] arrays = {"", "一", "二", "三", "四", "五"};
        return year + "年" + month + "月第" + arrays[weekOfMonth] + "周";
    }

    /**
     * 得到当前日期是第几周描述。如20160712是2016年7月第三周。 如果是周一返回上周
     *
     * @return
     */
    public static String getNeedWeekDesc() {
        Calendar calendar = Calendar.getInstance();
        if (isFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -7);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        String[] arrays = {"", "一", "二", "三", "四", "五"};
        return year + "年" + month + "月第" + arrays[weekOfMonth] + "周";
    }

    /**
     * 得到当前日期是第几周数值。如20160712是2016073。
     *
     * @return
     */
    public static String getCurrentWeekValue() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        String monthStr = month < 10 ? ("0" + String.valueOf(month)) : String
                .valueOf(month);

        return year + monthStr + weekOfMonth;
    }

    /**
     * @param offsetWeek
     * @return
     */
    public static String getOffsetYearMonthWeek(int offsetWeek) {

        Calendar calendar = Calendar.getInstance();
        // n为推迟的周数，0为本周， 1下周，-1向前推迟一周，2下周，依次类推
        calendar.add(Calendar.DATE, offsetWeek * 7);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        String monthStr = month < 10 ? ("0" + String.valueOf(month)) : String
                .valueOf(month);

        return year + monthStr + weekOfMonth;
    }

    /**
     * 得到当前年份的第一个月至当前月的描述。如 “2016年1月-7月”
     *
     * @return
     */
    public static String getCurrentMonthPeriodDesc() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "年1月-" + month + "月";
    }

    /**
     * 得到当前日期是第几月描述。如20160712是2016年7月。
     *
     * @return
     */
    public static String getCurrentMonthDesc() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "年" + month + "月";
    }

    /**
     * 得到当前日期是第几月描述。如20160712是2016年7月。
     * <p>
     * 如果今天是1号，则返回上月
     *
     * @return
     */
    public static String getNeedMonthDesc() {
        Calendar calendar = Calendar.getInstance();
        if (isFirstDayOfMonth()) {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "年" + month + "月";
    }

    // =====================================================================

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 周一为本周第一天
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 周一为本周第一天
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfWeek(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    /**
     * 返回当前时间{yyyyMMdd:HHmmss}格式
     *
     * @return
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd:HHmmss");// 格式化
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

    /**
     * 返回当前月的第一天{yyyyMMdd}格式
     *
     * @return
     */
    public static String getFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");// 格式化
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(calendar.getTime());
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getFirstDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1 - 1;
        } else if (quarter == 2) {
            month = 4 - 1;
        } else if (quarter == 3) {
            month = 7 - 1;
        } else if (quarter == 4) {
            month = 10 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季的最后一天
     *
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的上一季的最后一天
     *
     * @return
     */
    public static Date getLastDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 获取当前日期的前几天或者后几天日期
     *
     * @param day 天数 负数代表前几天，正数代表后几天
     * @return
     */
    public static String getDateByDay(int day) {
        // 获取当前日期
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        String strDate = new SimpleDateFormat("yyyyMMdd")
                .format(date.getTime());
        return strDate;
    }

    /**
     * 获取当前日期的前几天或者后几天日期
     *
     * @param day 天数 负数代表前几天，正数代表后几天
     * @return
     */
    public static Date getDateBy(int day) {
        // 获取当前日期
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        return date.getTime();
    }

    /**
     * 返回日期{yyyyMMdd}格式
     *
     * @return
     */
    public static String toYyyyMMdd(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");// 格式化
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return format.format(calendar.getTime());
    }

    /**
     * 返回日期{yyyy-MM-dd}格式
     *
     * @return
     */
    public static String toYyyy2MM2dd(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 格式化
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return format.format(calendar.getTime());
    }

    /**
     * 返回当前时间{yyyyMMdd:HHmmss}格式
     *
     * @return
     */
    public static String toYyyy2MM2ddHHmmss(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return format.format(calendar.getTime());
    }

    /**
     * 返回日期{yyyyMM}格式
     *
     * @param date
     * @return
     */
    public static String toYyyyMM(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");// 格式化
        String monthStr = format.format(date);
        return monthStr;
    }

    /**
     * 判断当天是否为本月第一天
     *
     * @return
     */
    public static boolean isFirstDayOfMonth() {
        boolean flag = false;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        if (1 == today) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断当天是否为本月第一天
     *
     * @return
     */
    public static boolean isFirstDayOfWeek() {
        boolean flag = false;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        if (2 == today) {// 中国是以星期一为每周的开始
            flag = true;
        }
        return flag;
    }

    /**
     * 计算两个日期之间相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getDateSpace(String startDate, String endDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(startDate));
        c2.setTime(sdf.parse(endDate));

        int result = 0;
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1 != year2) // 同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
                {
                    timeDistance += 366;
                } else // 不是闰年
                {
                    timeDistance += 365;
                }
            }
            result = timeDistance + (day2 - day1);
        } else // 不同年
        {
            // System.out.println("判断day2 - day1 : " + (day2-day1));
            result = day2 - day1;
        }
        return Math.abs(result);

    }

    /**
     * 计算两个日期之间相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getDateSpace(Date startDate, Date endDate)
            throws ParseException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);

        int result = 0;
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1 != year2) // 同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
                {
                    timeDistance += 366;
                } else // 不是闰年
                {
                    timeDistance += 365;
                }
            }
            result = timeDistance + (day2 - day1);
        } else // 不同年
        {
            result = day2 - day1;
        }
        return Math.abs(result);
    }

    /**
     * 计算两个日期相差的月数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String startDate, String endDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(startDate));
        c2.setTime(sdf.parse(endDate));
        int result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        int month = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    ///**
    // * 计算两个日期相差的月数{每个月按照30天算，就是差了几个30天}
    // *
    // * @param startDate
    // * @param endDate
    // * @return
    // * @throws ParseException
    // */
    //public static float getMonth30Space(String startDate, String endDate)
    //		throws ParseException {
    //	return CalendarUtils.getDateSpace(startDate,endDate)/30;
    //}

    // =====================================================================

    /**
     * 得到一段日期内所有的日期集合｛不包含结束日期当天｝
     *
     * @param startStr :{yyyy-MM-dd}格式
     * @param endStr   :{yyyy-MM-dd}格式
     * @return
     * @throws ParseException
     */
    public static List<String> getAllDatesBetween(String startStr, String endStr)
            throws ParseException {
        List<String> dates = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startStr);
        Date endDate = sdf.parse(endStr);

        Calendar calStart = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);

        // 开始日期先加入，可以避免开始日期等于结束日期这种情形
//		dates.add(sdf.format(calStart.getTime()));

        // 测试此日期是否在指定日期之后
        while (calEnd.getTime().after(calStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            dates.add(sdf.format(calStart.getTime()));
            calStart.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }

    // /**
    // * 获取中文周列表：map{周几在英文中排序，中文名称}
    // *
    // * 用于把sql查询出来的；英文周顺序转换成中文顺序，并显示成中文周
    // *
    // * @param dateStr
    // * @return
    // */
    // public static List<Map<String, String>> getWeekNameList() {
    // // String[] str = { "1"："周一", "2"："周二", "3"："周三", "4"："周四", "5"："周五",
    // // "6"："周六", "0"："周日" };
    // List<Map<String, String>> weekList = new ArrayList<Map<String,
    // String>>();
    // Map<String, String> week1 = new HashMap<String, String>();
    // week1.put("1", "周一");
    // weekList.add(week1);
    // Map<String, String> week2 = new HashMap<String, String>();
    // week2.put("2", "周二");
    // weekList.add(week2);
    // Map<String, String> week3 = new HashMap<String, String>();
    // week3.put("3", "周三");
    // weekList.add(week3);
    // Map<String, String> week4 = new HashMap<String, String>();
    // week4.put("4", "周四");
    // weekList.add(week4);
    // Map<String, String> week5 = new HashMap<String, String>();
    // week5.put("5", "周五");
    // weekList.add(week5);
    // Map<String, String> week6 = new HashMap<String, String>();
    // week6.put("6", "周六");
    // weekList.add(week6);
    // Map<String, String> week7 = new HashMap<String, String>();
    // week7.put("0", "周日");
    // weekList.add(week7);
    // return weekList;
    // }

    /**
     * 获取中文周列表：map{周几在数据库中排序，中文名称}
     * <p>
     * 用于把sql查询出来的；数据库周顺序转换成中文顺序，并显示成中文周
     * <p>
     * 列表是按照周一到周日排，但是内部的周几序号是按照数据库中的周日0排的
     *
     * @return
     */
    public static List<Week> getWeekNameList() {
        // String[] str = { "1"："周一", "2"："周二", "3"："周三", "4"："周四", "5"："周五",
        // "6"："周六", "0"："周日" };
        List<Week> weekList = new ArrayList<Week>();
        weekList.add(new Week(1, 1, "周一"));
        weekList.add(new Week(2, 2, "周二"));
        weekList.add(new Week(3, 3, "周三"));
        weekList.add(new Week(4, 4, "周四"));
        weekList.add(new Week(5, 5, "周五"));
        weekList.add(new Week(6, 6, "周六"));
        weekList.add(new Week(7, 0, "周日"));// list索引6
        return weekList;
    }

    /**
     * 得到一段日期内所有的{周一到周日}出现的次数集合：
     * <p>
     * {周一到周日，对应list中的顺序;Week.en_index周日是从0开始；对应数据库中的week顺序}
     *
     * @param startStr :{yyyy-MM-dd}格式
     * @param endStr   :{yyyy-MM-dd}格式
     * @return
     * @throws ParseException
     */
    public static List<Week> getWeekCountBetween(String startStr, String endStr)
            throws ParseException {
        List<Week> weekList = getWeekNameList();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startStr);
        Date endDate = sdf.parse(endStr);

        Calendar calStart = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);
        // 测试此日期是否在指定日期之后
        while (calEnd.getTime().after(calStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calStart.add(Calendar.DAY_OF_MONTH, 1);
            int weekIndex = calStart.get(Calendar.DAY_OF_WEEK) - 2;// DAY_OF_WEEK：从Sunday1-SATURDAY7，对应数据库中的要减1
            if (weekIndex == -1) {// 周日：对应weekList中的index 6
                weekList.get(6).setCount(weekList.get(6).getCount() + 1);
            } else {
                weekList.get(weekIndex).setCount(
                        weekList.get(weekIndex).getCount() + 1);// 给周几出现的次数加1
            }
        }
        return weekList;
    }

    // public static void main(String[] args) {
    // SimpleDateFormat dateFormat = new SimpleDateFormat("EE");
    // Calendar calendar = Calendar.getInstance();
    // calendar.setFirstDayOfWeek(Calendar.SUNDAY);
    // String dateStr = dateFormat.format(calendar.getTime());
    // // calendar.get(Calendar.DAY_OF_WEEK);
    // System.out.println(dateStr);
    //
    // System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
    //
    // }

    /**
     * 获取第二天：零点的时间｛一天开始的时间｝
     */
    public static Date nextZeroTime(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * 计算2个时间差多少秒
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int secondSpace(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    /**
     * 得到一段日期内所有的日期集合
     *
     * @param startStr :{yyyyMM}格式
     * @param endStr   :{yyyyMM}格式
     * @return
     * @throws ParseException
     */
    public static List<String> getAllDatesByPeriod(String startStr,
                                                   String endStr) throws ParseException {
        List<String> dates = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startDate = sdf.parse(startStr);
        Date endDate = sdf.parse(endStr);

        Calendar calStart = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);

        // 开始日期先加入，可以避免开始日期等于结束日期这种情形
        dates.add(sdf.format(calStart.getTime()));

        // 测试此日期是否在指定日期之后
        while (calEnd.getTime().after(calStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calStart.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(sdf.format(calStart.getTime()));
        }

        return dates;
    }

    /**
     * 24小时：转换成上午下午12时制：如：6a、12a、6p
     *
     * @param hour %k 小时(0……23)
     * @return
     */
    public static String hourToAPm(String hour) {
        int hourNum = Integer.parseInt(hour);
        if (hourNum > 12) {// 转换成上午下午12时制：如：6a、12a、6p
            hour = (hourNum - 12) + "p";// 下午
        } else {
            hour = hour + "a";//
        }
        return hour;
    }

    public static List<String> getAllDatesByPeriod10(String startStr,
                                                     String endStr) throws ParseException {
        List<String> dates = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startDate = sdf.parse(startStr);
        Date endDate = sdf.parse(endStr);

        Calendar calStart = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);

        // 开始日期先加入，可以避免开始日期等于结束日期这种情形
        dates.add(sdf.format(calStart.getTime()));

        // 测试此日期是否在指定日期之后
        while (calEnd.getTime().after(calStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calStart.add(Calendar.DAY_OF_MONTH, 10);
            dates.add(sdf.format(calStart.getTime()));
        }

        return dates;
    }

    /**
     * 获取指定时间后面一天的时间字符串
     *
     * @param dateStr yyyyMMdd格式时间字符串
     * @return
     */
    public static String nextDateStr(String dateStr) {
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        cal.add(Calendar.DATE, +1);
        return CalendarUtils.toYyyyMMdd(cal.getTime());
    }

    public static String addMinute(String times, int minutes) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        times = "2017-11-01 " + times;
        DateTime dateTime = DateTime.parse(times, format);
        dateTime = dateTime.plusMinutes(minutes);
        return StringUtils.substring(dateTime.toString("yyyy-MM-dd HH:mm:ss"), 11);
    }

    // /**
    // * 检查具体日期是否为节假日，工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2；
    // *
    // http://apistore.baidu.com/apiworks/servicedetail/1116.html?qq-pf-to=pcqq.discussion
    // * API集市 > 服务类API > 节假日
    // *
    // * @param httpArg
    // * :参数
    // * @return 返回结果
    // */
    // public static String request(String httpArg) {
    // String httpUrl = Constants.BAIDU_HOLIDAY_HTTPURL;
    // BufferedReader reader = null;
    // String result = null;
    // StringBuffer sbf = new StringBuffer();
    // httpUrl = httpUrl + "?" + httpArg;
    //
    // try {
    // URL url = new URL(httpUrl);
    // HttpURLConnection connection = (HttpURLConnection) url
    // .openConnection();
    // connection.setRequestMethod("GET");
    // // 填入apikey到HTTP header
    // connection.setRequestProperty("apikey", Constants.BAIDU_HOLIDAY_APIKEY);
    // connection.connect();
    // InputStream is = connection.getInputStream();
    // reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    // String strRead = null;
    // while ((strRead = reader.readLine()) != null) {
    // sbf.append(strRead);
    // sbf.append("\r\n");
    // }
    // reader.close();
    // result = sbf.toString();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return result;
    // }

    /*
     * public static void main(String[] args) { String httpArg = "d=20161103";
     * String jsonResult = request(httpArg); System.out.println(jsonResult); }
     */

    /**
     * 列举从传入年份到现在的所有年份
     *
     * @return
     */
    public static String[] getFilterYear(int startYear) {
        Calendar date = Calendar.getInstance();
        int nowYear = Integer.parseInt(String.valueOf(date.get(Calendar.YEAR)));
        if (startYear >= nowYear || startYear < 2000) {
            return null;
        }
        List<String> yearFilterList = new ArrayList<>();
        while (nowYear - startYear >= 0) {
            yearFilterList.add((startYear) + "");
            startYear = startYear + 1;
        }
        String[] array = new String[yearFilterList.size()];
        return yearFilterList.toArray(array);
    }
}
