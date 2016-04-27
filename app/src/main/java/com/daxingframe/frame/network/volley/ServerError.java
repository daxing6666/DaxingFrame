package com.daxingframe.frame.network.volley;

/**
 * 继承自 VolleyError，代表服务端错误
 */
@SuppressWarnings("serial")
public class ServerError extends VolleyError {
    public ServerError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public ServerError() {
        super();
    }
}

