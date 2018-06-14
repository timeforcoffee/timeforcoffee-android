package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.events.departuresEvents.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesErrorEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.backend.BackendService;
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
    BackendService backendService;

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

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        mActivity.setAreDeparturesLoading(false);

        mDepartures = event.getDepartures();
        mActivity.updateDepartures(mDepartures);

        updateFavorites();
    }

    @Subscribe
    public void onFetchErrorEvent(FetchDeparturesErrorEvent event) {
        mActivity.setAreDeparturesLoading(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDepartures();
            }
        });
    }

    public Station getStation() {
        return mStation;
    }

    public boolean getStationIsFavorite() {
        return mStation.getIsFavorite();
    }

    public void updateDepartures() {
        if (mDepartures == null || mDepartures.size() == 0) {
            mActivity.setAreDeparturesLoading(true);
        }

        mEventBus.post(new FetchDeparturesEvent(mStation.getId()));
    }

    public void updateFavorites() {
        if(mDepartures == null || mDepartures.size() == 0) {
            return;
        }

        mFavoriteDepartures = getFavorites();

        mActivity.updateDepartures(mDepartures);
        mActivity.updateFavorites(mFavoriteDepartures);
    }

    public void toggleStationIsFavorite() {
        if (mStation.getIsFavorite()) {
            mStation.setIsFavorite(false);
            favoritesDataSource.deleteFavoriteStation(mActivity, mStation);
        }
        else {
            mStation.setIsFavorite(true);
            favoritesDataSource.insertFavoriteStation(mActivity, mStation);
        }
    }

    public void updateDepartureIsFavorite(Departure departure, boolean isFavorite) {
        String action, action1;

        if (isFavorite) {
            favoritesDataSource.insertFavoriteLine(mActivity, departure);
            action = mActivity.getResources().getString(R.string.departure_action_add);
            action1 = mActivity.getResources().getString(R.string.departure_action_add_2);
        }
        else {
            favoritesDataSource.deleteFavoriteLine(mActivity, departure);
            action = mActivity.getResources().getString(R.string.departure_action_remove);
            action1 = mActivity.getResources().getString(R.string.departure_action_remove_2);
        }

        String message = mActivity.getResources().getString(R.string.departure_fav_message);
        String messageFormatted = String.format(message, departure.getLine(), departure.getDestinationName(), action, action1);
        mActivity.displayToastMessage(messageFormatted);

        updateFavoritesOnFavoriteList();
        if(mFavoriteDepartures.size() == 0) {
            mActivity.displayDepartureList();
        }
    }

    private void updateFavoritesOnFavoriteList() {
        mFavoriteDepartures = getFavorites();
        mActivity.updateFavorites(mFavoriteDepartures);
    }

    private List<Departure> getFavorites() {
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

        return favoriteDepartures;
    }
}
