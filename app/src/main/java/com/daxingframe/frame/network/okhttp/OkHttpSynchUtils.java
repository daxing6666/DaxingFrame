package com.daxingframe.frame.network.okhttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jack
 * okhttp同步操作
 */
public class OkHttpSynchUtils {


    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final String CHARSET_NAME = "UTF-8";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String errorResult = "";

    static
    {
        mOkHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().readTimeout(20, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().writeTimeout(20, TimeUnit.SECONDS);
    }

    //=======================================GET同步需要自己开一个线程去请求==================================================
    /**
     *
     *  不会开启异步线程。
     *  下载一个文件，打印他的响应头，以string形式打印响应体。
     响应体的 string() 方法对于小文档来说十分方便、高效。但是如果响应体太大（超过1MB），应避免适应 string()方法因为他会将把整个文档加载到内存中。
     对于超过1MB的响应body，应使用流的方式来处理body。
     * @param url
     * @return
     * @throws IOException
     */
    public String getResultForGet(String url,String tag) throws IOException{
        String result = errorResult;
        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            result = response.body().string();
        }
        /**
         * 如果需要header的可以得到
         */
//        Headers responseHeaders = response.headers();
//        for (int i = 0; i < responseHeaders.size(); i++) {
//            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//        }
        return result;
    }

    //===========================================post==============================================
    /**
     * POST提交Json数据
     * @param url
     * @param json
     * @return
     */
    public String getResultForPost(String url, String json){
        String result = errorResult;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            if(response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * POST提交key-value数据
     * @param url
     * @param map
     * @return
     */
    public String getResultForPost(String url, HashMap map){
        String result = errorResult;
        if(map != null && map.size()>0){
            Iterator iter = map.entrySet().iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                stringBuilder.append(key);
                stringBuilder.append("=");
                stringBuilder.append(val);
                if(iter.hasNext()){
                    stringBuilder.append("&");
                }
            }
            RequestBody body = RequestBody.create(JSON, stringBuilder.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                if(response.isSuccessful()) {
                    result = response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        return  result;

    }
}
