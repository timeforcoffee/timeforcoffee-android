package ch.liip.timeforcoffee.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.ConnectionsActivity;
import ch.liip.timeforcoffee.api.ConnectionService;
import ch.liip.timeforcoffee.api.events.connectionsEvents.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchConnectionsErrorEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.FetchStationsSearchOneEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.StationsSearchOneFetchedEvent;
import ch.liip.timeforcoffee.api.models.Connection;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.SnackBars;

public class ConnectionsPresenter implements Presenter {

    private ConnectionsActivity mActivity;
    private Station mStation;
    private Departure mDeparture;
    private List<Connection> mConnections;

    @Inject
    EventBus mEventBus;

    @Inject
    ConnectionService connectionService;

    @Inject
    BackendService backendService;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public ConnectionsPresenter(ConnectionsActivity activity, Station station, Departure departure) {
        mActivity = activity;
        mStation = station;
        mDeparture = departure;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);
    }

    public void onResumeView() { }

    public void onRefreshView() {
        updateConnections();
    }

    public void onPauseView() { }

    public void onDestroy() {
        mEventBus.unregister(this);
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

    public void updateConnections() {
        if (mConnections == null || mConnections.size() == 0) {
            mActivity.showProgressLayout(true);
        }

        if (mDeparture.getDestinationId() == 0) {
            mEventBus.post(new FetchStationsSearchOneEvent(mDeparture.getDestinationName()));
        }
        else {
            mEventBus.post(new FetchConnectionsEvent(
                    mStation.getId(),
                    mDeparture.getDestinationId(),
                    mDeparture.getDepartureStrForBackend())
            );
        }
    }

    public void toggleFavorite() {
        if (getIsFavorite()) {
            //Remove from fav
            mDeparture.setIsFavorite(false);
            favoritesDataSource.deleteFavoriteLine(mActivity, mDeparture);
        }
        else {
            //Add to fav
            mDeparture.setIsFavorite(true);
            favoritesDataSource.insertFavoriteLine(mActivity, mDeparture);
        }
    }

    @Subscribe
    public void onStationsFetchedEvent(StationsSearchOneFetchedEvent event) {
        int destinationId = event.getStation().getId();
        mDeparture.setDestinationId(destinationId);

        updateConnections();
    }

    @Subscribe
    public void onConnectionsFetchedEvent(ConnectionsFetchedEvent event) {
        mActivity.showProgressLayout(false);

        mConnections = event.getConnections();
        mActivity.updateConnections(mConnections);
    }

    @Subscribe
    public void onFetchErrorEvent(FetchConnectionsErrorEvent event) {
        mActivity.showProgressLayout(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConnections();
            }
        });
    }
}
