package com.daxingframe.frame.network.volley;

/**
 * 实现 RetryPolicy，Volley 默认的重试策略实现类。主要通过在 retry(…) 函数中判断重试次数是否达到上限确定是否继续重试。
 */
public class DefaultRetryPolicy implements RetryPolicy {

    // =============================================================================
    // Constants
    // =============================================================================
    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 2500;
    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;
    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    // =============================================================================
    // Private members
    // =============================================================================
    /** 变量表示当前重试的 timeout 时间，会以mBackoffMultiplier作为因子累计前几次重试的  */
    private int mCurrentTimeoutMs;

    /** 变量表示已经重试次数。 */
    private int mCurrentRetryCount;

    /** The maximum number of attempts. */
    private final int mMaxNumRetries;

    /** 表示每次重试之前的 timeout 该乘以的因子. */
    private final float mBackoffMultiplier;




    /**
     * Constructs a new retry policy using the default timeouts.
     */
    public DefaultRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    /**
     * Constructs a new retry policy.
     * @param initialTimeoutMs The initial timeout for the policy.
     * @param maxNumRetries The maximum number of retries.
     * @param backoffMultiplier Backoff multiplier for the policy.
     */
    public DefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }

    // =============================================================================
    // Supertype overrides
    // =============================================================================
    /**
     * Returns the current timeout.
     */
    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    /**
     * Returns the current retry count.
     */
    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }

    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     * @param error The error code of the last attempt.
     */
    @Override
    public void retry(VolleyError error) throws VolleyError {
        mCurrentRetryCount++;
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
        if (!hasAttemptRemaining()) {
            throw error;
        }
    }

    /**
     * Returns the backoff multiplier for the policy.
     */
    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    protected boolean hasAttemptRemaining() {
        return mCurrentRetryCount <= mMaxNumRetries;
    }
}
