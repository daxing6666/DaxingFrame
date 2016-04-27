package com.daxingframe.frame.network.volley;

/**
 * Volley 中所有错误异常的父类，继承自 Exception，可通过此类设置和获取 NetworkResponse 或者请求的耗时。
 * 知识点:
 * 在java语言中，错误类的基类是java.lang.Error，异常类的基类是java.lang.Exception。
   相同点：java.lang.Error和java.lang.Exception都是java.lang.Throwable的子类
 */

@SuppressWarnings("serial")
public class VolleyError extends Exception {

    // =============================================================================
    // Private members
    // =============================================================================
    public final NetworkResponse networkResponse;
    private long networkTimeMs;

    // =============================================================================
    // Constructors
    // =============================================================================
    public VolleyError() {
        networkResponse = null;
    }

    public VolleyError(NetworkResponse response) {
        networkResponse = response;
    }

    public VolleyError(String exceptionMessage) {
       super(exceptionMessage);
       networkResponse = null;
    }

    public VolleyError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
    }

    public VolleyError(Throwable cause) {
        super(cause);
        networkResponse = null;
    }

    // =============================================================================
    // Getter / Setter
    // =============================================================================
    /* package */
    void setNetworkTimeMs(long networkTimeMs) {
       this.networkTimeMs = networkTimeMs;
    }

    public long getNetworkTimeMs() {
       return networkTimeMs;
    }
}
