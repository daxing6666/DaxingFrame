package com.daxingframe.frame.network.volley;

/**
 * 重试策略接口
 */
public interface RetryPolicy {

    /**
     * 获取当前请求用时（用于 Log）
     */
    public int getCurrentTimeout();

    /**
     * 获取已经重试的次数（用于 Log）
     */
    public int getCurrentRetryCount();

    /**
     * 确定是否重试，参数为这次异常的具体信息。在请求异常时此接口会被调用，可在此函数实现中抛出传入的异常表示停止重试。
     */
    public void retry(VolleyError error) throws VolleyError;
}
