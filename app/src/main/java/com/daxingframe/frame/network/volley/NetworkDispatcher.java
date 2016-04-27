package com.daxingframe.frame.network.volley;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;

import java.util.concurrent.BlockingQueue;

/**
 * 一个线程，用于调度处理走网络的请求。启动后会不断从网络请求队列中取请求处理，队列为空则等待，
 * 请求处理结束则将结果传递给ResponseDelivery去执行后续处理，并判断结果是否要进行缓存
 */
public class NetworkDispatcher extends Thread {

    // =============================================================================
    // Private members
    // =============================================================================
    /** The queue of requests to service. */
    private final BlockingQueue<Request<?>> mQueue;
    /** The network interface for processing requests. */
    private final Network mNetwork;
    /** The cache to write to. */
    private final Cache mCache;
    /** For posting responses and errors. */
    private final ResponseDelivery mDelivery;
    /** Used for telling us to die. */
    private volatile boolean mQuit = false;

    // =============================================================================
    // Constructors
    // =============================================================================
    /**
     * Creates a new network dispatcher thread.  You must call {@link #start()}
     * in order to begin processing.
     *
     * @param queue Queue of incoming requests for triage
     * @param network Network interface to use for performing requests
     * @param cache Cache interface to use for writing responses to cache
     * @param delivery Delivery interface to use for posting responses
     */
    public NetworkDispatcher(BlockingQueue<Request<?>> queue,
                             Network network, Cache cache,
                             ResponseDelivery delivery) {
        mQueue = queue;
        mNetwork = network;
        mCache = cache;
        mDelivery = delivery;
    }
    // =============================================================================
    // Supertype overrides
    // =============================================================================
    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Request<?> request;
        while (true) {
            long startTimeMs = SystemClock.elapsedRealtime();
            // release previous request object to avoid leaking request object when mQueue is drained.
            request = null;
            try {
                // Take a request from the queue.
                request = mQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }

            try {
                request.addMarker("network-queue-take");

                // If the request was cancelled already, do not perform the
                // network request.
                if (request.isCanceled()) {
                    request.finish("network-discard-cancelled");
                    continue;
                }

                addTrafficStatsTag(request);

                /** Perform the network request.
                 *  通过NetWork执行Request请求,获取httpnetworkResponse
                 */
                NetworkResponse networkResponse = mNetwork.performRequest(request);
                request.addMarker("network-http-complete");

                // If the server returned 304 AND we delivered a response already,
                // we're done -- don't deliver a second identical response.
                /**
                 * 自从上次请求后，请求的网页未修改过。服务器返回此响应时，不会返回网页内容。
                 如果网页自请求者上次请求后再也没有更改过，
                 您应将服务器配置为返回此响应（称为 If-Modified-Since HTTP 标头）。
                 服务器可以告诉 Googlebot 自从上次抓取后网页没有变更，进而节省带宽和开销。
                 */
                //304响应且已经有响应传输(即用来验证是否更新请求)
                if (networkResponse.notModified && request.hasHadResponseDelivered()) {
                    request.finish("not-modified");
                    continue;
                }

                // Parse the response here on the worker thread.
                Response<?> response = request.parseNetworkResponse(networkResponse);
                request.addMarker("network-parse-complete");

                // Write to cache if applicable.
                // TODO: Only update cache metadata instead of entire record for 304s.
                /**
                 * 判断请求是否可以缓存并且缓存实体不为空
                 */
                if (request.shouldCache() && response.cacheEntry != null) {
                    mCache.put(request.getCacheKey(), response.cacheEntry);
                    request.addMarker("network-cache-written");
                }

                // Post the response back.
                request.markDelivered();
                mDelivery.postResponse(request, response);
            } catch (VolleyError volleyError) {
                volleyError.setNetworkTimeMs(SystemClock.elapsedRealtime() - startTimeMs);
                parseAndDeliverNetworkError(request, volleyError);
            } catch (Exception e) {
                VolleyLog.e(e, "Unhandled exception %s", e.toString());
                VolleyError volleyError = new VolleyError(e);
                volleyError.setNetworkTimeMs(SystemClock.elapsedRealtime() - startTimeMs);
                mDelivery.postError(request, volleyError);
            }
        }
    }

    // =============================================================================
    // Methods
    // =============================================================================
    /**
     * Forces this dispatcher to quit immediately.  If any requests are still in
     * the queue, they are not guaranteed to be processed.
     */
    public void quit() {
        mQuit = true;
        interrupt();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void addTrafficStatsTag(Request<?> request) {
        // Tag the request (if API >= 14)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            //给访问网络的线程设置一个标签
            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
        }
    }

    private void parseAndDeliverNetworkError(Request<?> request, VolleyError error) {
        error = request.parseNetworkError(error);
        mDelivery.postError(request, error);
    }
}
