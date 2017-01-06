package ch.liip.timeforcoffee.presenter;

import android.view.View;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.StationSearchActivity;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.ZvvApiService;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.api.events.FetchStationsEvent;
import ch.liip.timeforcoffee.api.events.StationsFetchedEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.widget.SnackBars;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Created by nicolas on 02/01/17.
 */
public class StationSearchPresenter implements Presenter {

    private StationSearchActivity mActivity;
    private String mSearchQuery;

    @Inject
    EventBus mEventBus;

    @Inject
    StationService stationService;

    @Inject
    ZvvApiService zvvApiService;

    public StationSearchPresenter(StationSearchActivity activity, String searchQuery) {
        mActivity = activity;
        mSearchQuery = searchQuery;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

    }

    @Override
    public void onResumeView() {

    }

    public void search() {
        if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
            mActivity.showProgressLayout(true);
            mEventBus.post(new FetchStationsEvent(mSearchQuery));
        }
    }

    @Override
    public void onRefreshView() {
        search();
    }

    @Override
    public void onPauseView() {

    }

    @Subscribe
    public void onStationsFetchedEvent(StationsFetchedEvent event) {
        mActivity.showProgressLayout(false);
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mActivity.showProgressLayout(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mEventBus.unregister(this);
    }

    public void setSearchQuery(String searchQuery) {
        mSearchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }
}
