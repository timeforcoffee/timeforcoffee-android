package ch.liip.timeforcoffee.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.api.events.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.fragment.ConnectionListFragment;

public class ConnectionListPresenter implements Presenter {

    ConnectionListFragment mFragment;

    private List<Connection> mConnections;

    @Inject
    EventBus mEventBus;

    public ConnectionListPresenter(ConnectionListFragment fragment) {
        mFragment = fragment;

        ((TimeForCoffeeApplication) fragment.getActivity().getApplication()).inject(this);
        mEventBus.register(this);
    }

    @Override
    public void onResumeView() {
        if (mConnections != null && mConnections.size() == 0) {
            mFragment.showProgressLayout(true);
        }
    }

    @Override
    public void onRefreshView() { }

    @Override
    public void onPauseView() { }

    @Override
    public void onDestroy() {
        mFragment = null;
        mEventBus.unregister(this);
    }

    public void updateConnections(List<Connection> newConnections) {
        mConnections = newConnections;
        mFragment.setConnections(newConnections);
    }

    @Subscribe
    public void onConnectionsFetchedEvent(ConnectionsFetchedEvent event) {
        mFragment.showProgressLayout(false);
        updateConnections(event.getConnections());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mFragment.showProgressLayout(false);
    }

}
