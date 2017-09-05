package ch.liip.timeforcoffee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import ch.liip.timeforcoffee.api.ConnectionService;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.StationService;
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
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://frozen-journey-28246.herokuapp.com")
                .setConverter(new GsonConverter(gson))
                .build();

        BackendService service = restAdapter.create(BackendService.class);
        return service;
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
