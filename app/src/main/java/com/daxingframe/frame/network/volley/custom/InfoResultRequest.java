package com.daxingframe.frame.network.volley.custom;

import android.content.Context;
import android.text.TextUtils;
import com.daxingframe.AppDroid;
import com.daxingframe.R;
import com.daxingframe.frame.interfaces.IresponseParserListener;
import com.daxingframe.frame.interfaces.ILogic;
import com.daxingframe.frame.model.InfoResult;
import com.daxingframe.frame.network.volley.AuthFailureError;
import com.daxingframe.frame.network.volley.DefaultRetryPolicy;
import com.daxingframe.frame.network.volley.NetworkResponse;
import com.daxingframe.frame.network.volley.Request;
import com.daxingframe.frame.network.volley.Response;
import com.daxingframe.frame.network.volley.RetryPolicy;
import com.daxingframe.frame.network.volley.VolleyError;
import com.daxingframe.frame.network.volley.toolbox.HttpHeaderParser;
import com.daxingframe.util.NetworkUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * [description about this class]
 * 自己定义的Volley请求
 * @author jack
 * @version [DaxingFrame, 2016/03/02 12:00]
 */
public class InfoResultRequest extends Request<InfoResult> implements Response.Listener<InfoResult> {

    /**
     * 设置超时时间为20s
     */
    public static final int timeoutperiod = 20 * 1000;
    /** request identifier*/
    private int requestId;
    /** request headers*/
    private Map<String, String> headers;
    /** 回调业务层做解析Bean封装*/
    private IresponseParserListener parserListener;
    /** request params*/
    private Map<String, String> params;
    /** 知否是同步阻塞型请求*/
    private boolean isSyncRequest;
    /** 请求消息体*/
    private String requestBody;
    /** 分发解析好的数据到业务层*/
    private ILogic logic;
    /**
     * 是否返回字节数组
     */
    private boolean isNeedByteStream = false;

    /**
     * get请求
     * @param requestId
     * @param url
     * @param parseListener
     * @param logic
     */
    public InfoResultRequest(int requestId, String url, IresponseParserListener parseListener, ILogic logic) {
        this(requestId, url, Method.GET, null, null, parseListener, logic);
    }

    public InfoResultRequest(int requestId, String url, Map<String, String> params, IresponseParserListener parseListener, ILogic logic)
    {
        this(requestId, url, Method.POST, params, null, parseListener, logic);
    }

    public InfoResultRequest(int requestId, String url, String requestBody, IresponseParserListener parseListener, ILogic logic)
    {
        this(requestId, url, Method.POST, null, requestBody, parseListener, logic);
    }

    public InfoResultRequest(final int requestId, String url, int method, Map<String, String> params, String requestBody, final IresponseParserListener parseListener, final ILogic logic)
    {
        super(method, url, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                try
                {
                    InfoResult infoResult = null;
                    Context context = AppDroid.getInstance().getApplicationContext();
                    if (NetworkUtils.isNetworkConnected(context)){
                        // 错误
                        StringBuilder sb = new StringBuilder();
                        sb.append("{");

                        sb.append("\"state\"");
                        sb.append(":");
                        sb.append("\"-100\"");
                        sb.append(",");

                        sb.append("\"desc\"");
                        sb.append(":");
                        sb.append("\"context.getString(R.string.requesting_failure)\"");
                        sb.append(",");

                        sb.append("\"code\"");
                        sb.append(":");
                        sb.append("\"-100\"");
                        sb.append(",");

                        sb.append("\"data\"");
                        sb.append(":");
                        sb.append("\"{}\"");

                        sb.append("}");
                        String result = sb.toString();
                        infoResult = parseListener.doParse(result);
                    }
                    //没有网络的情况下,(如果有业务需要,我们可以在这里做操作)
                    else
                    {
                        infoResult = parseListener.doParse("{\"state\": -100,\"errMsg\": \""
                                + context.getString(R.string.network_unavailable) + "\",\"data\": {}}");

                    }
                    infoResult.setExtraObj(error);
                    logic.onResult(requestId, infoResult);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        this.params = params;
        this.requestBody = requestBody;
        this.parserListener = parseListener;
        this.requestId = requestId;
        this.logic = logic;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeoutperiod, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);
        if (requestId != R.id.userlogin)
        {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("access-token", AppDroid.getInstance().getUserInfo().getToken());
            addHeaders(headers);
        }
    }

    /**
     * 重写此方法，可以构建用于 POST、PUT、PATCH 请求方式的 Body 内容。
     * @return
     * @throws AuthFailureError
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (!TextUtils.isEmpty(requestBody))
        {
            try {
                return requestBody.getBytes(getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return super.getBody();
    }

    /**
     * 在上面getBody函数没有被重写情况下，此方法的返回值会被 key、value 分别编码后拼装起来转换为字节码作为 Body 内容。
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return params;
    }

    /**
     * 返回POST或PUT请求内容的类型
     * @return
     */
    @Override
    public String getBodyContentType() {
        if (!TextUtils.isEmpty(requestBody)){
            return "application/json";
        }
        return super.getBodyContentType();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        if (headers != null)
        {
            return headers;
        }
        else
        {
            return super.getHeaders();
        }
    }

    /**
     * 子类重写此方法，将网络返回的原生字节内容，转换成合适的类型。此方法会在工作线程中被调用。
     * @param response
     * @return
     */
    @Override
    protected Response<InfoResult> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            InfoResult infoResult = null;
            if (isNeedByteStream()) {
                infoResult = parserListener.doParse(response.data);
            }
            else {
                String str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                infoResult = parserListener.doParse(str);
            }
            if (infoResult == null) {
                return Response.error(new VolleyError("parse response error"));
            }
            // 解析成功
            else {
                return Response.success(infoResult, HttpHeaderParser.parseCacheHeaders(response));
            }
        }
        catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError("UnsupportedEncodingException"));
        }
        catch (Exception e) {
            return Response.error(new VolleyError("Exception is >>> " + e.getMessage()));
        }
    }

    /**
     * 子类重写此方法，将解析成合适类型的内容传递给它们的监听回调。
     * @param response
     */
    @Override
    protected void deliverResponse(InfoResult response) {
        this.onResponse(response);
    }

    /**
     * 网络返回的结果数据解析为 InfoResult 类
     * @param response
     */
    @Override
    public void onResponse(InfoResult response) {
        logic.onResult(requestId, response);
    }

    /**
     * 修改参数值
     * @param key 键
     * @param value 值
     */
    public void setParamValue(String key, String value)
    {
        if (params != null){
            params.put(key, value);
        }
    }

    /**
     * 更新请求数据
     */
    public void updateRequest() {

    }

    /**
     * 设置头信息
     * @param headers
     */
    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    /**
     * 添加头信息
     * @param headers
     */
    public void addHeaders(Map<String, String> headers)
    {
        if (this.headers == null || headers == null){
            setHeaders(headers);
        }
        else{
            this.headers.putAll(headers);
        }
    }

    /**
     * 是否是同步的请求, 如果是则其他所有请求都必须等待该请求结束才能执行
     * @return
     */
    public boolean isSyncRequest()
    {
        return isSyncRequest;
    }

    public void setSyncRequest(boolean isSyncRequest)
    {
        this.isSyncRequest = isSyncRequest;
    }

    public int getRequestId()
    {
        return requestId;
    }

    public boolean isNeedByteStream() {
        return isNeedByteStream;
    }

    public void setIsNeedByteStream(boolean isNeedByteStream) {
        this.isNeedByteStream = isNeedByteStream;
    }
}
