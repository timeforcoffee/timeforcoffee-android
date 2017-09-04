package ch.liip.timeforcoffee.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.fragment.ConnectionListFragment;
import ch.liip.timeforcoffee.fragment.StationMapFragment;
import ch.liip.timeforcoffee.presenter.ConnectionsPresenter;

public class ConnectionsActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener, ConnectionListFragment.Callbacks {

    public static final String ARG_STATION_ID = "station_id";
    public static final String ARG_STATION_NAME = "station_name";
    public static final String ARG_STATION_DISTANCE = "station_distance";
    public static final String ARG_STATION_LONGITUDE = "station_longitude";
    public static final String ARG_STATION_LATITUDE = "station_latitude";
    public static final String ARG_STATION_IS_FAVORITE = "station_is_favorite";
    public static final String ARG_DEPARTURE_NAME = "departure_name";
    public static final String ARG_DEPARTURE_TYPE = "departure_type";
    public static final String ARG_DEPARTURE_ACCESSIBLE = "departure_accessible";
    public static final String ARG_DEPARTURE_DESTINATION_ID = "departure_destination_id";
    public static final String ARG_DEPARTURE_DESTINATION_NAME = "departure_destination_name";
    public static final String ARG_DEPARTURE_PLATFORM = "departure_platform";
    public static final String ARG_DEPARTURE_DEPARTURE_SCHEDULED = "departure_departure_scheduled";
    public static final String ARG_DEPARTURE_DEPARTURE_REALTIME = "departure_departure_realtime";
    public static final String ARG_DEPARTURE_ARRIVAL_SCHEDULED = "departure_arrival_scheduled";
    public static final String ARG_DEPARTURE_COLOR_FG = "departure_color_fg";
    public static final String ARG_DEPARTURE_COLOR_BG = "departure_color_bg";
    public static final String ARG_DEPARTURE_IS_FAVORITE = "departure_is_favorite";

    private SlidingUpPanelLayout mSlidingLayout;
    private StationMapFragment mStationMapFragment;
    private RelativeLayout mProgressLayout;

    private ConnectionsPresenter mPresenter;
    private ConnectionListFragment mConnectionListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_connection_list);

        mConnectionListFragment = (ConnectionListFragment) getSupportFragmentManager().findFragmentById(R.id.connection_list);
        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);
        mProgressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int stationId = getIntent().getIntExtra(ARG_STATION_ID, 0);
        String stationName = getIntent().getStringExtra(ARG_STATION_NAME);
        float stationDistance = getIntent().getFloatExtra(ARG_STATION_DISTANCE, 0.0f);
        boolean stationIsFavorite = getIntent().getBooleanExtra(ARG_STATION_IS_FAVORITE, false);

        Location stationLoc = new Location("reverseGeocoded");
        stationLoc.setLatitude(getIntent().getDoubleExtra(ARG_STATION_LATITUDE, 0.0));
        stationLoc.setLongitude(getIntent().getDoubleExtra(ARG_STATION_LONGITUDE, 0.0));

        String departureName = getIntent().getStringExtra(ARG_DEPARTURE_NAME);
        String departureType = getIntent().getStringExtra(ARG_DEPARTURE_TYPE);
        boolean departureAccessible = getIntent().getBooleanExtra(ARG_DEPARTURE_ACCESSIBLE, true);
        int departureDestinationId = getIntent().getIntExtra(ARG_DEPARTURE_DESTINATION_ID, 0);
        String departureDestinationName = getIntent().getStringExtra(ARG_DEPARTURE_DESTINATION_NAME);
        String departurePlatform = getIntent().getStringExtra(ARG_DEPARTURE_PLATFORM);
        Date departureScheduled = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_DEPARTURE_SCHEDULED);
        Date departureRealtime = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_DEPARTURE_REALTIME);
        Date arrivalScheduled = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_ARRIVAL_SCHEDULED);
        int departureColorFg = getIntent().getIntExtra(ARG_DEPARTURE_COLOR_FG, 0);
        int departureColorBg = getIntent().getIntExtra(ARG_DEPARTURE_COLOR_BG, 0);
        boolean departureIsFavorite = getIntent().getBooleanExtra(ARG_DEPARTURE_IS_FAVORITE, false);

        Station station = new Station(stationId, stationName, stationDistance, stationLoc, stationIsFavorite);
        Departure departure = new Departure(departureName, departureDestinationId, departureDestinationName, departurePlatform, departureColorFg, departureColorBg,
                departureScheduled, departureRealtime, arrivalScheduled, departureAccessible, departureIsFavorite);

        mStationMapFragment.setup(station, departure, getResources().getString(R.string.connection_from));
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
            onBackPressed();
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

    public void updateConnections(List<Connection> connections) {
        mConnectionListFragment.setConnections(connections);
    }

    @Override
    public void onConnectionSelected(Connection connection) { }

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

    public void showProgressLayout(boolean show) {
        if (show) {
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
        }
    }
}
