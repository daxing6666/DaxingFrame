package com.daxingframe.frame.network.volley.toolbox;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.daxingframe.frame.network.volley.AuthFailureError;
import com.daxingframe.frame.network.volley.Request;
import com.daxingframe.frame.network.volley.Request.Method;

/**
 * 实现 HttpStack 接口，利用 Apache 的 HttpClient 进行各种请求方式的请求
 */
public class HttpClientStack implements HttpStack {

    /**
     * Creates the appropriate subclass of HttpUriRequest for passed in request.
     */
    @SuppressWarnings("deprecation")
    /* protected */
    static HttpUriRequest createHttpRequest(Request<?> request,
                                            Map<String, String> additionalHeaders) throws AuthFailureError {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST: {
                /**
                 * This is the deprecated way that needs to be handled for backwards compatibility
                 * deprecated: 不赞成，反对  backwards: 向后的；倒行的；返回的；落后的 compatibility: 适合；互换性
                 * 这是一种过时的方法，需要处理的向后兼容性
                 * If the request's post body is null, then the assumption is that the request is
                 * GET.  Otherwise, it is assumed that the request is a POST(根据body是否为空,假定是空认为是GET请求
                 * 要不然就是post请求)
                 */
                /**
                 * 1 byte = 8 bit  eg:int是32bit也就是4字节
                 */
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    HttpPost postRequest = new HttpPost(request.getUrl());
                    postRequest.addHeader(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    HttpEntity entity;
                    entity = new ByteArrayEntity(postBody);
                    postRequest.setEntity(entity);
                    return postRequest;
                } else {
                    return new HttpGet(request.getUrl());
                }
            }
            case Method.GET:
                return new HttpGet(request.getUrl());
            case Method.DELETE:
                return new HttpDelete(request.getUrl());
            case Method.POST: {
                HttpPost postRequest = new HttpPost(request.getUrl());
                postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfNonEmptyBody(postRequest, request);
                return postRequest;
            }
            case Method.PUT: {
                HttpPut putRequest = new HttpPut(request.getUrl());
                putRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfNonEmptyBody(putRequest, request);
                return putRequest;
            }
            case Method.HEAD:
                return new HttpHead(request.getUrl());
            case Method.OPTIONS:
                return new HttpOptions(request.getUrl());
            case Method.TRACE:
                return new HttpTrace(request.getUrl());
            case Method.PATCH: {
                HttpPatch patchRequest = new HttpPatch(request.getUrl());
                patchRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfNonEmptyBody(patchRequest, request);
                return patchRequest;
            }
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    // =============================================================================
    // Constants
    // =============================================================================

    protected final HttpClient mClient;
    private final static String HEADER_CONTENT_TYPE = "Content-Type";

    // =============================================================================
    // Constructors
    // =============================================================================
    public HttpClientStack(HttpClient client) {
        mClient = client;
    }

    // =============================================================================
    // Supertype overrides
    // =============================================================================
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        HttpUriRequest httpRequest = createHttpRequest(request, additionalHeaders);
        addHeaders(httpRequest, additionalHeaders);
        addHeaders(httpRequest, request.getHeaders());
        onPrepareRequest(httpRequest);
        HttpParams httpParams = httpRequest.getParams();
        int timeoutMs = request.getTimeoutMs();
        // TODO: Reevaluate this connection timeout based on more wide-scale
        // data collection and possibly different for wifi vs. 3G.
        if(httpParams == null){
            httpParams = new BasicHttpParams();
        }
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
        return mClient.execute(httpRequest);
    }

    // =============================================================================
    // Static Methods
    // =============================================================================
    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    @SuppressWarnings("unused")
    private static List<NameValuePair> getPostParameterPairs(Map<String, String> postParams) {
        List<NameValuePair> result = new ArrayList<NameValuePair>(postParams.size());
        for (String key : postParams.keySet()) {
            result.add(new BasicNameValuePair(key, postParams.get(key)));
        }
        return result;
    }

    /**
     * 设置请求参数 eg:name=jack age=20
     * @param httpRequest
     * @param request
     * @throws AuthFailureError
     */
    private static void setEntityIfNonEmptyBody(HttpEntityEnclosingRequestBase httpRequest,
                                                Request<?> request) throws AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            HttpEntity entity = new ByteArrayEntity(body);
            httpRequest.setEntity(entity);
        }
    }

    // =============================================================================
    // Methods
    // =============================================================================

    /**
     * Called before the request is executed using the underlying HttpClient.
     *
     * <p>Overwrite in subclasses to augment the request.</p>
     */
    protected void onPrepareRequest(HttpUriRequest request) throws IOException {
        // Nothing.
    }

    // =============================================================================
    // Public classes
    // =============================================================================
    /**
     * The HttpPatch class does not exist in the Android framework, so this has been defined here.
     */
    public static final class HttpPatch extends HttpEntityEnclosingRequestBase {

        public final static String METHOD_NAME = "PATCH";

        public HttpPatch() {
            super();
        }

        public HttpPatch(final URI uri) {
            super();
            setURI(uri);
        }

        /**
         * @throws IllegalArgumentException if the uri is invalid.
         */
        public HttpPatch(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }
}
