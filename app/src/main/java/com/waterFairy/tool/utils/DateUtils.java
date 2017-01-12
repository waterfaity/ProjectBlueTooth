package com.waterfairy.tool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by water_fairy on 2016/12/2.
 */

public class DateUtils {
    private static String TAG = "timeUtils >>";

    public static String trans(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        return simpleDateFormat.format(date);


    }

    public static String trans(String timeStr) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));//时区
//        calendar.getTime().getTime();
//        Fri Jan 17 11:14:45 CST 2016
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss CST yyyy");
        String content = "";
        try {
            Date parse = dateFormat.parse("Fri Jan 17 11:14:45 CST 2014");//解析
            return trans(parse.getTime());
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return "";
    }

    /**
     * @param currentDate 当前时间
     * @param transDate   需要转化的时间(默认转化为 前天,昨天,今天,明天,后天)
     * @param detail      是否更详细(现在,5分钟前,半小时前,n个小时前)
     * @return
     */
    public String transWhichDate(Date currentDate, Date transDate, boolean detail) {

        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonth();
        int currentDay = currentDate.getDay();
        int currentHours = currentDate.getHours();
        int currentMinutes = currentDate.getMinutes();

        return "";


    }
}
