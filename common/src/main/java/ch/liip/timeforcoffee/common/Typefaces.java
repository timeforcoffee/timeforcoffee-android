package ch.liip.timeforcoffee.common;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by nicolas on 20/09/15.
 */
public class Typefaces {

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(c.getAssets(), String.format("fonts/%s.otf", name));
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }
}