package ch.liip.timeforcoffee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;

import java.util.ArrayList;
import java.util.List;

public class DepartureListFragment extends Fragment {

    private Station mStation;

    private ProgressBar mProgressBar;
    private TextView mNoResultsTextView;
    private TextView mTitleTextView;
    private ListView mListView;
    private DepartureListAdapter mAdapter;

    private List<Departure> mDepartures = new ArrayList<>();

    public DepartureListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departure_list, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mNoResultsTextView = view.findViewById(R.id.noResults);
        mTitleTextView = view.findViewById(R.id.title);
        mListView = view.findViewById(R.id.list_view);

        mAdapter = new DepartureListAdapter(getActivity(), mDepartures);
        mListView.setAdapter(mAdapter);

        return view;
    }

    public void setStation(Station station) {
        mStation = station;
        mTitleTextView.setText(station.getName());
    }

    public Station getStation() {
        return mStation;
    }

    public void setDepartures(List<Departure> departures) {
        mDepartures = departures;
    }

    public void displayDepartures() {
        mProgressBar.setVisibility(View.GONE);
        mNoResultsTextView.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);

        if (mAdapter != null && mDepartures != null) {
            mAdapter.setDepartures(mDepartures);
        }
    }

    public void displayProgressIndicator() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoResultsTextView.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }

    public void displayNoResults() {
        mProgressBar.setVisibility(View.GONE);
        mNoResultsTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }
}
