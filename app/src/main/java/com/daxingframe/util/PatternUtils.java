package com.daxingframe.util;

import android.webkit.URLUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class PatternUtils
{
    /**
     * 手机号码
     */
    public static final String REGEX_PHONENUMBER = "^1[3|4|5|6|7|8|9][0-9]{9}$";
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

    public static boolean isPhoneNumber(CharSequence phoneNumber)
    {
        Pattern pattern = Pattern.compile(PatternUtils.REGEX_PHONENUMBER);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static final boolean isEMail(CharSequence email)
    {
        Pattern pattern = Pattern.compile(PatternUtils.REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static final boolean isHTTP(String http)
    {
        return URLUtil.isHttpsUrl(http) || URLUtil.isHttpUrl(http);
    }
}
