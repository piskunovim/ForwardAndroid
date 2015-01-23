package ru.forwardmobile.tforwardpayment.settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by PiskunovI on 23.01.2015.
 */
public class TimeClass {
    public String getCurrentDateString(){
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        return formatDate.format(date);

    }

    public String getFullCurrentDateString(){
        return Calendar.getInstance().getTime().toString();
    }
}
