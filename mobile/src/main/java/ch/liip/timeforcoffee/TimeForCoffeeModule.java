package ch.liip.timeforcoffee;

import ch.liip.timeforcoffee.api.ConnectionService;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.OpenDataApiService;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.ZvvApiService;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.api.deserializers.ConnectionsDeserializer;
import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.opendata.TransportService;
import ch.liip.timeforcoffee.presenter.*;
import ch.liip.timeforcoffee.wear.DataService;
import ch.liip.timeforcoffee.zvv.ConnectionsResponse;
import ch.liip.timeforcoffee.zvv.ZvvService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import javax.inject.Singleton;

@Module(
        injects = {
                DataService.class,
                MainPresenter.class,
                DeparturesPresenter.class,
                StationSearchPresenter.class,
                ConnectionsPresenter.class,
                StationListFragment.class,
                DepartureListFragment.class,
                FavoritesListFragment.class
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
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://transport.opendata.ch")
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(TransportService.class);
    }

    @Provides
    @Singleton
    ZvvService provideZvvService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(ConnectionsResponse.class, new ConnectionsDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://tfc.chregu.tv")
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(ZvvService.class);
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
    StationService provideStationService(EventBus eventBus) {
        return new StationService(eventBus);
    }

    @Provides
    @Singleton
    DepartureService provideDepartureService(EventBus eventBus) {
        return new DepartureService(eventBus);
    }

    @Provides
    @Singleton
    ConnectionService provideConnectionService(EventBus eventBus) {
        return new ConnectionService(eventBus);
    }

    @Provides
    @Singleton
    FavoritesDataSource provideFavoriteDataSource() {
        return new FavoritesDataSource();
    }
}
