package com.daxingframe.frame.network.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import com.daxingframe.frame.network.volley.Network;
import com.daxingframe.frame.network.volley.RequestQueue;
import java.io.File;

/**
 * 构建一个可用于添加网络请求的RequestQueue对象
 */
public class Volley {

    /** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "volley";

    /**
     * Creates a default instance of the worker pool and calls { RequestQueue#start()} on it.
     * You may set a maximum size of the disk cache in bytes.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     * @param maxDiskCacheBytes the maximum size of the disk cache, in bytes. Use -1 for default size.
     * @return A started { RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack, int maxDiskCacheBytes) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (NameNotFoundException e) {
        }
        /**
         * 9以上都是用HttpURLConnection，9以下用HttpClient。
         */
        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }
        /**
         * 得到了 HttpStack,然后通过它构造一个代表网络（Network）的具体实现BasicNetwork。
         */
        Network network = new BasicNetwork(stack);
        RequestQueue queue;
        /**
         * 构造一个代表缓存（Cache）的基于 Disk 的具体实现DiskBasedCache,
         * 最后将网络（Network）对象和缓存（Cache）对象传入构建一个 RequestQueue，启动这个 RequestQueue
         */
        if (maxDiskCacheBytes <= -1)
        {
            // No maximum size specified
            queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        }
        else
        {
            // Disk cache size specified
            queue = new RequestQueue(new DiskBasedCache(cacheDir, maxDiskCacheBytes), network);
        }
        queue.start();
        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     * You may set a maximum size of the disk cache in bytes.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param maxDiskCacheBytes the maximum size of the disk cache, in bytes. Use -1 for default size.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, int maxDiskCacheBytes) {
        return newRequestQueue(context, null, maxDiskCacheBytes);
    }

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack)
    {
        return newRequestQueue(context, stack, -1);
    }

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }

    /**
     * 我们平时大多采用Volly.newRequestQueue(context)的默认实现，构建 RequestQueue。
     通过源码可以看出，我们可以抛开 Volley 工具类构建自定义的 RequestQueue，采用自定义的HttpStatck，采用自定义的Network实现，采用自定义的 Cache 实现等来构建RequestQueue。
     优秀框架的高可拓展性的魅力来源于此啊
     */
}


