package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.fragment.StationSearchFragment;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.StationsSearchFetchedEvent;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class StationSearchPresenter implements Presenter {

    private StationSearchFragment mFragment;
    private List<Station> mStations;
    private String mSearchQuery;

    @Inject
    EventBus mEventBus;

    @Inject
    StationService stationService;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public StationSearchPresenter(StationSearchFragment fragment, String searchQuery) {
        mFragment = fragment;
        mSearchQuery = searchQuery;

        ((TimeForCoffeeApplication) fragment.getActivity().getApplication()).inject(this);
        mEventBus.register(this);
    }

    @Override
    public void onResumeView() { }

    @Override
    public void onRefreshView() {
        search();
    }

    @Override
    public void onPauseView() { }

    @Override
    public void onDestroy() {
        mFragment = null;
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onStationsFetchedEvent(StationsSearchFetchedEvent event) {
        mFragment.setIsSearchLoading(false);
        mStations = event.getStations();

        List<Station> favoriteStations = favoritesDataSource.getAllFavoriteStations(mFragment.getContext());
        for(Station station : mStations) {
            station.setIsFavorite(favoriteStations.contains(station));
        }

        mFragment.updateStations(mStations);
    }

    @Subscribe
    public void onFetchErrorEvent(FetchStationsSearchErrorEvent event) {
        mFragment.setIsSearchLoading(false);
        SnackBars.showNetworkError(mFragment.getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    public void setSearchQuery(String searchQuery) {
        mSearchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public void search() {
        if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
            mFragment.setIsSearchLoading(true);
            mEventBus.post(new FetchStationsSearchEvent(mSearchQuery));
        }
    }

    public void updateStationIsFavorite(Station station, boolean isFavorite) {
        if (isFavorite) {
            favoritesDataSource.insertFavoriteStation(mFragment.getContext(), station);
        }
        else {
            favoritesDataSource.deleteFavoriteStation(mFragment.getContext(), station);
        }
    }

    public void clear() {
        if (mStations != null) {
            mStations.clear();
            mFragment.updateStations(mStations);
        }
    }
}
