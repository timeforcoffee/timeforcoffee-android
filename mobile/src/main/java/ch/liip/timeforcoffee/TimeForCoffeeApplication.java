package ch.liip.timeforcoffee;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by fsantschi on 08/03/15.
 */
public class TimeForCoffeeApplication extends Application {
    private ObjectGraph graph;

    @Override public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

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
}
