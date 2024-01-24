package com.as.demo.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;

public class LocalCache {

    //默认缓存时长 单位s
    private static final Long DEFAULT_TIMEOUT = 1 * 60 * 50L;
    //默认清理间隔时间 单位s
    private static final Long CLEAN_TIMEOUT = 1 * 60 * 50L;
    //缓存对象
    public static TimedCache<String, String> timedCache = CacheUtil.newTimedCache(DEFAULT_TIMEOUT);

    static {
        //启动定时任务
        timedCache.schedulePrune(CLEAN_TIMEOUT);
    }

    public static void put(String key, String value) {
        timedCache.put(key, value);
    }

    public static void put(String key, String value, Integer expire) {
        timedCache.put(key, value, DateUnit.SECOND.getMillis() * expire);
    }

    public static String get(String key, boolean isUpdateLastAccess) {
        return timedCache.get(key, isUpdateLastAccess);
    }

    public static String get(String key) {
        return timedCache.get(key);
    }

    public static void remove(String key) {
        timedCache.remove(key);
    }

    public static void clear() {
        timedCache.clear();
    }

}