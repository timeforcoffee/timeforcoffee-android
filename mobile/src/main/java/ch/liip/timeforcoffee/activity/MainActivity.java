package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
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
    private BottomNavigationView mBottomNav;
    private int menuItemSelected;

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

        mStationListFragment = (StationListFragment) Fragment.instantiate(this, StationListFragment.class.getName(), stationsFragmentArgs);
        mFavoriteListFragment = (FavoritesListFragment) Fragment.instantiate(this, FavoritesListFragment.class.getName(), favoritesFragmentArgs);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFavoriteListFragment).commit();

        // Bottom navigation
        mBottomNav = findViewById(R.id.bottom_navigation);
        mBottomNav.setOnNavigationItemSelectedListener(navListener);

        menuItemSelected = R.id.action_stations;
        mBottomNav.setSelectedItemId(menuItemSelected);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBottomNav.setSelectedItemId(menuItemSelected);
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

    public void displayStationList() {
        //
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            Intent stationSearchIntent = null;

            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    stationSearchIntent = new Intent(getApplicationContext(), StationSearchActivity.class);
                    break;
                case R.id.action_stations:
                    menuItemSelected = R.id.action_stations;
                    selectedFragment = mStationListFragment;
                    break;
                case R.id.action_favorites:
                    menuItemSelected = R.id.action_favorites;
                    selectedFragment = mFavoriteListFragment;
                    break;
                case R.id.action_about:
                    menuItemSelected = R.id.action_about;
                    selectedFragment = new AboutFragment();
            }

            if (selectedFragment == null) {
                startActivity(stationSearchIntent);
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        }
    };
}
