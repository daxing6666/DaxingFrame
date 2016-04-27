package com.daxingframe.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import org.apache.http.protocol.HTTP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;

/**
 */
public class AndroidUtils
{

    private final static AndroidUtils instance = new AndroidUtils();

    /**
     * 单例对象实例
     */
    public static AndroidUtils getInstance(){
        return instance;
    }

    /**
     * 获取手机设备号 IMEI
     *
     * @param context
     * @return
     */
    public String getIMEI(Context context)
    {
        String deviceId = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();
        return deviceId;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public String getPhoneModel()
    {
        return android.os.Build.MODEL;

    }

    /**
     * 获取系统发布版本
     *
     * @return
     */
    public String getOSVersion()
    {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取基带版本
     *
     * @return
     */
    public String getBaseHandVersion()
    {
        String BaseHandVersion = "";
        try
        {

            Class<?> cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();

            Method m = cl.getMethod("get", new Class[]{String.class, String.class});

            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", ""});

            BaseHandVersion = (String) result;

        }
        catch (Exception e)
        {

        }
        return BaseHandVersion;
    }

    /**
     * 获取系统内核
     *
     * @return
     */
    public String getOSKernelVersion()
    {
        String oSKernelVersion = "";
        Process process = null;
        try
        {
            process = Runtime.getRuntime().exec("cat /proc/version");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);
        String result = "";
        String line;
        // get the whole standard output string
        try
        {
            while ((line = brout.readLine()) != null)
            {
                result += line;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (result != "")
        {
            String Keyword = "version ";
            int index = result.indexOf(Keyword);
            line = result.substring(index + Keyword.length());
            index = line.indexOf(" ");
            oSKernelVersion = line.substring(0, index);
        }
        return oSKernelVersion;
    }

    /**
     * 获取硬件版本信息
     *
     * @return
     */
    public String getHardWareVersion()
    {
        String result = "";
        String str = "";
        ProcessBuilder cmd;
        try
        {
            String[] args = {"system/bin/cat", "proc/cpuinfo"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1)
            {
                result += new String(re);
            }
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            int start = result.indexOf("Hardware");
            int end = result.indexOf("Revision");
            String str1 = result.substring(start, end);
            int offest = str1.indexOf(':');
            str = str1.substring(offest + 1).replaceAll("\n", "");
        }
        catch (Exception e)
        {
            return "";
        }

        return str;

    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public String getAppVersion(Context context)
    {
        String appVersion = "";
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            // 当前应用的版本名称
            appVersion = info.versionName;
            // 当前版本的版本号
            // appVersion = ""+info.versionCode;
			/*
             * // 当前版本的包名 String packageNames = info.packageName;
			 */
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return appVersion;
        }
        return appVersion;
    }

    /**
     * 获取应用程序唯一名字
     *
     * @param context
     * @return
     */
    public String getAPPName(Context context)
    {
        String appName = "";
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            appName = info.packageName + "." + info.applicationInfo.loadLabel(context.getPackageManager()).toString();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return appName;
        }
        return appName;
    }

    /**
     * 获取封装的os版本
     *
     * @return
     */
    public String getPackageOsVersion()
    {
        return "";
    }
    /**
     * md5加密
     *
     * @param sStr
     * @return
     */
    public String MD5(String sStr)
    {
        String sReturnCode = "";
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sStr.getBytes(HTTP.UTF_8));
            byte b[] = md.digest();
            int i;
            StringBuffer sb = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++)
            {
                i = b[offset];
                if (i < 0)
                {
                    i += 256;
                }
                if (i < 16)
                {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }

            sReturnCode = sb.toString();
        }
        catch (Exception ex)
        {
        }
        return sReturnCode;
    }
}
