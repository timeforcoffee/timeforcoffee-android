package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;
import java.util.Vector;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.TabsAdapter;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements StationListFragment.Callbacks, FavoritesListFragment.Callbacks {

    private MainPresenter mPresenter;
    private StationListFragment mStationListFragment;
    private FavoritesListFragment mFavoriteListFragment;

    public static final String STATION_LIST_FRAGMENT_KEY = "station_list";
    public static final String FAVORITE_LIST_FRAGMENT_KEY = "favorite_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        // Presenter
        mPresenter = new MainPresenter(this);

        // Fragments
        Bundle favoritesFragmentArgs = new Bundle();
        favoritesFragmentArgs.putInt(FavoritesListFragment.ARG_MODE, FavoritesListFragment.ARG_MODE_STATIONS);

        Bundle stationsFragmentArgs = new Bundle();
        stationsFragmentArgs.putBoolean(StationListFragment.ARG_SEARCH_MODE, false);

        if (savedInstanceState == null) {
            mStationListFragment = (StationListFragment) Fragment.instantiate(this, StationListFragment.class.getName(), stationsFragmentArgs);
            mFavoriteListFragment = (FavoritesListFragment) Fragment.instantiate(this, FavoritesListFragment.class.getName(), favoritesFragmentArgs);
        } else{
            mStationListFragment = (StationListFragment)getSupportFragmentManager().getFragment(savedInstanceState, STATION_LIST_FRAGMENT_KEY);
            mFavoriteListFragment = (FavoritesListFragment)getSupportFragmentManager().getFragment(savedInstanceState, FAVORITE_LIST_FRAGMENT_KEY);
        }

        List fragments = new Vector();
        fragments.add(mStationListFragment);
        fragments.add(mFavoriteListFragment);

        // Initialize the ViewPager and bind to tabs
        TabsAdapter pagerAdapter = new TabsAdapter(this, super.getSupportFragmentManager(), new int[]{ R.string.tab_stations, R.string.tab_favorites }, fragments);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        PagerSlidingTabStrip tabs = findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPresenter.updateFavorites();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mStationListFragment.isAdded() && mFavoriteListFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, STATION_LIST_FRAGMENT_KEY, mStationListFragment);
            getSupportFragmentManager().putFragment(outState, FAVORITE_LIST_FRAGMENT_KEY, mFavoriteListFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumeView();
    }


    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPauseView();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshView();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_search:
                Intent stationSearchIntent = new Intent(this, StationSearchActivity.class);
                startActivity(stationSearchIntent);
                return true;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onFavoriteStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onFavoriteDepartureSelected(Departure departure) { }

    private void selectStation(Station station) {
        Intent detailIntent = new Intent(this, DeparturesActivity.class);
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_ID, station.getId());
        detailIntent.putExtra(DeparturesActivity.ARG__STATION_NAME, station.getName());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_DISTANCE, station.getDistance());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_LATITUDE, station.getLocation().getLatitude());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_LONGITUDE, station.getLocation().getLongitude());
        detailIntent.putExtra(DeparturesActivity.ARG_IS_FAVORITE, station.getIsFavorite());

        startActivity(detailIntent);
    }

    public void performStationsUpdate() {
        mPresenter.updateStations();
    }

    public void updateStations(List<Station> stations) {
        mStationListFragment.setStations(stations);
    }

    public void updateFavorites(List<Station> favoriteStations) {
        mFavoriteListFragment.setStations(favoriteStations);
    }

    public void setIsPositionLoading(boolean loading) {
        mStationListFragment.showLoadingPositionLayout(loading);
    }
}
