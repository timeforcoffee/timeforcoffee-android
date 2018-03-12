package ch.liip.timeforcoffee;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

public class TimeForCoffeeApplication extends Application {

    private ObjectGraph graph;

    @Override public void onCreate() {
        super.onCreate();

        initCrashlytics();
        initModules();
    }

    private void initCrashlytics() {
        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();

        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());
    }

    private void initModules() {
        List<Object> modules = Collections.<Object>singletonList(new TimeForCoffeeModule());
        this.graph = ObjectGraph.create(modules.toArray());
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
