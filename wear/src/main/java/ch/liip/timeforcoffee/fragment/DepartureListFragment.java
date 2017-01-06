package ch.liip.timeforcoffee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;

import java.util.ArrayList;
import java.util.List;

public class DepartureListFragment extends Fragment {

    final String TAG = "timeforcoffee";

    private Station mStation;

    private DepartureListAdapter mAdapter;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private TextView mTitleTextView;

    private List<Departure> mDepartures = new ArrayList<>();

    public DepartureListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departure_list, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new DepartureListAdapter(getActivity(), mDepartures);
        mListView.setAdapter(mAdapter);
        mTitleTextView = (TextView) view.findViewById(R.id.title);
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
        mListView.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        if (mAdapter != null && mDepartures != null) {
            mAdapter.setDepartures(mDepartures);
        }
    }

    public void displayProgressIndicator() {
        mListView.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

}
