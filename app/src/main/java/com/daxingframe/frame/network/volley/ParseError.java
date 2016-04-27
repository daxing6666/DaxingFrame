package com.daxingframe.frame.network.volley;

/**
 * 继承自 VolleyError，代表内容解析错误
 */
@SuppressWarnings("serial")
public class ParseError extends VolleyError {
    public ParseError() { }

    public ParseError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public ParseError(Throwable cause) {
        super(cause);
    }
}
