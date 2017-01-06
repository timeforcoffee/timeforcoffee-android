package ch.liip.timeforcoffee.presenter;

import android.view.View;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.ZvvApiService;
import ch.liip.timeforcoffee.api.events.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nicolas on 23/12/16.
 */
public class DeparturesPresenter implements Presenter {

    private DeparturesActivity mActivity;
    Station mStation;
    private Timer mAutoUpdateTimer;
    public static final int UPDATE_FREQUENCY = 60000;

    private FavoritesDataSource mFavoriteDataSource;

    @Inject
    EventBus mEventBus;

    @Inject
    DepartureService departureService;

    @Inject
    ZvvApiService zvvApiService;

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
        mEventBus.post(new FetchDeparturesEvent(mStation.getId()));
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
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

    public boolean getIsFavorite() {
        return mStation.getIsFavorite();
    }

    public void toggleFavorite() {
        if (getIsFavorite()) {
            //Remove from fav
            mStation.setIsFavorite(false);
            mFavoriteDataSource.deleteFavorite(mStation);
        } else {
            //Add to fav
            mStation.setIsFavorite(true);
            mFavoriteDataSource.insertFavorites(mStation);
        }
    }
}
