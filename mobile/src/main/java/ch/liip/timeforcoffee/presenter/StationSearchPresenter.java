package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.StationSearchActivity;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.ZvvApiService;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.StationsSearchFetchedEvent;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class StationSearchPresenter implements Presenter {

    private StationSearchActivity mActivity;
    private List<Station> mStations;
    private String mSearchQuery;

    @Inject
    EventBus mEventBus;

    @Inject
    StationService stationService;

    @Inject
    ZvvApiService zvvApiService;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public StationSearchPresenter(StationSearchActivity activity, String searchQuery) {
        mActivity = activity;
        mSearchQuery = searchQuery;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);
    }

    @Override
    public void onResumeView() {
        search();
    }

    @Override
    public void onRefreshView() {
        search();
    }

    @Override
    public void onPauseView() { }

    public void search() {
        if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
            mActivity.showProgressLayout(true);
            mEventBus.post(new FetchStationsSearchEvent(mSearchQuery));
        }
    }

    @Subscribe
    public void onStationsFetchedEvent(StationsSearchFetchedEvent event) {
        mActivity.showProgressLayout(false);
        mStations = event.getStations();

        List<Station> favoriteStations = favoritesDataSource.getAllFavoriteStations(mActivity);
        for(Station station : mStations) {
            station.setIsFavorite(favoriteStations.contains(station));
        }

        mActivity.updateStations(mStations);
    }

    @Subscribe
    public void onFetchErrorEvent(FetchStationsSearchErrorEvent event) {
        mActivity.showProgressLayout(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mEventBus.unregister(this);
    }

    public void setSearchQuery(String searchQuery) {
        mSearchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }
}
