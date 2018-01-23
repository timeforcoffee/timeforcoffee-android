package ch.liip.timeforcoffee.api;

import android.util.LruCache;

import ch.liip.timeforcoffee.api.models.WalkingDistance;

/* Created by nicolas on 16/05/16.
 */
public class WalkingDistanceCache {

    private static final int CACHE_SIZE = 1000;

    private static WalkingDistanceCache singleton;
    private LruCache<String, WalkingDistance> mCache;

    public static WalkingDistanceCache getInstance() {
        if (singleton == null) {
            singleton = new WalkingDistanceCache();
        }
        return singleton;
    }

    private WalkingDistanceCache() {
        mCache = new LruCache(CACHE_SIZE);
    }

    public LruCache getCache() {
        return mCache;
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
