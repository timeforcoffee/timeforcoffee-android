package ch.liip.timeforcoffee.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.DotsPageIndicator;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;

import java.util.List;
import java.util.Vector;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.TabsAdapter;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.MapActionFragment;
import ch.liip.timeforcoffee.fragment.RefreshActionFragment;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.presenter.WearPresenter;
import ch.liip.timeforcoffee.view.FragmentViewPager;

public class WearActivity extends Activity {

    private FragmentViewPager mFragmentViewPager;
    private TabsAdapter mTabsAdapter;
    private DotsPageIndicator mDotsPageIndicator;

    private StationListFragment mStationListFragment;
    private DepartureListFragment mDepartureListFragment;
    private MapActionFragment mMapActionFragment;
    private RefreshActionFragment mRefreshActionFragment;

    private GestureDetector mDetector;
    private DismissOverlayView mDismissOverlay;

    WearPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wear);

        mStationListFragment = (StationListFragment) Fragment.instantiate(this, StationListFragment.class.getName());
        mDepartureListFragment = (DepartureListFragment) Fragment.instantiate(this, DepartureListFragment.class.getName());
        mMapActionFragment = (MapActionFragment) Fragment.instantiate(this, MapActionFragment.class.getName());
        mRefreshActionFragment = (RefreshActionFragment) Fragment.instantiate(this, RefreshActionFragment.class.getName());

        // Initialize the ViewPager and set an adapter
        List fragments = new Vector();
        fragments.add(mDepartureListFragment);
        fragments.add(mStationListFragment);
        fragments.add(mMapActionFragment);
        fragments.add(mRefreshActionFragment);

        mTabsAdapter = new TabsAdapter(getFragmentManager(), fragments);
        mFragmentViewPager = (FragmentViewPager) findViewById(R.id.gridViewPager);
        mFragmentViewPager.setAdapter(mTabsAdapter);

        mDotsPageIndicator = (DotsPageIndicator) findViewById(R.id.dotsPageIndicator);
        mDotsPageIndicator.setPager(mFragmentViewPager);
        mDotsPageIndicator.setDotColorSelected(getResources().getColor(R.color.black));
        mDotsPageIndicator.setDotColor(getResources().getColor(R.color.black));

        // Set up the DismissOverlayView
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(getString(R.string.basic_wear_long_press_intro));
        mDismissOverlay.showIntroIfNecessary();
        // Configure a gesture detector
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                mDismissOverlay.show();
            }
        });

        mPresenter = new WearPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResumeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPauseView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mPresenter.PLAY_SERVICE_RESOLUTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPresenter.onResumeView();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event) || super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public void selectStation(Station station) {
        mDepartureListFragment.displayProgressIndicator();
        mDepartureListFragment.setStation(station);
        mFragmentViewPager.setCurrentItem(0, 0);

        mPresenter.selectStation(station);
    }

    public void refresh() {
        mFragmentViewPager.setCurrentItem(0, 0, true);
        mPresenter.onRefreshView();
    }

    public void setDepartures(final List<Departure> departures) {
        runOnUiThread(new Runnable() {
            public void run() {
                mDepartureListFragment.setDepartures(departures);
                mDepartureListFragment.displayDepartures();
            }
        });
    }

    public void setStations(final List<Station> stations) {
        runOnUiThread(new Runnable() {
            public void run() {
                mStationListFragment.setStations(stations);
                mStationListFragment.displayStations();
            }
        });
    }

    public void setStation(final Station station) {
        runOnUiThread(new Runnable() {
            public void run() {
                mDepartureListFragment.setStation(station);
            }
        });
    }

    public void displayMap() {
        if (mDepartureListFragment.getStation() == null) {
            return;
        }
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.ARG_STATION_LATITUDE, mDepartureListFragment.getStation().getLocation().getLatitude());
        intent.putExtra(MapActivity.ARG_STATION_LONGITUDE, mDepartureListFragment.getStation().getLocation().getLongitude());
        intent.putExtra(MapActivity.ARG_STATION_NAME, mDepartureListFragment.getStation().getName());
        startActivity(intent);
    }

    public void displayRefreshState() {
        mDepartureListFragment.displayProgressIndicator();
        mStationListFragment.displayProgressIndicator();
    }

    public void displayNoResult() {
        mDepartureListFragment.displayNoResults();
        mStationListFragment.displayNoResults();
    }
}
