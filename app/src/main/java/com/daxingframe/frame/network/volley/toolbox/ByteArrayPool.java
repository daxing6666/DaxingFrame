package com.daxingframe.frame.network.volley.toolbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * byte[] 的回收池，用于 byte[] 的回收再利用，减少了内存的分配和回收
 */
public class ByteArrayPool {
    // =============================================================================
    // Private members
    // =============================================================================
    /** The buffer pool, arranged both by last use and by buffer size */
    private List<byte[]> mBuffersByLastUse = new LinkedList<byte[]>();
    private List<byte[]> mBuffersBySize = new ArrayList<byte[]>(64);
    /** The total size of the buffers in the pool */
    private int mCurrentSize = 0;
    /**
     * The maximum aggregate size of the buffers in the pool. Old buffers are discarded to stay
     * under this limit.
     */
    private final int mSizeLimit;

    // =============================================================================
    // Constructors
    // =============================================================================
    /**
     * @param sizeLimit the maximum size of the pool, in bytes
     */
    public ByteArrayPool(int sizeLimit) {
        mSizeLimit = sizeLimit;
    }

    // =============================================================================
    // Static Methods
    // =============================================================================
    /** Compares buffers by size */
    protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator<byte[]>() {
        @Override
        public int compare(byte[] lhs, byte[] rhs) {
            return lhs.length - rhs.length;
        }
    };

    // =============================================================================
    // Methods
    // =============================================================================
    /**
     * 获取长度不小于 len 的 byte[]，遍历缓存，找出第一个长度大于传入参数len的 byte[]，
     * 并返回；如果最终没有合适的 byte[]，new 一个返回
     */
    public synchronized byte[] getBuf(int len) {
        for (int i = 0; i < mBuffersBySize.size(); i++) {
            byte[] buf = mBuffersBySize.get(i);
            if (buf.length >= len) {
                mCurrentSize -= buf.length;
                mBuffersBySize.remove(i);
                mBuffersByLastUse.remove(buf);
                return buf;
            }
        }
        return new byte[len];
    }

    /**
     将用过的 byte[] 回收，根据 byte[] 长度按照从小到大的排序将 byte[] 插入到缓存中合适位置
     *
     * @param buf the buffer to return to the pool.
     */
    public synchronized void returnBuf(byte[] buf) {
        if (buf == null || buf.length > mSizeLimit) {
            return;
        }
        mBuffersByLastUse.add(buf);
        int pos = Collections.binarySearch(mBuffersBySize, buf, BUF_COMPARATOR);
        if (pos < 0) {
            pos = -pos - 1;
        }
        mBuffersBySize.add(pos, buf);
        mCurrentSize += buf.length;
        trim();
    }

    /**
     * 当缓存的 byte 超过预先设置的大小时，按照先进先出的顺序删除最早的 byte[]。
     */
    private synchronized void trim() {
        while (mCurrentSize > mSizeLimit) {
            byte[] buf = mBuffersByLastUse.remove(0);
            mBuffersBySize.remove(buf);
            mCurrentSize -= buf.length;
        }
    }

}
