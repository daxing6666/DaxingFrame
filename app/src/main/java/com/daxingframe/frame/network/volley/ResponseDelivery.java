package com.daxingframe.frame.network.volley;

/**
 * 请求结果的传输接口，用于传递请求结果或者请求错误。
 */
public interface ResponseDelivery {
    /**
     * 此方法用于传递请求结果，request 和 response 参数分别表示请求信息和返回结果信息。
     */
    public void postResponse(Request<?> request, Response<?> response);

    /**
     * 此方法用于传递请求结果，并在完成传递后执行 Runnable。
     */
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable);

    /**
     * 此方法用于传输请求错误。
     */
    public void postError(Request<?> request, VolleyError error);
}
