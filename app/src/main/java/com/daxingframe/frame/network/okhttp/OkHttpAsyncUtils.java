package com.daxingframe.frame.network.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jack
 * okhttp异步操作
 */
public class OkHttpAsyncUtils {

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

    //===========================================get异步==============================================
    private String result = errorResult;
    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }
    public String getResultForGetAnsyc(String url,String tag){

        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = errorResult;
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    setResult(response.body().string());
                }
            }
        });
        return result;
    }

}
