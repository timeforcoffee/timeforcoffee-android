package ch.liip.timeforcoffee;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by fsantschi on 08/03/15.
 */
public class TimeForCoffeeApplication extends Application {

    private static Context context;
    private ObjectGraph graph;

    @Override public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        context = getApplicationContext();
        graph = ObjectGraph.create(getModules().toArray());

    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new TimeForCoffeeModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    public static Context getAppContext() {
        return context;
    }
}
