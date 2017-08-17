package ch.liip.timeforcoffee.presenter;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.api.events.StationsFetchedEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 02/01/17.
 */
public class StationListPresenter implements Presenter {

    private StationListFragment mFragment;
    private boolean mSearchMode;

    private List<Station> mStations;
    private FavoritesDataSource mFavoriteDataSource;

    @Inject
    EventBus mEventBus;

    public StationListPresenter(StationListFragment fragment, boolean searchMode) {
        mFragment = fragment;
        mSearchMode = searchMode;

        ((TimeForCoffeeApplication) mFragment.getActivity().getApplication()).inject(this);
        mEventBus.register(this);

        mFavoriteDataSource = new FavoritesDataSource(mFragment.getActivity());
        mFavoriteDataSource.open();
    }

    public void onResumeView() {

        if (!mSearchMode) {
            if (mStations != null && mStations.size() == 0) {
                mFragment.showProgressLayout(true);
            }
            updateFavorites();
        }
    }

    public void onRefreshView() {

    }

    @Subscribe
    public void onStationsFetched(StationsFetchedEvent event) {
        mFragment.showProgressLayout(false);
        updateStations(event.getStations());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mFragment.showProgressLayout(false);
    }

    public void updateStations(List<Station> newStations) {

        if (newStations.size() == 0) {
            mFragment.showNoStationsLayout(true);
        } else {
            mFragment.showNoStationsLayout(false);
        }

        //look if some of the retrieved stations are store as favorite and filter out stations with same id
        List<Station> favorites = mFavoriteDataSource.getAllFavoriteStations();
        List<Station> stationsFiltered = new ArrayList<Station>();

        for (Station station : newStations) {
            if (mStations != null && mStations.contains(station)) {
                //Station already in the list => get the old station (avoid to recompute walking distance)
                stationsFiltered.add(mStations.get(mStations.indexOf(station)));
            } else {
                //otherwise, add the new station
                stationsFiltered.add(station);
            }

            for(Station favorite : favorites) {
                if(favorite.equals(station)) {
                    station.setIsFavorite(true);
                }
            }
        }

        mStations = stationsFiltered;
        mFragment.setStations(mStations);
    }

    public void updateFavorites() {
        if (mStations == null) {
            return;
        }

        List<Station> favorites = mFavoriteDataSource.getAllFavoriteStations();
        for (Station station : mStations) {
            for(Station favorite : favorites) {
                if(favorite.equals(station)) {
                    station.setIsFavorite(true);
                }
            }
        }

        mFragment.setStations(mStations);
    }

    public void onPauseView() {

    }

    public void onDestroy() {
        mFragment = null;
        mEventBus.unregister(this);
        mFavoriteDataSource.close();
    }

    public boolean getSearchMode() {
        return mSearchMode;
    }

    public FavoritesDataSource getFavoritesDataSource() {
        return mFavoriteDataSource;
    }

}
