package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.ConnectionsActivity;
import ch.liip.timeforcoffee.api.ConnectionService;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.events.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class ConnectionsPresenter implements Presenter {

    private ConnectionsActivity mActivity;
    private Departure mDeparture;
    private Timer mAutoUpdateTimer;
    public static final int UPDATE_FREQUENCY = 60000;

    private FavoritesDataSource mFavoriteDataSource;

    @Inject
    EventBus mEventBus;

    @Inject
    ConnectionService connectionService;

    public ConnectionsPresenter(ConnectionsActivity activity, Departure departure) {
        mActivity = activity;
        mDeparture = departure;

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
                        updateConnections();
                    }
                });
            }
        }, 0, UPDATE_FREQUENCY);

    }

    public void onRefreshView() {
        updateConnections();
    }

    public void updateConnections() {
        mEventBus.post(new FetchConnectionsEvent(mDeparture));
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConnections();
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
        return mDeparture.getIsFavorite();
    }

    public void toggleFavorite() {
        if (getIsFavorite()) {
            //Remove from fav
            mDeparture.setIsFavorite(false);
            mFavoriteDataSource.deleteFavoriteLine(mDeparture);
        } else {
            //Add to fav
            mDeparture.setIsFavorite(true);
            mFavoriteDataSource.insertFavoriteLine(mDeparture);
        }
    }
}
