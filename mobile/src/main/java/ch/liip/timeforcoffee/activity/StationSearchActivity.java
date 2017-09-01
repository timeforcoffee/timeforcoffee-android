package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.presenter.StationSearchPresenter;

public class StationSearchActivity extends AppCompatActivity
        implements StationListFragment.Callbacks {

    StationSearchPresenter mPresenter;
    StationListFragment mStationListFragment;

    private EditText mSearchEditText;
    private ProgressBar mSearchProgressBar;
    private android.os.Handler mSearchHandler;
    private Runnable mSearchRunnable;

    public static final String ARG_SEARCH_QUERY = "search_query";
    final String TAG = "timeforcoffee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String searchQuery = null;
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(ARG_SEARCH_QUERY);
        }
        if (getIntent() != null) {
            searchQuery = getIntent().getStringExtra(ARG_SEARCH_QUERY);
        }

        mPresenter = new StationSearchPresenter(this, searchQuery);
        setContentView(R.layout.activity_search_station_list);

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mStationListFragment = (StationListFragment) getSupportFragmentManager().findFragmentById(R.id.station_list);

        mSearchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.searchField);
        mSearchEditText.addTextChangedListener(new SearchWatcher());
        mSearchEditText.setText(searchQuery);
        mSearchEditText.requestFocus();

        mSearchProgressBar = (ProgressBar) actionBar.getCustomView().findViewById(R.id.search_progress_bar);
        mSearchProgressBar.setVisibility(View.GONE);
    }

    /**
     * Responsible for handling changes in search edit text.
     */
    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            //Stop current handler since we have a new text entered
            if (mSearchHandler != null && mSearchRunnable != null) {
                //Log.i(TAG, "Stop current search");
                mSearchHandler.removeCallbacks(mSearchRunnable);
                mSearchHandler = null;
                mSearchRunnable = null;
            }

            //Search afer 1 sec. This allow to stop the search is user is still entering
            //new letters
            mSearchRunnable = new Runnable() {
                public void run() {
                    mPresenter.setSearchQuery(mSearchEditText.getText().toString());
                    mPresenter.search();
                }
            };
            mSearchHandler = new android.os.Handler();
            mSearchHandler.postDelayed(mSearchRunnable, 1000);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_SEARCH_QUERY, mPresenter.getSearchQuery());
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void updateStations(List<Station> stations) {
        mStationListFragment.setStations(stations);
    }

    public void showProgressLayout(boolean show) {
        if (show) {
            mSearchProgressBar.setVisibility(View.VISIBLE);
        } else {
            mSearchProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshView();
    }

    private void selectStation(Station station) {
        Intent detailIntent = new Intent(this, DeparturesActivity.class);

        detailIntent.putExtra(DeparturesActivity.ARG_STATION_ID, station.getId());
        detailIntent.putExtra(DeparturesActivity.ARG__STATION_NAME, station.getName());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_DISTANCE, station.getDistance());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_LATITUDE, station.getLocation().getLatitude());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_LONGITUDE, station.getLocation().getLongitude());
        detailIntent.putExtra(DeparturesActivity.ARG_FROM_SEARCH, true);
        detailIntent.putExtra(ARG_SEARCH_QUERY, mPresenter.getSearchQuery());

        startActivity(detailIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public FavoritesDataSource getFavoriteDataSource() {
        return mPresenter.getFavoritesDataSource();
    }
}
