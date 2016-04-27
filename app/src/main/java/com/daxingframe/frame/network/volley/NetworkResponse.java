package com.daxingframe.frame.network.volley;

import org.apache.http.HttpStatus;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 封装了网络请求响应的 StatusCode，Headers 和 Body 等
 */
public class NetworkResponse implements Serializable{
    // =============================================================================
    // Constants
    // =============================================================================
    private static final long serialVersionUID = -20150728102000L;

    // =============================================================================
    // Private members
    // =============================================================================
    /** Http 响应状态码 */
    public final int statusCode;

    /** Body 数据 */
    public final byte[] data;

    /** 响应 Headers */
    public final Map<String, String> headers;

    /** 表示是否为 304 响应 */
    public final boolean notModified;

    /** 请求耗时 */
    public final long networkTimeMs;

    // =============================================================================
    // Constructors
    // =============================================================================
    /**
     * Creates a new network response.
     * @param statusCode the HTTP status code
     * @param data Response body
     * @param headers Headers returned with this response, or null for none
     * @param notModified True if the server returned a 304 and the data was already in cache
     * @param networkTimeMs Round-trip network time to receive network response
     */
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                           boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }

    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                           boolean notModified) {
        this(statusCode, data, headers, notModified, 0);
    }

    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(), false, 0);
    }

    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false, 0);
    }
}



