package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.ZvvApiService;
import ch.liip.timeforcoffee.api.events.departuresEvents.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesErrorEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class DeparturesPresenter implements Presenter {

    private DeparturesActivity mActivity;
    private Station mStation;
    private List<Departure> mDepartures;
    private List<Departure> mFavoriteDepartures;
    private Timer mAutoUpdateTimer;

    @Inject
    EventBus mEventBus;

    @Inject
    DepartureService departureService;

    @Inject
    ZvvApiService zvvApiService;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public DeparturesPresenter(DeparturesActivity activity, Station station) {
        mActivity = activity;
        mStation = station;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);
    }

    public void onResumeView() { }

    public void onRefreshView() {
        updateDepartures();
    }

    public void onPauseView() {
        if (mAutoUpdateTimer != null) {
            mAutoUpdateTimer.cancel();
            mAutoUpdateTimer = null;
        }
    }

    public void onDestroy() {
        mEventBus.unregister(this);
        mActivity = null;
    }

    public Station getStation() {
        return mStation;
    }

    public boolean getIsFavorite() {
        return mStation.getIsFavorite();
    }

    public void updateDepartures() {
        if (mDepartures == null || mDepartures.size() == 0) {
            mActivity.showProgressLayout(true);
        }

        mEventBus.post(new FetchDeparturesEvent(mStation.getIdStr()));
    }

    public void updateFavorites() {
        if(mDepartures == null || mDepartures.size() == 0) {
            return;
        }

        List<Departure> favoriteLines = favoritesDataSource.getAllFavoriteLines(mActivity);
        List<Departure> favoriteDepartures = new ArrayList<>();
        for(Departure departure : mDepartures) {
            boolean contains = false;
            for(Departure favorite : favoriteLines) {
                if(favorite.lineEquals(departure)) {
                    favoriteDepartures.add(departure);
                    contains = true;
                }
            }

            departure.setIsFavorite(contains);
        }

        mFavoriteDepartures = favoriteDepartures;
        mActivity.updateDepartures(mDepartures);
        mActivity.updateFavorites(mFavoriteDepartures);
    }

    public void toggleFavorite() {
        if (getIsFavorite()) {
            //Remove from fav
            mStation.setIsFavorite(false);
            favoritesDataSource.deleteFavoriteStation(mActivity, mStation);
        } else {
            //Add to fav
            mStation.setIsFavorite(true);
            favoritesDataSource.insertFavoriteStation(mActivity, mStation);
        }
    }

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        mActivity.showProgressLayout(false);

        mDepartures = event.getDepartures();
        mActivity.updateDepartures(mDepartures);

        updateFavorites();
    }

    @Subscribe
    public void onFetchErrorEvent(FetchDeparturesErrorEvent event) {
        mActivity.showProgressLayout(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDepartures();
            }
        });
    }
}
