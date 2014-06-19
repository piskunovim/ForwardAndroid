/*
 *  Утилита для работы с датами
 */
package ru.forwardmobile.util.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Vanin V. ООО "Ракета" 2011
 */
public class Dates {
    
    public static final String SQL_DATE_FORMAT     = "yyyy-MM-dd";
    public static final String SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SQL_TIME_FORMAT = "HH:mm:ss";
    
    public static final String COMMAND_DATE_FORMAT = "yyyyMMdd";
    public static final String COMMAND_TIME_FORMAT = "HHmmss";
    
    public static Date fromSqlDate(String date) throws ParseException
    {
        if(date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_DATE_FORMAT);
        return  dateFormat.parse(date);
    }
    
    public static Date fromSqlDatetime(String date) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_DATETIME_FORMAT);
        return  dateFormat.parse(date);
    }
    
    public static Date fromSqlTime(String date) throws ParseException
    {
        if(date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
        return  dateFormat.parse(date);
    }
    
    public static long nullSafeGetTimestamp(Date date)
    {
        if(date == null)
        {
            return 0;
        }
        
        return date.getTime();
    }
    
    public static String Format(Date date, String FORMAT)
    {
        if(date == null) {
            return null;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
        return dateFormat.format(date);
    }
    
    public static long Now()
    {
        Date date = new Date();
        return date.getTime();
    }
    
    public static String Now(String FORMAT)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
        return dateFormat.format(date);
    }
    
    public static String Curdate()
    {
        return Dates.Now(Dates.SQL_DATE_FORMAT);
    }
    
    public static String Curtime()
    {
        return Dates.Now(Dates.SQL_TIME_FORMAT);
    }

    public static Date fromCommandDate(String date) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(COMMAND_DATE_FORMAT);
        return  dateFormat.parse(date);
    }
    
    public static Date fromCommandTime(String date) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(COMMAND_TIME_FORMAT);
        return  dateFormat.parse(date);
    }
    
    public static Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(date);
    }
}
