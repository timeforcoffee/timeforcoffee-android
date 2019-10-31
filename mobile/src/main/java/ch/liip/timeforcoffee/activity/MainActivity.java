package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.fragment.AboutFragment;
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

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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
        }
        else{
            mStationListFragment = (StationListFragment)getSupportFragmentManager().getFragment(savedInstanceState, STATION_LIST_FRAGMENT_KEY);
            mFavoriteListFragment = (FavoritesListFragment)getSupportFragmentManager().getFragment(savedInstanceState, FAVORITE_LIST_FRAGMENT_KEY);
        }

        // Bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navLister);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onFavoriteStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onFavoriteDepartureSelected(Departure departure) { }

    @Override
    public void onStationFavoriteToggled(Station station, boolean isFavorite) {
        mPresenter.updateStationIsFavorite(station, isFavorite);
    }

    @Override
    public void onDepartureFavoriteToggled(Departure station, boolean isFavorite) { }

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

    private BottomNavigationView.OnNavigationItemSelectedListener navLister = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    selectedFragment = new Fragment();
                    break;
                case R.id.action_stations:
                    selectedFragment = new Fragment();
                    break;
                case R.id.action_favorites:
                    selectedFragment = new Fragment();
                    break;
                case R.id.action_about:
                    selectedFragment = new AboutFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}