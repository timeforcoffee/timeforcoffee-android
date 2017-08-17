package ch.liip.timeforcoffee.activity;

import android.content.Intent;
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

        setContentView(R.layout.activity_departure_list); // handle xml

        mStationMapFragment = (StationMapFragment) getFragmentManager().findFragmentById(R.id.station_map);
        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // handle args
        String name = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_NAME);
        String type = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_TYPE);
        boolean accessible = getIntent().getBooleanExtra(ConnectionListFragment.ARG_DEPARTURE_ACCESSIBLE, true);
        String destination = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_DESTINATION);
        String platform = getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_PLATFORM);
        Date scheduled = new Date(getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_SCHEDULED));
        Date realtime = new Date(getIntent().getStringExtra(ConnectionListFragment.ARG_DEPARTURE_REALTIME));
        int colorFg = getIntent().getIntExtra(ConnectionListFragment.ARG_DEPARTURE_COLOR_FG, 0);
        int colorBg = getIntent().getIntExtra(ConnectionListFragment.ARG_DEPARTURE_COLOR_BG, 0);
        boolean isFavorite = getIntent().getBooleanExtra(ConnectionListFragment.ARG_DEPARTURE_IS_FAVORITE, false);

        Departure departure = new Departure(name, destination, platform, colorFg, colorBg, scheduled, realtime, accessible, isFavorite);

        // handle station
        // mStationMapFragment.setStation(station);

        mPresenter = new ConnectionsPresenter(this, departure);
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
