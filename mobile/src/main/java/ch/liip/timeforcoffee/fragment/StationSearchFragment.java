package ch.liip.timeforcoffee.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.presenter.StationSearchPresenter;

public class StationSearchFragment extends Fragment implements StationListFragment.Callbacks {

    private StationSearchPresenter mPresenter;
    private StationListFragment mStationListFragment;

    private EditText mSearchEditText;
    private ProgressBar mSearchProgressBar;
    private Handler mSearchHandler;
    private Runnable mSearchRunnable;

    public static final String STATION_LIST_FRAGMENT_KEY = "station_list";
    public static final String SEARCH_QUERY_KEY = "search_query";
    private static final int NUMBER_MIN_OF_CHAR = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_station_list, container, false);

        // Presenter
        String searchQuery = null;
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY);
        }
        mPresenter = new StationSearchPresenter(this, searchQuery);

        // Action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        mSearchEditText = actionBar.getCustomView().findViewById(R.id.searchField);
        mSearchEditText.addTextChangedListener(new SearchWatcher());
        mSearchEditText.setText(searchQuery);
        mSearchEditText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mSearchProgressBar = actionBar.getCustomView().findViewById(R.id.search_progress_bar);
        mSearchProgressBar.setVisibility(View.GONE);

        // Fragment
        Bundle stationsFragmentArgs = new Bundle();
        stationsFragmentArgs.putBoolean(StationListFragment.ARG_SEARCH_MODE, true);

        if (savedInstanceState == null) {
            mStationListFragment = (StationListFragment) Fragment.instantiate(getContext(), StationListFragment.class.getName(), stationsFragmentArgs);
        }
        else{
            mStationListFragment = (StationListFragment) getActivity().getSupportFragmentManager().getFragment(savedInstanceState, STATION_LIST_FRAGMENT_KEY);
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mStationListFragment).commit();

        return rootView;
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSearching();
        mPresenter.onDestroy();
    }

    @Override
    public void onStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onStationFavoriteToggled(Station station, boolean isFavorite) {
        mPresenter.updateStationIsFavorite(station, isFavorite);
    }

    private void selectStation(Station station) {
        Intent detailIntent = new Intent(getContext(), DeparturesActivity.class);
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_ID, station.getId());
        detailIntent.putExtra(DeparturesActivity.ARG__STATION_NAME, station.getName());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_DISTANCE, station.getDistance());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_LATITUDE, station.getLocation().getLatitude());
        detailIntent.putExtra(DeparturesActivity.ARG_STATION_LONGITUDE, station.getLocation().getLongitude());
        detailIntent.putExtra(DeparturesActivity.ARG_IS_FAVORITE, station.getIsFavorite());

        startActivity(detailIntent);
    }

    public void performStationsSearch() {
        mPresenter.search();
    }

    public void updateStations(List<Station> stations) {
        mStationListFragment.setStations(stations);
    }

    public void setIsSearchLoading(boolean loading) {
        if (loading) {
            mSearchProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mSearchProgressBar.setVisibility(View.GONE);
        }
    }

    private void stopSearching() {
        if (mSearchHandler != null && mSearchRunnable != null) {
            mSearchHandler.removeCallbacks(mSearchRunnable);
            mSearchHandler = null;
            mSearchRunnable = null;
        }
    }

    /**
     * Responsible for handling changes in search edit text.
     */
    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) { }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) { }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() >= NUMBER_MIN_OF_CHAR) {
                //Stop current handler since we have a new text entered
                stopSearching();

                //Search after 1 sec. This allow to stop the search is user is still entering text
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
    }
}
