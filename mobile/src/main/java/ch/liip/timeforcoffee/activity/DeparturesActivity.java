package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.fragment.StationMapFragment;
import ch.liip.timeforcoffee.presenter.DeparturesPresenter;

public class DeparturesActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener, StationMapFragment.Callbacks, FavoritesListFragment.Callbacks, DepartureListFragment.Callbacks {

    private SlidingUpPanelLayout mSlidingLayout;
    private StationMapFragment mStationMapFragment;

    private DeparturesPresenter mPresenter;
    private DepartureListFragment mDepartureListFragment;
    private FavoritesListFragment mFavoriteListFragment;

    public static final String DEPARTURE_LIST_FRAGMENT_KEY = "departure_list";
    public static final String FAVORITE_LIST_FRAGMENT_KEY = "favorite_list";

    public static final String ARG_STATION_ID = "station_id";
    public static final String ARG__STATION_NAME = "station_name";
    public static final String ARG_STATION_DISTANCE = "station_distance";
    public static final String ARG_STATION_LONGITUDE = "station_longitude";
    public static final String ARG_STATION_LATITUDE = "station_latitude";
    public static final String ARG_IS_FAVORITE = "station_is_favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_list);

        // Presenter
        int id = getIntent().getIntExtra(ARG_STATION_ID, 0);
        String name = getIntent().getStringExtra(ARG__STATION_NAME);
        float distance = getIntent().getFloatExtra(ARG_STATION_DISTANCE, 0.0f);
        boolean isFavorite = getIntent().getBooleanExtra(ARG_IS_FAVORITE, false);

        Location loc = new Location("reverseGeocoded");
        loc.setLatitude(getIntent().getDoubleExtra(ARG_STATION_LATITUDE, 0.0));
        loc.setLongitude(getIntent().getDoubleExtra(ARG_STATION_LONGITUDE, 0.0));

        Station station = new Station(id, name, distance, loc, false);
        station.setIsFavorite(isFavorite);

        mPresenter = new DeparturesPresenter(this, station);

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Fragments
        Bundle favoritesFragmentArgs = new Bundle();
        favoritesFragmentArgs.putInt(FavoritesListFragment.ARG_MODE, FavoritesListFragment.ARG_MODE_DEPARTURES);

        if (savedInstanceState == null) {
            mDepartureListFragment = (DepartureListFragment) Fragment.instantiate(this, DepartureListFragment.class.getName());
            mFavoriteListFragment = (FavoritesListFragment)  Fragment.instantiate(this, FavoritesListFragment.class.getName(), favoritesFragmentArgs);
        }
        else{
            mDepartureListFragment = (DepartureListFragment) getSupportFragmentManager().getFragment(savedInstanceState, DEPARTURE_LIST_FRAGMENT_KEY);
            mFavoriteListFragment = (FavoritesListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FAVORITE_LIST_FRAGMENT_KEY);
        }

        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);
        mStationMapFragment.setup(station);

        mSlidingLayout = findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);
        mSlidingLayout.setTouchEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mDepartureListFragment.isAdded() && mFavoriteListFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, DEPARTURE_LIST_FRAGMENT_KEY, mDepartureListFragment);
            getSupportFragmentManager().putFragment(outState, FAVORITE_LIST_FRAGMENT_KEY, mFavoriteListFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumeView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPauseView();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        mStationMapFragment.updateGradientOverlay(1 - slideOffset, mSlidingLayout.getLayoutParams().height);
    }

    @Override
    public void onPanelExpanded(View panel) { }

    @Override
    public void onPanelCollapsed(View panel) { }

    @Override
    public void onPanelAnchored(View panel) { }

    @Override
    public void onPanelHidden(View panel) { }

    @Override
    public void onMapLoaded() {
        mSlidingLayout.setTouchEnabled(true);
    }

    @Override
    public void onDepartureSelected(Departure departure) {
        selectDeparture(departure);
    }

    @Override
    public void onFavoriteDepartureSelected(Departure departure) {
        selectDeparture(departure);
    }

    @Override
    public void onFavoriteStationSelected(Station station) { }

    @Override
    public void onStationFavoriteToggled(Station station, boolean isFavorite) { }

    @Override
    public void onDepartureFavoriteToggled(Departure departure, boolean isFavorite) {
        mPresenter.updateDepartureIsFavorite(departure, isFavorite);
    }

    private void selectDeparture(Departure departure) {
        Intent detailIntent = new Intent(this, ConnectionsActivity.class);
        Station station = mPresenter.getStation();

        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_ID, station.getId());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_NAME, station.getName());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_DISTANCE, station.getDistance());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_LATITUDE, station.getLocation().getLatitude());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_LONGITUDE, station.getLocation().getLongitude());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_IS_FAVORITE, station.getIsFavorite());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DESTINATION_ID, departure.getDestinationId());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DESTINATION_NAME, departure.getDestinationName());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DEPARTURE_SCHEDULED, departure.getDepartureScheduled());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DEPARTURE_REALTIME, departure.getDepartureRealtime());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_ARRIVAL_SCHEDULED, departure.getArrivalScheduled());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_ARRIVAL_REALTIME, departure.getArrivalRealtime());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_LINE, departure.getLine());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_PLATFORM, departure.getPlatform());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_COLOR_FG, departure.getColorFg());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_COLOR_BG, departure.getColorBg());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_ACCESSIBLE, departure.isAccessible());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_IS_FAVORITE, departure.getIsFavorite());

        startActivity(detailIntent);
    }

    public void performDepartureUpdate() {
        mPresenter.updateDepartures();
    }

    public void updateDepartures(List<Departure> departures) {
        mDepartureListFragment.setDepartures(departures);
    }

    public void updateFavorites(List<Departure> favoriteDepartures) {
        mFavoriteListFragment.setDepartures(favoriteDepartures);
    }

    public void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setAreDeparturesLoading(boolean loading) {
        mDepartureListFragment.showLoadingDeparturesProgressBar(loading);
    }
}
