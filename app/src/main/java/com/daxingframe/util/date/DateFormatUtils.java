package com.daxingframe.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class DateFormatUtils
{
    public static String format(String time)
    {
        return format(time, false);
    }

    public static String format(String time, boolean withYMD)
    {
        return format(time, withYMD, true);
    }

    /**
     * 格式化时间
     *
     * @param time    ：long字符串类型的时间
     * @param withYMD
     * @param withHm
     * @return
     */
    public static String format(String time, boolean withYMD, boolean withHm)
    {
        Date date = null;
        try
        {
            date = new Date(Long.parseLong(time));
        }
        catch (Exception e)
        {
            System.out.println("格式化的时间不是long类型的  time = " + time);
            date = new Date(System.currentTimeMillis());
        }
        String pattern = "MM-dd HH:mm";
        if (withYMD)
        {
            pattern = "yyyy-MM-dd";
            if (withHm)
            {
                pattern = "yyyy-MM-dd HH:mm";
            }
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String timeToLongString(String time)
    {
        String result = System.currentTimeMillis() + "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date date = sdf.parse(time);
            result = date.getTime() + "";
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
