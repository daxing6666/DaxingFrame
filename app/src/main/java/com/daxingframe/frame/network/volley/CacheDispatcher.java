package com.daxingframe.frame.network.volley;

import android.os.Process;
import java.util.concurrent.BlockingQueue;

/**
 * 一个线程，用于调度处理走缓存的请求。启动后会不断从缓存请求队列中取请求处理，队列为空则等待，
 * 请求处理结束则将结果传递给ResponseDelivery去执行后续处理。
 * 当结果未缓存过、缓存失效或缓存需要刷新的情况下，该请求都需要重新进入NetworkDispatcher去调度处理
 */
public class CacheDispatcher extends Thread {

    // =============================================================================
    // Constants
    // =============================================================================
    private static final boolean DEBUG = VolleyLog.DEBUG;

    /** The queue of requests coming in for triage. */
    private final BlockingQueue<Request<?>> mCacheQueue;

    /** The queue of requests going out to the network. */
    private final BlockingQueue<Request<?>> mNetworkQueue;

    /** The cache to read from. */
    private final Cache mCache;

    /** For posting responses. */
    private final ResponseDelivery mDelivery;

    /** Used for telling us to die. */
    private volatile boolean mQuit = false;

    /**
     * Creates a new cache triage dispatcher thread.  You must call {@link #start()}
     * in order to begin processing.
     *
     * @param cacheQueue Queue of incoming requests for triage
     * @param networkQueue Queue to post requests that require network to
     * @param cache Cache interface to use for resolution
     * @param delivery Delivery interface to use for posting responses
     */
    public CacheDispatcher(
            BlockingQueue<Request<?>> cacheQueue, BlockingQueue<Request<?>> networkQueue,
            Cache cache, ResponseDelivery delivery) {
        mCacheQueue = cacheQueue;
        mNetworkQueue = networkQueue;
        mCache = cache;
        mDelivery = delivery;
    }

    /**
     * Forces this dispatcher to quit immediately.  If any requests are still in
     * the queue, they are not guaranteed to be processed.
     */
    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        if (DEBUG) VolleyLog.v("start new dispatcher");
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // Make a blocking call to initialize the cache.
        mCache.initialize();

        while (true) {
            try {
                // Get a request from the cache triage queue, blocking until
                // at least one is available.
                final Request<?> request = mCacheQueue.take();
                request.addMarker("cache-queue-take");

                // If the request has been canceled, don't bother dispatching it.
                if (request.isCanceled()) {
                    request.finish("cache-discard-canceled");
                    continue;
                }

                // Attempt to retrieve this item from cache.
                Cache.Entry entry = mCache.get(request.getCacheKey());
                if (entry == null) {
                    request.addMarker("cache-miss");
                    // Cache miss; send off to the network dispatcher.
                    mNetworkQueue.put(request);
                    continue;
                }

                // If it is completely expired, just send it to the network.
                if (entry.isExpired()) {
                    request.addMarker("cache-hit-expired");
                    request.setCacheEntry(entry);
                    mNetworkQueue.put(request);
                    continue;
                }

                // We have a cache hit; parse its data for delivery back to the request.
                request.addMarker("cache-hit");
                /**
                 * 返回的原生字节内容
                 */
                Response<?> response = request.parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
                request.addMarker("cache-hit-parsed");
                /**
                 * 缓存是否需要刷新
                 */
                if (!entry.refreshNeeded()) {
                    // Completely unexpired cache hit. Just deliver the response.
                    mDelivery.postResponse(request, response);
                } else {
                    // Soft-expired cache hit. We can deliver the cached response,
                    // but we need to also send the request to the network for
                    // refreshing.
                    request.addMarker("cache-hit-refresh-needed");
                    request.setCacheEntry(entry);

                    // Mark the response as intermediate.
                    response.intermediate = true;

                    // Post the intermediate response back to the user and have
                    // the delivery then forward the request along to the network.
                    mDelivery.postResponse(request, response, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mNetworkQueue.put(request);
                            } catch (InterruptedException e) {
                                // Not much we can do about this.
                            }
                        }
                    });
                }

            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }
        }
    }

    /**
     * 知识点:
     *  对于Android平台上的线程优先级设置来说可以处理很多并发线程的阻塞问题，
     *  比如很多无关紧要的线程会占用大量的CPU时间，
     *  虽然通过了MultiThread来解决慢速I/O但是合理分配优先级对于并发编程来说十分重要。
     *  Android在线程方面主要使用的是Java本身的Thread类，
     *  我们可以在Thread或Runnable接口中的run方法首句加入Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND); //设置线程优先级为后台，
     *  这样当多个线程并发后很多无关紧要的线程分配的CPU时间将会减少，有利于主线程的处理，
     *  相关的Thread优先级就Android平台专有的定义有以下几种:
     *
     *  int THREAD_PRIORITY_AUDIO //标准音乐播放使用的线程优先级

     　　int THREAD_PRIORITY_BACKGROUND //标准后台程序

     　　int THREAD_PRIORITY_DEFAULT // 默认应用的优先级

     　　int THREAD_PRIORITY_DISPLAY //标准显示系统优先级，主要是改善UI的刷新

     　　int THREAD_PRIORITY_FOREGROUND //标准前台线程优先级

     　　int THREAD_PRIORITY_LESS_FAVORABLE //低于favorable

     　　int THREAD_PRIORITY_LOWEST //有效的线程最低的优先级

     　　int THREAD_PRIORITY_MORE_FAVORABLE //高于favorable

     　　int THREAD_PRIORITY_URGENT_AUDIO //标准较重要音频播放优先级

     　　int THREAD_PRIORITY_URGENT_DISPLAY //标准较重要显示优先级，对于输入事件同样适用。
     */
}
