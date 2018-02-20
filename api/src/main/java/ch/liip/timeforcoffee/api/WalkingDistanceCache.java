package ch.liip.timeforcoffee.api;

import android.util.LruCache;

import ch.liip.timeforcoffee.api.models.WalkingDistance;

public class WalkingDistanceCache {

    private static final int CACHE_SIZE = 1000;
    private static WalkingDistanceCache singleton;

    private final LruCache<String, WalkingDistance> mCache = new LruCache(CACHE_SIZE);

    public static WalkingDistanceCache getInstance() {
        if (singleton == null) {
            singleton = new WalkingDistanceCache();
        }
        return singleton;
    }

    public WalkingDistance get(String key) {
        synchronized (mCache) {
            return mCache.get(key);
        }
    }

    public void put(String key, WalkingDistance value){
        synchronized (mCache) {
            if (mCache.get(key) == null) {
                mCache.put(key, value);
            }
        }
    }
}
