package com.daxingframe.frame.network.original;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class TrustAnyX509TrustManager implements X509TrustManager
{

    private static final String TAG = "TrustAnyX509TrustManager";

    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    /**
     * (non-Javadoc)
     * @see
     * X509TrustManager checkServerTrusted(java.security.cert.
     * X509Certificate[], String)
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    /**
     * (non-Javadoc)
     * @see X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[] {};
    }

}
