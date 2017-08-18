package ch.liip.timeforcoffee;

import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.OpenDataApiService;
import ch.liip.timeforcoffee.api.ZvvApiService;
import ch.liip.timeforcoffee.opendata.TransportService;
import ch.liip.timeforcoffee.presenter.*;
import ch.liip.timeforcoffee.wear.DataService;
import ch.liip.timeforcoffee.zvv.ZvvService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import javax.inject.Singleton;

/**
 * Created by fsantschi on 08/03/15.
 */
@Module(
        injects = {
                DataService.class,
                MainPresenter.class,
                DeparturesPresenter.class,
                StationSearchPresenter.class
        }
)
class TimeForCoffeeModule {

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    TransportService provideTransportService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://transport.opendata.ch")
                .setConverter(new GsonConverter(gson))
                .build();

        TransportService service = restAdapter.create(TransportService.class);
        return service;
    }

    @Provides
    @Singleton
    ZvvService provideZvvService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://tfc.chregu.tv")
                .setConverter(new GsonConverter(gson))
                .build();

        ZvvService service = restAdapter.create(ZvvService.class);
        return service;
    }

    @Provides
    @Singleton
    OpenDataApiService provideOpenDataApiService(EventBus eventBus, TransportService transportService) {
        return new OpenDataApiService(eventBus, transportService);
    }

    @Provides
    @Singleton
    ZvvApiService provideZvvApiService(EventBus eventBus, ZvvService zvvService) {
        return new ZvvApiService(eventBus, zvvService);
    }

    @Provides
    @Singleton
    DepartureService provideDepartureService(EventBus eventBus) {
        return new DepartureService(eventBus);
    }

}
