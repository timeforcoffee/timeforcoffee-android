package ch.liip.timeforcoffee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import javax.inject.Singleton;

import ch.liip.timeforcoffee.api.ConnectionService;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.backend.OpenDataService;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.presenter.WearPresenter;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                WearPresenter.class,
                StationListFragment.class,
                DepartureListFragment.class,
        }
)

class TimeForCoffeeWearModule {

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    BackendService provideBackendService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.BACKEND_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build();

        return restAdapter.create(BackendService.class);
    }

    @Provides
    @Singleton
    OpenDataService provideOpenDataService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.OPEN_DATA_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build();

        return restAdapter.create(OpenDataService.class);
    }

    @Provides
    @Singleton
    StationService provideStationService(EventBus eventBus, OpenDataService openDataService) {
        return new StationService(eventBus, openDataService);
    }

    @Provides
    @Singleton
    DepartureService provideDepartureService(EventBus eventBus, BackendService backendService) {
        return new DepartureService(eventBus, backendService);
    }
}
