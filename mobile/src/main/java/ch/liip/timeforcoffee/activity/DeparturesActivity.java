package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;
import java.util.Vector;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.TabsAdapter;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.fragment.StationMapFragment;
import ch.liip.timeforcoffee.presenter.DeparturesPresenter;

public class DeparturesActivity extends AppCompatActivity implements
        SlidingUpPanelLayout.PanelSlideListener, FavoritesListFragment.Callbacks, DepartureListFragment.Callbacks {

    public static final String ARG_STATION_ID = "station_id";
    public static final String ARG__STATION_NAME = "station_name";
    public static final String ARG_STATION_DISTANCE = "station_distance";
    public static final String ARG_STATION_LONGITUDE = "station_longitude";
    public static final String ARG_STATION_LATITUDE = "station_latitude";
    public static final String ARG_IS_FAVORITE = "station_is_favorite";

    private SlidingUpPanelLayout mSlidingLayout;
    private StationMapFragment mStationMapFragment;
    private TabsAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mProgressLayout;

    private DeparturesPresenter mPresenter;
    private DepartureListFragment mDepartureListFragment;
    private FavoritesListFragment mFavoriteListFragment;

    public static final String ARG_FROM_SEARCH = "from_search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            //we don't want to restore this activity => go back to default activity
            navigateUpTo(new Intent(this, MainActivity.class));
        }

        setContentView(R.layout.activity_departure_list);

        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);
        mProgressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(ARG_STATION_ID, 0);
        String name = getIntent().getStringExtra(ARG__STATION_NAME);
        float distance = getIntent().getFloatExtra(ARG_STATION_DISTANCE, 0.0f);

        Location loc = new Location("reverseGeocoded");
        loc.setLatitude(getIntent().getDoubleExtra(ARG_STATION_LATITUDE, 0.0));
        loc.setLongitude(getIntent().getDoubleExtra(ARG_STATION_LONGITUDE, 0.0));

        boolean isFavorite = getIntent().getBooleanExtra(ARG_IS_FAVORITE, false);

        Station station = new Station(id, name, distance, loc, false);
        station.setIsFavorite(isFavorite);
        mStationMapFragment.setup(station);

        // Initialize the ViewPager and set an adapter
        Bundle favoritesFragmentArgs = new Bundle();
        favoritesFragmentArgs.putInt(FavoritesListFragment.ARG_MODE, FavoritesListFragment.ARG_MODE_DEPARTURES);

        mDepartureListFragment = (DepartureListFragment) Fragment.instantiate(this, DepartureListFragment.class.getName());
        mFavoriteListFragment = (FavoritesListFragment)  Fragment.instantiate(this, FavoritesListFragment.class.getName(), favoritesFragmentArgs);

        List fragments = new Vector();
        fragments.add(mDepartureListFragment);
        fragments.add(mFavoriteListFragment);

        mPagerAdapter = new TabsAdapter(this, super.getSupportFragmentManager(), new int[]{ R.string.tab_departures, R.string.tab_favorites }, fragments);
        mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPresenter.updateFavorites();
            }
        });

        mPresenter = new DeparturesPresenter(this, station);
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

    public void updateDepartures(List<Departure> departures) {
        mDepartureListFragment.setDepartures(departures);
    }

    public void updateFavorites(List<Departure> favoriteDepartures) {
        mFavoriteListFragment.setDepartures(favoriteDepartures);
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

    private void selectDeparture(Departure departure) {
        Intent detailIntent = new Intent(this, ConnectionsActivity.class);
        Station station = mPresenter.getStation();

        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_ID, station.getId());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_NAME, station.getName());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_DISTANCE, station.getDistance());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_LATITUDE, station.getLocation().getLatitude());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_LONGITUDE, station.getLocation().getLongitude());
        detailIntent.putExtra(ConnectionsActivity.ARG_STATION_IS_FAVORITE, station.getIsFavorite());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_NAME, departure.getName());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_TYPE, departure.getType());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_ACCESSIBLE, departure.isAccessible());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DESTINATION_ID, departure.getDestinationId());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DESTINATION_NAME, departure.getDestinationName());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_PLATFORM, departure.getPlatform());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_COLOR_FG, departure.getColorFg());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_COLOR_BG, departure.getColorBg());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DEPARTURE_SCHEDULED, departure.getDepartureScheduled());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_DEPARTURE_REALTIME, departure.getDepartureRealtime());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_ARRIVAL_SCHEDULED, departure.getArrivalScheduled());
        detailIntent.putExtra(ConnectionsActivity.ARG_DEPARTURE_IS_FAVORITE, departure.getIsFavorite());

        startActivity(detailIntent);
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

    public void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressLayout(boolean show) {
        if (show) {
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
        }
    }
}
