package ch.liip.timeforcoffee;

import android.app.Application;



import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;

public class TimeForCoffeeApplication extends Application {

    private ObjectGraph graph;

    @Override public void onCreate() {
        super.onCreate();

        initModules();
    }

    private void initModules() {
        List<Object> modules = Collections.<Object>singletonList(new TimeForCoffeeModule());
        this.graph = ObjectGraph.create(modules.toArray());
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
