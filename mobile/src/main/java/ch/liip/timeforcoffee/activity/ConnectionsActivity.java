package ch.liip.timeforcoffee.activity;

import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Connection;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.fragment.ConnectionListFragment;
import ch.liip.timeforcoffee.fragment.StationMapFragment;
import ch.liip.timeforcoffee.presenter.ConnectionsPresenter;

public class ConnectionsActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener, StationMapFragment.Callbacks, ConnectionListFragment.Callbacks {

    private SlidingUpPanelLayout mSlidingLayout;
    private StationMapFragment mStationMapFragment;
    private RelativeLayout mProgressLayout;

    private ConnectionsPresenter mPresenter;
    private ConnectionListFragment mConnectionListFragment;

    public static final String CONNECTION_LIST_FRAGMENT_KEY = "connection_list";

    public static final String ARG_STATION_ID = "station_id";
    public static final String ARG_STATION_NAME = "station_name";
    public static final String ARG_STATION_DISTANCE = "station_distance";
    public static final String ARG_STATION_LONGITUDE = "station_longitude";
    public static final String ARG_STATION_LATITUDE = "station_latitude";
    public static final String ARG_STATION_IS_FAVORITE = "station_is_favorite";
    public static final String ARG_DEPARTURE_DESTINATION_ID = "departure_destination_id";
    public static final String ARG_DEPARTURE_DESTINATION_NAME = "departure_destination_name";
    public static final String ARG_DEPARTURE_DEPARTURE_SCHEDULED = "departure_departure_scheduled";
    public static final String ARG_DEPARTURE_DEPARTURE_REALTIME = "departure_departure_realtime";
    public static final String ARG_DEPARTURE_ARRIVAL_SCHEDULED = "departure_arrival_scheduled";
    public static final String ARG_DEPARTURE_ARRIVAL_REALTIME = "departure_arrival_scheduled";
    public static final String ARG_DEPARTURE_LINE = "departure_line";
    public static final String ARG_DEPARTURE_PLATFORM = "departure_platform";
    public static final String ARG_DEPARTURE_COLOR_FG = "departure_color_fg";
    public static final String ARG_DEPARTURE_COLOR_BG = "departure_color_bg";
    public static final String ARG_DEPARTURE_ACCESSIBLE = "departure_accessible";
    public static final String ARG_DEPARTURE_IS_FAVORITE = "departure_is_favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_list);

        // Presenter
        int stationId = getIntent().getIntExtra(ARG_STATION_ID, 0);
        String stationName = getIntent().getStringExtra(ARG_STATION_NAME);
        float stationDistance = getIntent().getFloatExtra(ARG_STATION_DISTANCE, 0.0f);
        boolean stationIsFavorite = getIntent().getBooleanExtra(ARG_STATION_IS_FAVORITE, false);

        Location stationLoc = new Location("reverseGeocoded");
        stationLoc.setLatitude(getIntent().getDoubleExtra(ARG_STATION_LATITUDE, 0.0));
        stationLoc.setLongitude(getIntent().getDoubleExtra(ARG_STATION_LONGITUDE, 0.0));

        int departureDestinationId = getIntent().getIntExtra(ARG_DEPARTURE_DESTINATION_ID, 0);
        String departureDestinationName = getIntent().getStringExtra(ARG_DEPARTURE_DESTINATION_NAME);
        Date departureScheduled = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_DEPARTURE_SCHEDULED);
        Date departureRealtime = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_DEPARTURE_REALTIME);
        Date arrivalScheduled = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_ARRIVAL_SCHEDULED);
        Date arrivalRealtime = (Date) getIntent().getSerializableExtra(ARG_DEPARTURE_ARRIVAL_REALTIME);
        String departureLine = getIntent().getStringExtra(ARG_DEPARTURE_LINE);
        String departurePlatform = getIntent().getStringExtra(ARG_DEPARTURE_PLATFORM);
        int departureColorFg = getIntent().getIntExtra(ARG_DEPARTURE_COLOR_FG, 0);
        int departureColorBg = getIntent().getIntExtra(ARG_DEPARTURE_COLOR_BG, 0);
        boolean departureAccessible = getIntent().getBooleanExtra(ARG_DEPARTURE_ACCESSIBLE, true);
        boolean departureIsFavorite = getIntent().getBooleanExtra(ARG_DEPARTURE_IS_FAVORITE, false);

        Station station = new Station(stationId, stationName, stationDistance, stationLoc, stationIsFavorite);
        Departure departure = new Departure(
                departureDestinationId,
                departureDestinationName,
                departureScheduled,
                departureRealtime,
                arrivalScheduled,
                arrivalRealtime,
                departureLine,
                departurePlatform,
                departureColorFg,
                departureColorBg,
                departureAccessible,
                departureIsFavorite
        );

        mPresenter = new ConnectionsPresenter(this, station, departure);

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Fragments
        if (savedInstanceState == null) {
            mConnectionListFragment = (ConnectionListFragment) Fragment.instantiate(this, ConnectionListFragment.class.getName());
        }
        else {
            mConnectionListFragment = (ConnectionListFragment) getSupportFragmentManager().getFragment(savedInstanceState, CONNECTION_LIST_FRAGMENT_KEY);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mConnectionListFragment)
                .commit();

        // View
        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);

        mProgressLayout = findViewById(R.id.progressLayout);
        mSlidingLayout = findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);
        mSlidingLayout.setTouchEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mConnectionListFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, CONNECTION_LIST_FRAGMENT_KEY, mConnectionListFragment);
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

    @Override
    public void onMapLoaded() {
        mSlidingLayout.setTouchEnabled(true);
    }

    public void performConnectionsUpdate() {
        mPresenter.updateConnections();
    }

    public void updateConnections(List<Connection> connections) {
        mStationMapFragment.setup(connections);
        mConnectionListFragment.setConnections(connections);
    }

    public void showProgressLayout(boolean show) {
        if (show) {
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
        }
    }
}
