package com.amtech.tahfizulquranonline.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shourav Paul on 15-12-2021.
 **/
public class MyTimeCalculator {

    private static final String TAG = "MyTimeCalculator";
    public boolean inTimeFrame(String date, String strtTime, String endTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String dateTimeStr1 = date+" "+strtTime;
            String dateTimeStr2 = date+" "+endTime;
            Date mDate1 = sdf.parse(dateTimeStr1);
            Date mDate2 = sdf.parse(dateTimeStr2);
            long startMilli = mDate1.getTime();
            long endMilli = mDate2.getTime();
            if(Calendar.getInstance().getTimeInMillis() >= startMilli && Calendar.getInstance().getTimeInMillis() <= endMilli)
            {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String formatTime(String unformatTime)
    {
        //Start Time
        String[] stTimeArr = unformatTime.split(" ");
        String[] s = stTimeArr[0].split(":");
        String hrFrag = s[0].length() == 1 ? "0"+s[0] : s[0];
        if(stTimeArr[1].equals("PM"))
        {
            if(!hrFrag.equals("12"))
            {
                int hrInt = Integer.parseInt(hrFrag) + 12;
                hrFrag = String.valueOf(hrInt);
            }
        }
        else
        {
            if(hrFrag.equals("12"))
            {
                hrFrag = "00";
            }
        }
        String newTimeFormat = hrFrag+":"+s[1];
        String stTimeStr = newTimeFormat+":"+"00";
        return stTimeStr;
    }
    public String getAddedDateInStr(int noOfDays)
    {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long weekInMillis = noOfDays*24*60*60*1000L;
        long afterWeekTime = timeInMillis+weekInMillis;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(afterWeekTime);
        return formatter.format(calendar.getTime());
    }
}
