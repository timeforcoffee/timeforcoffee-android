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
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.presenter.ConnectionsPresenter;
import ch.liip.timeforcoffee.presenter.DeparturesPresenter;
import ch.liip.timeforcoffee.presenter.MainPresenter;
import ch.liip.timeforcoffee.presenter.StationSearchPresenter;
import ch.liip.timeforcoffee.wear.DataService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

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
    BackendService provideBackendService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://timeforcoffee-backend.herokuapp.com")
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(BackendService.class);
    }

    @Provides
    @Singleton
    StationService provideStationService(EventBus eventBus, BackendService backendService) {
        return new StationService(eventBus, backendService);
    }

    @Provides
    @Singleton
    DepartureService provideDepartureService(EventBus eventBus, BackendService backendService) {
        return new DepartureService(eventBus, backendService);
    }

    @Provides
    @Singleton
    ConnectionService provideConnectionService(EventBus eventBus, BackendService backendService) {
        return new ConnectionService(eventBus, backendService);
    }

    @Provides
    @Singleton
    FavoritesDataSource provideFavoriteDataSource() {
        return new FavoritesDataSource();
    }
}
