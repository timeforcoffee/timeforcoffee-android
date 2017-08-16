package ch.liip.timeforcoffee.presenter;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DepartureListPresenter implements Presenter {

    DepartureListFragment mFragment;

    private List<Departure> mDepartures;
    private FavoritesDataSource mFavoriteDataSource;

    @Inject
    EventBus mEventBus;

    public DepartureListPresenter(DepartureListFragment fragment) {
        mFragment = fragment;

        ((TimeForCoffeeApplication) fragment.getActivity().getApplication()).inject(this);
        mEventBus.register(this);

        mFavoriteDataSource = new FavoritesDataSource(mFragment.getActivity());
        mFavoriteDataSource.open();
    }

    @Override
    public void onResumeView() {
        if (mDepartures != null && mDepartures.size() == 0) {
            mFragment.showProgressLayout(true);
        }
        updateFavorites();
    }

    @Override
    public void onRefreshView() {

    }

    @Override
    public void onPauseView() {

    }

    @Override
    public void onDestroy() {
        mFragment = null;
        mEventBus.unregister(this);
        mFavoriteDataSource.close();
    }

    public void updateDepartures(List<Departure> newDepartures) {
        List<Departure> favorites = mFavoriteDataSource.getAllFavoriteLines();
        for (Departure departure : newDepartures)  {
            departure.setIsFavorite(favorites.contains(departure));
        }

        mDepartures = newDepartures;
        mFragment.setDepartures(newDepartures);
    }

    public void updateFavorites() {
        if (mDepartures == null) {
            return;
        }

        List<Departure> favorites = mFavoriteDataSource.getAllFavoriteLines();
        for (Departure departure : mDepartures) {
            //update favorite state
            departure.setIsFavorite(favorites.contains(departure));
        }

        mFragment.setDepartures(mDepartures);
    }

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        mFragment.showProgressLayout(false);
        updateDepartures(event.getDepartures());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mFragment.showProgressLayout(false);
    }

    public FavoritesDataSource getFavoritesDataSource() {
        return mFavoriteDataSource;
    }
}
