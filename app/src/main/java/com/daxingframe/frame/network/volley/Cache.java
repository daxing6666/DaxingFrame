package com.daxingframe.frame.network.volley;

import java.util.Collections;
import java.util.Map;

/**
 * 缓存接口，代表了一个可以获取请求结果，存储请求结果的缓存
 */
public interface Cache {
    /**
     通过 key 获取请求的缓存实体
     */
    public Entry get(String key);

    /**
     存入一个请求的缓存实体
     */
    public void put(String key, Entry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     *
     *  potentially:[p?'ten??l?] 潜在地 可能地
     *  初始化
     */
    public void initialize();

    /**
     * Invalidates an entry in the cache.
     * @param key Cache key
     * @param fullExpire True to fully expire the entry, false to soft expire
     *
     * Invalidates:使无效
     * fullExpire:全到期
     */
    public void invalidate(String key, boolean fullExpire);

    /**
     * 移除指定的缓存实体
     */
    public void remove(String key);

    /**
     * 清空缓存
     */
    public void clear();

    /**
     * Data and metadata for an entry returned by the cache.
     */

    // =============================================================================
    // Public classes
    // =============================================================================
    public static class Entry {

        /** The data returned from cache.
         *  请求返回的数据（Body 实体）
         * */
        public byte[] data;

        /** Http 响应首部中用于缓存新鲜度验证的 ETag*/
        public String etag;

        /**  Http 响应首部中的响应产生时间 */
        public long serverDate;

        /** The last modified date for the requested object. */
        public long lastModified;

        /**  缓存的过期时间 */
        public long ttl;

        /**  缓存的新鲜时间 */
        public long softTtl;

        /**  响应的 Headers */
        public Map<String, String> responseHeaders = Collections.emptyMap();

        /** 判断缓存是否过期，过期缓存不能继续使用 */
        public boolean isExpired() {
            return this.ttl < System.currentTimeMillis();
        }

        /** 判断缓存是否新鲜，不新鲜的缓存需要发到服务端做新鲜度的检测 */
        public boolean refreshNeeded() {
            return this.softTtl < System.currentTimeMillis();
        }
    }

    /**
     * 知识点:
     * System类代表系统，系统级的很多属性和控制方法都放置在该类的内部。该类位于java.lang包。
     currentTimeMillis方法
     public static long currentTimeMillis()
     该方法的作用是返回当前的计算机时间，时间的表达格式为当前计算机时间和GMT时间(格林威治时间)1970年1月1号0时0分0秒所差的毫秒数。
     可以直接把这个方法强制转换成date类型。
     代码如下：
     long currentTime = System.currentTimeMillis();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
     Date date = new Date(currentTime);
     System.out.println(formatter.format(date));
     运行结果如下：
     当前时间:2011年-08月10日-14时11分46秒
     另：
     可获得当前的系统和用户属性：
     String osName = System.getProperty(“os.name”);
     　　String user = System.getProperty(“user.name”);
     　　System.out.println(“当前操作系统是：” + osName);
     　　System.out.println(“当前用户是：” + user);
     System.getProperty 这个方法可以得到很多系统的属性。
     */
}
