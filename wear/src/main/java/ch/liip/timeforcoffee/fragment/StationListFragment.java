package ch.liip.timeforcoffee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.WearActivity;
import ch.liip.timeforcoffee.adapter.StationListAdapter;
import ch.liip.timeforcoffee.api.models.Station;

import java.util.ArrayList;
import java.util.List;

public class StationListFragment extends Fragment implements WearableListView.ClickListener {

    private TextView mNoResultsTextView;
    private ProgressBar mProgressBar;
    private TextView mTitleTextView;

    WearableLinearLayoutManager mLinearLayoutManager;
    private WearableRecyclerView mListView;
    private StationListAdapter mAdapter;

    private List<Station> mStations = new ArrayList<>();

    public StationListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_list, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mNoResultsTextView = view.findViewById(R.id.noResults);
        mTitleTextView = view.findViewById(R.id.title);
        mListView = view.findViewById(R.id.list_view);

        mAdapter = new StationListAdapter(getActivity(), mStations);

        mLinearLayoutManager = new WearableLinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLinearLayoutManager);
        mListView.setEdgeItemsCenteringEnabled(true);
        mListView.setAdapter(mAdapter);
        //mListView.setClickListener(this);

        return view;
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        int position = viewHolder.getPosition();
        Station station = mStations.get(position);
        ((WearActivity) getActivity()).selectStation(station);
    }

    @Override
    public void onTopEmptyRegionClick() { }

    public void setStations(List<Station> stations) {
        mStations = stations;
    }

    public void displayStations() {
        mProgressBar.setVisibility(View.GONE);
        mNoResultsTextView.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);

        if (mAdapter != null && mStations != null) {
            mAdapter.setStations(mStations);
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
