package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.ConnectionsActivity;
import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.api.ConnectionService;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.events.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class ConnectionsPresenter implements Presenter {

    private ConnectionsActivity mActivity;
    private Timer mAutoUpdateTimer;
    public static final int UPDATE_FREQUENCY = 60000;

    private Station mStation;
    private Departure mDeparture;
    private List<Connection> mConnections;
    private FavoritesDataSource mFavoriteDataSource;

    @Inject
    EventBus mEventBus;

    @Inject
    ConnectionService connectionService;

    public ConnectionsPresenter(ConnectionsActivity activity, Station station, Departure departure) {
        mActivity = activity;
        mStation = station;
        mDeparture = departure;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

        mFavoriteDataSource = new FavoritesDataSource(activity);
        mFavoriteDataSource.open();
    }

    public void onResumeView() {
        updateConnections();
    }

    public void onRefreshView() {
        updateConnections();
    }

    public void updateConnections() {
        if (mConnections == null || mConnections.size() == 0) {
            mActivity.showProgressLayout(true);
        }

        mEventBus.post(new FetchConnectionsEvent(mStation.getName(), mDeparture.getDestination()));
    }

    @Subscribe
    public void onConnectionsFetchedEvent(ConnectionsFetchedEvent event) {
        mActivity.showProgressLayout(false);

        mConnections = event.getConnections();
        mActivity.updateConnections(mConnections);
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mActivity.showProgressLayout(false);
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

    public Station getStation() {
        return mStation;
    }

    public Departure getDeparture() {
        return mDeparture;
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

    public FavoritesDataSource getFavoritesDataSource() {
        return mFavoriteDataSource;
    }
}
