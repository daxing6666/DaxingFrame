package com.daxingframe.frame.network.original;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * [description about this class]
 *
 * @author jack
 * @version [Briup, 2015/08/07 13:54]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class HttpRequestUtil {

    /**
     * 发送GET请求
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static String sendGetRequest(String url,
                                        Map<String, String> params, Map<String, String> headers)
            throws Exception {
        StringBuilder buf = new StringBuilder(url);
        Set<Entry<String, String>> entrys = null;
        // 如果是GET请求，则请求参数在URL中
        if (params != null && !params.isEmpty()) {
            buf.append("?");
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                buf.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }
        HttpURLConnection conn = null;
        URL url1 = new URL(buf.toString());
        if (url.startsWith("https")) {
            TrustAnyX509TrustManager mTrustAnyX509TrustManager = new TrustAnyX509TrustManager();
            TrustManager[] mTrustManagers = new TrustManager[] {mTrustAnyX509TrustManager };
            TrustAnyHostnameVerifier mTrustAnyHostnameVerifier = new TrustAnyHostnameVerifier();
            SecureRandom mSecureRandom = new SecureRandom();
            SSLContext mSslContext = SSLContext.getInstance("TLS");
            mSslContext.init(null, mTrustManagers, mSecureRandom);
            HttpsURLConnection mHttpsURLConnection = (HttpsURLConnection) url1.openConnection();
            mHttpsURLConnection.setSSLSocketFactory(mSslContext.getSocketFactory());
            mHttpsURLConnection.setHostnameVerifier(mTrustAnyHostnameVerifier);
            conn = mHttpsURLConnection;
        } else
        {
            conn = (HttpURLConnection) url1.openConnection();
        }
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8 * 1000);//设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(8 * 1000);//设置从主机读取数据超时（单位：毫秒）
        conn.setUseCaches(false);//请求是否使用缓存
        conn.setDoInput(true);//设置是否从httpUrlConnection读入,默认情况下是true;
        conn.setRequestProperty("Content-Type","text/html; charset=UTF-8");
        //conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        //conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            entrys = headers.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        int res = conn.getResponseCode();
        if (res == 200){

            StringBuilder builder = new StringBuilder();
            // 获取响应内容
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            for (String s = reader.readLine(); s != null; s = reader
                    .readLine()) {
                builder.append(s);
            }
            return builder.toString();
        }else {
            return String.valueOf(res);
        }
    }

    /**
     * 发送POST请求
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static String sendPostRequest(String url,
                                         Map<String, String> params, Map<String, String> headers)
            throws Exception {
        StringBuilder buf = new StringBuilder();
        Set<Entry<String, String>> entrys = null;
        // 如果存在参数，则放在HTTP请求体，形如name=a&age=10
        if (params != null && !params.isEmpty()) {
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                buf.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }
        HttpURLConnection conn = null;
        URL url1 = new URL(buf.toString());
        if (url.startsWith("https")) {
            TrustAnyX509TrustManager mTrustAnyX509TrustManager = new TrustAnyX509TrustManager();
            TrustManager[] mTrustManagers = new TrustManager[] {mTrustAnyX509TrustManager };
            TrustAnyHostnameVerifier mTrustAnyHostnameVerifier = new TrustAnyHostnameVerifier();
            SecureRandom mSecureRandom = new SecureRandom();
            SSLContext mSslContext = SSLContext.getInstance("TLS");
            mSslContext.init(null, mTrustManagers, mSecureRandom);
            HttpsURLConnection mHttpsURLConnection = (HttpsURLConnection) url1.openConnection();
            mHttpsURLConnection.setSSLSocketFactory(mSslContext.getSocketFactory());
            mHttpsURLConnection.setHostnameVerifier(mTrustAnyHostnameVerifier);
            conn = mHttpsURLConnection;
        } else
        {
            conn = (HttpURLConnection) url1.openConnection();
        }
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(8 * 1000);//设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(8 * 1000);//设置从主机读取数据超时（单位：毫秒）
        conn.setUseCaches(false);//请求是否使用缓存
        conn.setDoInput(true);//设置是否从httpUrlConnection读入,默认情况下是true;
        conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        out.write(buf.toString().getBytes("UTF-8"));
        out.close();
        if (headers != null && !headers.isEmpty()) {
            entrys = headers.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        int res = conn.getResponseCode();
        if (res == 200){

            StringBuilder builder = new StringBuilder();
            // 获取响应内容
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            for (String s = reader.readLine(); s != null; s = reader
                    .readLine()) {
                builder.append(s);
            }
            return builder.toString();
        }else {
            return String.valueOf(res);
        }
    }

    /**
     * 提交数据到服务器
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file 上传文件
     */
    public static boolean uploadFile(String path, Map<String, String> params, FormFile file) throws Exception{
        return uploadFiles(path, params, new FormFile[]{file});
    }

    /**
     * @param path 上传路径(使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param files 上传文件
     */
    public static boolean uploadFiles(String path, Map<String, String> params, FormFile[] files) throws Exception{
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        if(files!=null&&files.length!=0){
            for(FormFile uploadFile : files){//得到文件类型数据的总长度
                StringBuilder fileExplain = new StringBuilder();
                fileExplain.append("--");
                fileExplain.append(BOUNDARY);
                fileExplain.append("\r\n");
                fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
                fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
                fileExplain.append("\r\n");
                fileDataLength += fileExplain.length();
                if(uploadFile.getInStream()!=null){
                    fileDataLength += uploadFile.getFile().length();
                }else{
                    fileDataLength += uploadFile.getData().length;
                }
            }
        }
        StringBuilder textEntity = new StringBuilder();
        if(params!=null&&!params.isEmpty()){
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort()==-1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        if(files!=null&&files.length!=0){
            for(FormFile uploadFile : files){
                StringBuilder fileEntity = new StringBuilder();
                fileEntity.append("--");
                fileEntity.append(BOUNDARY);
                fileEntity.append("\r\n");
                fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
                fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
                outStream.write(fileEntity.toString().getBytes());
                if(uploadFile.getInStream()!=null){
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1){
                        outStream.write(buffer, 0, len);
                    }
                    uploadFile.getInStream().close();
                }else{
                    outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
                }
                outStream.write("\r\n".getBytes());
            }
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if(reader.readLine().indexOf("200")==-1){//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return false;
        }
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return true;
    }

    /**
     * 将输入流转为字节数组
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] read2Byte(InputStream inStream)throws Exception{
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
    /**
     * 将输入流转为字符串
     * @param inStream
     * @return
     * @throws Exception
     */

    public static String read2String(InputStream inStream)throws Exception{
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return new String(outSteam.toByteArray(),"UTF-8");
    }

    /**
     * 发送xml数据
     * @param path 请求地址
     * @param xml xml数据
     * @param encoding 编码
     * @return
     * @throws Exception
     */
    public static byte[] postXml(String path, String xml, String encoding) throws Exception{
        byte[] data = xml.getBytes(encoding);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset="+ encoding);
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        conn.setConnectTimeout(5 * 1000);
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if(conn.getResponseCode()==200){
            return read2Byte(conn.getInputStream());
        }
        return null;
    }
}
