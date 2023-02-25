package com.amtech.tahfizulquranonline.utils;

/**
 * Created by Shourav Paul on 10-02-2022.
 **/
public class Helper {

    public static String checkIfNull(String str)
    {
        String val = (str == null) ? "No Data Found" : str;
        return val;
    }
}
