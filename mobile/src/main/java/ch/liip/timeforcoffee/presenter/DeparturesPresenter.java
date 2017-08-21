package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class DeparturesPresenter implements Presenter {

    private DeparturesActivity mActivity;
    private Timer mAutoUpdateTimer;
    public static final int UPDATE_FREQUENCY = 60000;

    private Station mStation;
    private List<Departure> mDepartures;
    private List<Departure> mFavoriteDepartures;
    private FavoritesDataSource mFavoriteDataSource;

    @Inject
    EventBus mEventBus;

    @Inject
    DepartureService departureService;

    public DeparturesPresenter(DeparturesActivity activity, Station station) {
        mActivity = activity;
        mStation = station;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

        mFavoriteDataSource = new FavoritesDataSource(activity);
        mFavoriteDataSource.open();
    }

    public void onResumeView() {
        //timer to refresh departures each 60 secs
        mAutoUpdateTimer = new Timer();
        mAutoUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        updateDepartures();
                    }
                });
            }
        }, 0, UPDATE_FREQUENCY);

    }

    public void onRefreshView() {
        updateDepartures();
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

        List<Departure> favoriteLines = mFavoriteDataSource.getAllFavoriteLines();
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

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        mActivity.showProgressLayout(false);

        mDepartures = event.getDepartures();
        mActivity.updateDepartures(mDepartures);

        updateFavorites();
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mActivity.showProgressLayout(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDepartures();
            }
        });
    }

    public void onPauseView() {
        if (mAutoUpdateTimer != null) {
            mAutoUpdateTimer.cancel();
            mAutoUpdateTimer = null;
        }
    }

    public void onDestroy() {
        mEventBus.unregister(this);
        mFavoriteDataSource.close();
        mActivity = null;
    }

    public Station getStation() {
        return mStation;
    }

    public boolean getIsFavorite() {
        return mStation.getIsFavorite();
    }

    public void toggleFavorite() {
        if (getIsFavorite()) {
            //Remove from fav
            mStation.setIsFavorite(false);
            mFavoriteDataSource.deleteFavoriteStation(mStation);
        } else {
            //Add to fav
            mStation.setIsFavorite(true);
            mFavoriteDataSource.insertFavoriteStation(mStation);
        }
    }

    public FavoritesDataSource getFavoritesDataSource() {
        return mFavoriteDataSource;
    }
}
