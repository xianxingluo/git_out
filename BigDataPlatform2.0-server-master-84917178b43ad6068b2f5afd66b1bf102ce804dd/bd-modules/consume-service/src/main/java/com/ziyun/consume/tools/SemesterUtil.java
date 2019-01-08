package com.ziyun.consume.tools;

import com.ziyun.consume.vo.Params;
import com.ziyun.utils.date.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: yk.tan
 * @since: 2017/6/2
 * @history:
 */
public class SemesterUtil {

    private static final String FROM_DATE = "03-01";
    private static final String TO_DATE = "08-01";

    public static void computeSemesterFromDate(Params params) {
        params.setSemester(computeSemesterFromDate(new Date(), Integer.parseInt(params.getSemester())));
    }

    /**
     * @param date 当前时间
     * @param pos  本学期0，上学期 -1
     * @return
     */
    public static String computeSemesterFromDate(Date date, int pos) {
        //判断是否在3.1--7.30号
        String result;
        String year = CalendarUtils.getOffsetYear(0);
        String fromStr = year + "-" + FROM_DATE;
        String toStr = year + "-" + TO_DATE;
        Date from = DateUtils.parse(fromStr, DateUtils.DATE_YY_MM_DD_ME);
        Date to = DateUtils.parse(toStr, DateUtils.DATE_YY_MM_DD_ME);
        int curSemester = 0;
        if (belongCalendar(date, from, to)) {
            if (pos == 0) {
                curSemester = 2;
            } else {
                curSemester = 1;
            }
            result = CalendarUtils.getOffsetYear(-1) + "-" + year + "-" + curSemester;
        } else {
            if (pos == 0) {
                curSemester = 1;
                result = year + "-" + CalendarUtils.getOffsetYear(1) + "-" + curSemester;
            } else {
                curSemester = 2;
                result = CalendarUtils.getOffsetYear(0) + "-" + year + "-" + curSemester;
            }

        }
        return result;
    }

    public static boolean belongCalendar(Date time, Date from, Date to) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);

        Calendar after = Calendar.getInstance();
        after.setTime(from);

        Calendar before = Calendar.getInstance();
        before.setTime(to);

        if (date.after(after) && date.before(before)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Date cur = new Date();
        String str = SemesterUtil.computeSemesterFromDate(cur, 0);
        System.out.println(str);
    }
}
