package com.daxingframe.util;

import android.text.TextUtils;

/**
 */
public class SplitUtils
{
    public static String[] split(String text)
    {
        String[] strings = null;
        if(!TextUtils.isEmpty(text))
        {
            strings = text.split(Constants.SPLIT);
        }
        return  strings;
    }
}
