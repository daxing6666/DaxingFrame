package com.daxingframe.frame.network.volley.toolbox;

import com.daxingframe.frame.network.volley.AuthFailureError;
import com.daxingframe.frame.network.volley.Request;

import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpResponse;

/**
 * An HTTP stack abstraction.
 * 用于处理 Http 请求，返回请求结果的接口。
 * 目前 Volley 中的实现有基于 HttpURLConnection 的 HurlStack 和 基于 Apache HttpClient 的 HttpClientStack
 */
public interface HttpStack {
    /**
     * Performs an HTTP request with the given parameters.
     *
     * <p>A GET request is sent if request.getPostBody() == null. A POST request is sent otherwise,
     * and the Content-Type header is set to request.getPostBodyContentType().</p>
     *
     * @param request the request to perform
     * @param additionalHeaders additional headers to be sent together with
     *         { Request#getHeaders()}
     * @return the HTTP response
     *
     * 执行 Request 代表的请求，第二个参数表示发起请求之前，添加额外的请求 Headers。
     *     additional  额外的
     *     perform 执行
     */
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError;

}
