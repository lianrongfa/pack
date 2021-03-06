package cn.lianrf.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * @version: v1.0
 * @date: 2019/4/12
 * @author: lianrf
 */
public abstract class DateUtils {

    /**
     * 获取两个月份之间的月份集合
     * @param minDate 开始月份 201901
     * @param maxDate 结束月份 201903
     * @return
     * @throws ParseException
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * 获得一个月中的所有周六周天
     * @param month long形时间戳
     * @return
     */
    public static List<Integer> getWeekendInMonth(Long month) {
        List list = new ArrayList<Integer>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(month);
        // 当月最大天数
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daySize; i++) {
            //在第一天的基础上加1
            calendar.add(Calendar.DATE, 1);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            // 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
            if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
                // 得到当天是一个月的第几天
                list.add(calendar.get(Calendar.DAY_OF_MONTH));
            }
        }
        return list;
    }

    /**
     * 获取两个日期字符串之间的日期集合
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param calendar calendar
     * @return
     */
    public static List<String> getBetweenDate(Long startTime, Long endTime, Calendar calendar) {
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        long index = startTime;
        calendar.setTimeInMillis(index);
        //用Calendar 进行日期比较判断
        while (index <= endTime) {
            // 把日期添加到集合
            list.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            //把日期增加一天
            calendar.add(Calendar.DATE, 1);
            // 获取增加后的日期
            index = calendar.getTimeInMillis();
        }
        return list;
    }



    /**
     * 获取当前系统时间最近12月的年月（含当月）
     * 2018-04~2019-03
     */
    public static List<String> getLatest12Month(){
        YearMonth month = YearMonth.now();
        ArrayList<String> monthsStr = new ArrayList<>();
        monthsStr.add(month.toString());
        for (int i = 0; i <11 ; i++) {
            month= month.minus(Period.ofMonths(1));
            monthsStr.add(month.toString());
        }
        return monthsStr;
    }

}
