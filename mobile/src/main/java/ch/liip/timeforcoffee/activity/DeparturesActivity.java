package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.StationMapFragment;
import ch.liip.timeforcoffee.presenter.DeparturesPresenter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class DeparturesActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener, DepartureListFragment.Callbacks {

    private SlidingUpPanelLayout mSlidingLayout;
    private StationMapFragment mStationMapFragment;

    private DeparturesPresenter mPresenter;

    public static final String ARG_FROM_SEARCH = "from_search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            //we don't want to restore this activity => go back to default activity
            navigateUpTo(new Intent(this, MainActivity.class));
        }

        setContentView(R.layout.activity_departure_list);

        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);
        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra(DepartureListFragment.ARG_STATION_ID);
        String name = getIntent().getStringExtra(DepartureListFragment.ARG__STATION_NAME);
        float distance = getIntent().getFloatExtra(DepartureListFragment.ARG_STATION_DISTANCE, 0.0f);

        Location loc = new Location("reverseGeocoded");
        loc.setLatitude(getIntent().getDoubleExtra(DepartureListFragment.ARG_STATION_LATITUDE, 0.0));
        loc.setLongitude(getIntent().getDoubleExtra(DepartureListFragment.ARG_STATION_LONGITUDE, 0.0));

        boolean isFavorite = getIntent().getBooleanExtra(DepartureListFragment.ARG_IS_FAVORITE, false);

        Station station = new Station(id, name, distance, loc, false);
        station.setIsFavorite(isFavorite);
        mStationMapFragment.setStation(station);

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
    public void onPanelSlide(View panel, float slideOffset) {
        mStationMapFragment.updateGradientOverlay(1 - slideOffset, mSlidingLayout.getLayoutParams().height);
    }

    @Override
    public void onPanelExpanded(View panel) {
    }

    @Override
    public void onPanelCollapsed(View panel) {
    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }
}
