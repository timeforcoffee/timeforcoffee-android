package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Date;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.fragment.ConnectionListFragment;
import ch.liip.timeforcoffee.fragment.StationMapFragment;
import ch.liip.timeforcoffee.presenter.ConnectionsPresenter;

public class ConnectionsActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener, ConnectionListFragment.Callbacks {


    private SlidingUpPanelLayout mSlidingLayout;
    private StationMapFragment mStationMapFragment;
    private ConnectionsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection_list);

        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);
        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String stationId = getIntent().getStringExtra(ConnectionListFragment.ARG_STATION_ID);
        String stationName = getIntent().getStringExtra(ConnectionListFragment.ARG_STATION_NAME);
        float stationDistance = getIntent().getFloatExtra(ConnectionListFragment.ARG_STATION_DISTANCE, 0.0f);
        boolean stationIsFavorite = getIntent().getBooleanExtra(ConnectionListFragment.ARG_STATION_IS_FAVORITE, false);

        Location stationLoc = new Location("reverseGeocoded");
        stationLoc.setLatitude(getIntent().getDoubleExtra(ConnectionListFragment.ARG_STATION_LATITUDE, 0.0));
        stationLoc.setLongitude(getIntent().getDoubleExtra(ConnectionListFragment.ARG_STATION_LONGITUDE, 0.0));

        String departureName = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_NAME);
        String departureType = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_TYPE);
        boolean departureAccessible = getIntent().getBooleanExtra(ConnectionListFragment.ARG_DEPARTURE_ACCESSIBLE, true);
        String departureDestination = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_DESTINATION);
        String departurePlatform = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_PLATFORM);
        Date departureScheduled = (Date) getIntent().getSerializableExtra(ConnectionListFragment.ARG_DEPARTURE_SCHEDULED);
        Date departureRealtime = (Date) getIntent().getSerializableExtra(ConnectionListFragment.ARG_DEPARTURE_REALTIME);
        int departureColorFg = getIntent().getIntExtra(ConnectionListFragment.ARG_DEPARTURE_COLOR_FG, 0);
        int departureColorBg = getIntent().getIntExtra(ConnectionListFragment.ARG_DEPARTURE_COLOR_BG, 0);
        boolean departureIsFavorite = getIntent().getBooleanExtra(ConnectionListFragment.ARG_DEPARTURE_IS_FAVORITE, false);

        Station station = new Station(stationId, stationName, stationDistance, stationLoc, stationIsFavorite);
        Departure departure = new Departure(departureName, departureDestination, departurePlatform, departureColorFg, departureColorBg,
                departureScheduled, departureRealtime, departureAccessible, departureIsFavorite);

        mStationMapFragment.setStation(station);
        mPresenter = new ConnectionsPresenter(this, station, departure);
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fav, menu);
        MenuItem favItem = menu.findItem(R.id.action_fav);

        if (mPresenter.getIsFavorite()) {
            favItem.setIcon(R.drawable.ic_action_star);
        } else {
            favItem.setIcon(R.drawable.ic_action_star_border);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_fav) {
            mPresenter.toggleFavorite();
            if (mPresenter.getIsFavorite()) {
                item.setIcon(R.drawable.ic_action_star);
            } else {
                item.setIcon(R.drawable.ic_action_star_border);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionSelected(Connection connection) {
        // handle nav to departures for the selected connexion
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        mStationMapFragment.updateGradientOverlay(1 - slideOffset, mSlidingLayout.getLayoutParams().height);
    }

    @Override
    public void onPanelCollapsed(View panel) { }

    @Override
    public void onPanelExpanded(View panel) { }

    @Override
    public void onPanelAnchored(View panel) { }

    @Override
    public void onPanelHidden(View panel) { }

}
