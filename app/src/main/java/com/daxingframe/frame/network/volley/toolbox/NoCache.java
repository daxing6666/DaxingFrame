package com.daxingframe.frame.network.volley.toolbox;

import com.daxingframe.frame.network.volley.Cache;

/**
 * 继承 Cache 类，不做任何操作的缓存实现类，可将它作为构建RequestQueue的参数以实现一个不带缓存的请求队列
 */
public class NoCache implements Cache {

    @Override
    public void clear() {
    }

    @Override
    public Entry get(String key) {
        return null;
    }

    @Override
    public void put(String key, Entry entry) {
    }

    @Override
    public void invalidate(String key, boolean fullExpire) {
    }

    @Override
    public void remove(String key) {
    }

    @Override
    public void initialize() {
    }
}
