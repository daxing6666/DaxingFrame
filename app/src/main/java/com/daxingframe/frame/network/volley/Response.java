package com.daxingframe.frame.network.volley;


/**
 * 封装了经过解析后的数据，用于传输。并且有两个内部接口 Listener 和 ErrorListener 分别可表示请求失败和成功后的回调。
 */
public class Response<T> {

    // =============================================================================
    // Private members
    // =============================================================================
    /** Parsed response, or null in the case of error. */
    public final T result;

    /** Cache metadata for this response, or null in the case of error. */
    public final Cache.Entry cacheEntry;

    /** Detailed error information if <code>errorCode != OK</code>. */
    public final VolleyError error;

    /** True if this response was a soft-expired one and a second one MAY be coming. */
    public boolean intermediate = false;

    // =============================================================================
    // Constructors
    // =============================================================================
    private Response(T result, Cache.Entry cacheEntry) {
        this.result = result;
        this.cacheEntry = cacheEntry;
        this.error = null;
    }

    private Response(VolleyError error) {
        this.result = null;
        this.cacheEntry = null;
        this.error = error;
    }

    // =============================================================================
    // Static Methods
    // =============================================================================
    /** Returns a successful response containing the parsed result. */
    public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
        return new Response<T>(result, cacheEntry);
    }

    /**
     * Returns a failed response containing the given error code and an optional
     * localized message displayed to the user.
     */
    public static <T> Response<T> error(VolleyError error) {
        return new Response<T>(error);
    }

    // =============================================================================
    // Getter / Setter
    // =============================================================================
    /**
     * Returns whether this response is considered successful.
     */
    public boolean isSuccess() {
        return error == null;
    }

    // =============================================================================
    // Interface
    // =============================================================================
    /** Callback interface for delivering parsed responses. */
    public interface Listener<T> {
        /** Called when a response is received. */
        public void onResponse(T response);
    }

    /** Callback interface for delivering error responses. */
    public interface ErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onErrorResponse(VolleyError error);
    }
}
