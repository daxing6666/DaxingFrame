package com.daxingframe.frame.network.original;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 默认的SSL验证器
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier
{

    private static final String TAG = "TrustAnyHostnameVerifier";

    /**
     * 验证主机和会话
     * @see HostnameVerifier#verify(String,
     * SSLSession)、
     * @param hostname 主机名
     * @param session ssl会话
     * @return 是否验证通过，默认都是通过
     */
    public boolean verify(String hostname, SSLSession session)
    {
        return true;
    }

}
