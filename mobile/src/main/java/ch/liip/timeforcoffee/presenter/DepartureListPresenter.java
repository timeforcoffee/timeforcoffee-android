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
            mFragment.showProgresLayout(true);
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

        // handle this
       /* if (newDepartures.size() == 0) {
            mFragment.showNoStationsLayout(true);
        } else {
            mFragment.showNoStationsLayout(false);
        }

        //look if some of the retrieved stations are store as favorite and filter out stations with same id
        List<Departure> favorites = mFavoriteDataSource.getAllFavoriteLines();
        List<Departure> stationsFiltered = new ArrayList<>();

        for (Departure departure : newDepartures)  {
            if (mDepartures != null && mDepartures.contains(departure)) {
                //Station already in the list => get the old station (avoid to recompute walking distance)
                stationsFiltered.add(mDepartures.get(mDepartures.indexOf(departure)));
            } else {
                //otherwise, add the new station
                stationsFiltered.add(departure);
            }

            //update favorite state // handle this for a line
            departure.setIsFavorite(favorites.contains(departure));

        }

        mDepartures = stationsFiltered;
        mFragment.setDepartures(mDepartures);*/

        mDepartures = newDepartures;
        mFragment.setDepartures(newDepartures);
    }

    public void updateFavorites() {
        if (mDepartures == null) {
            return;
        }

        /*List<Departure> favorites = mFavoriteDataSource.getAllFavoriteLines(); // probably need to handle a line model
        for (Departure departure : mDepartures) {
            //update favorite state // handle this for a line
            departure.setIsFavorite(favorites.contains(departure));
        }*/

        mFragment.setDepartures(mDepartures);
    }

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        mFragment.showProgresLayout(false);
        updateDepartures(event.getDepartures());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mFragment.showProgresLayout(false);
    }

    public FavoritesDataSource getFavoritesDataSource() {
        return mFavoriteDataSource;
    }
}
