package ch.liip.timeforcoffee.presenter;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Created by nicolas on 02/01/17.
 */
public class DepartureListPresenter implements Presenter {

    DepartureListFragment mFragment;

    @Inject
    EventBus mEventBus;

    public DepartureListPresenter(DepartureListFragment fragment) {
        mFragment = fragment;

        ((TimeForCoffeeApplication) fragment.getActivity().getApplication()).inject(this);
        mEventBus.register(this);

    }

    @Override
    public void onResumeView() {

    }

    @Override
    public void onRefreshView() {

    }

    @Override
    public void onPauseView() {

    }

    @Override
    public void onDestroy() {
        mEventBus.unregister(this);
        mFragment = null;
    }

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        mFragment.showProgresLayout(false);
        mFragment.setDepartures(event.getDepartures());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mFragment.showProgresLayout(false);
    }

}
