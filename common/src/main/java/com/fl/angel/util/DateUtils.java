package com.fl.angel.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.fl.angel.constants.DateFormatConstants;

import java.util.Date;

/**
 * @Author fdm
 * @Date 2020/11/11 11:24
 * @description：  时间处理工具类
 */
public class DateUtils {

    private DateUtils(){}

    /**
     * 获取当前时间
     * @return
     */
    public static Date getCurrentDateTime(){
        return  DateUtil.date();
    }

    /**
     * 获取当前时间字符串，格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateTimeStr(){
        return DateUtil.now();
    }

    /**
     * 获取当前日期字符串，格式为yyyy-MM-dd
     * @return
     */
    public static String getCurrentDateStr(){
        return DateUtil.today();
    }

    /**
     * 时间字符串转时间对象
     * @param dateStr 时间字符串
     * @param format  时间格式
     * @return
     */
    public static Date parse(String dateStr,String format){
        return DateUtil.parse(dateStr, format);
    }


    /**
     * 不带格式的日期时间解析，使用默认格式：yyyy-MM-dd HH:mm:ss
     * @param dateStr
     * @return
     */
    public static Date parse(String dateStr){
        return DateUtils.parse(dateStr, DateFormatConstants.FORMAT3);
    }

    /**
     * 时间对象转时间字符串
     * @param date
     * @param format
     * @return
     */
    public static String  format(Date date,String format){
        return DateUtil.format(date, format);
    }

    /**
     *时间格式化为日期字符串，转换默认格式yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatDate(Date date){
       return DateUtils.format(date,DateFormatConstants.FORMAT1);
    }

    /**
     *时间格式化为时间字符串，转换默认格式HH:mm:ss
     * @param date
     * @return
     */
    public static String formatTime(Date date){
        return DateUtils.format(date,DateFormatConstants.FORMAT2);
    }

    /**
     *时间格式化为日期时间字符串，转换默认格式yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        return DateUtils.format(date,DateFormatConstants.FORMAT3);
    }

    /**
     * 获取传入时间的月份，
     * @param date
     * @return
     */
    public static int getDatemonth(Date date){
        return DateUtil.month(date)+1;
    }

    /**
     * 获取传入时间当天的开始时间，如：2020-03-01 00:00:00
     * @param date
     * @return
     */
    public static DateTime getDayBegin(Date date){
        return  DateUtil.beginOfDay(date);
    }

    /**
     * 获取传入时间当天的结束时间，2020-03-01 23:59:59
     * @param date
     * @return
     */
    public static DateTime getDayEnd(Date date){
        return DateUtil.endOfDay(date);
    }

    /**
     * 获取传入时间所在月份的第一天的00：00：00，如：2020-11-01 00:00:00
     * @param date
     * @return
     */
    public static DateTime beginOfMonth(Date date){
        return DateUtil.endOfMonth(date);
    }

    /**
     * 获取传入时间所在月份的最后一天的23：59：59，如：2020-11-30 23:59:59
     * @param date
     * @return
     */
    public static DateTime endOfMonth(Date date){
        return DateUtil.endOfMonth(date);
    }

    /**
     * 获取两个日期之间相差多少天
     * @param date1
     * @param date2
     * @return
     */
    public static long dateBetween(Date date1,Date date2){
        return DateUtil.between(date1, date2, DateUnit.DAY);
    }

}
