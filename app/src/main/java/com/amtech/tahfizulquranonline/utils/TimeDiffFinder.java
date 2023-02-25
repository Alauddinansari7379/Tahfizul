package com.amtech.tahfizulquranonline.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shourav Paul on 15-12-2021.
 **/
public class TimeDiffFinder {
    private static final String TAG = "TimeDiffFinder";
    public long getTimeDiff(String date, String strtTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String dateTimeStr1 = date+" "+strtTime;
            Date mDate1 = sdf.parse(dateTimeStr1);
            long startMilli = mDate1.getTime();

            if(Calendar.getInstance().getTimeInMillis() < startMilli)
            {
                return (startMilli - Calendar.getInstance().getTimeInMillis());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
