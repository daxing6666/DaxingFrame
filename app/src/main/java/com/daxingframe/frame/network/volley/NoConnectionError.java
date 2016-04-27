package com.daxingframe.frame.network.volley;

/**
 * 继承自 NetworkError，代表无法建立连接错误。
 */
@SuppressWarnings("serial")
public class NoConnectionError extends NetworkError {
    public NoConnectionError() {
        super();
    }

    public NoConnectionError(Throwable reason) {
        super(reason);
    }
}
