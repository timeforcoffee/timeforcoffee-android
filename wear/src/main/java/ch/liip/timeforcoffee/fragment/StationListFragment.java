package ch.liip.timeforcoffee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.WearActivity;
import ch.liip.timeforcoffee.adapter.StationListAdapter;
import ch.liip.timeforcoffee.api.Station;

import java.util.ArrayList;
import java.util.List;

public class StationListFragment extends Fragment implements WearableListView.ClickListener {

    final String TAG = "timeforcoffee";

    private StationListAdapter mAdapter;
    private ProgressBar mProgressBar;
    private WearableListView mListView;
    private TextView mTitleTextView;

    private List<Station> mStations = new ArrayList<>();

    public StationListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_list, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mListView = (WearableListView) view.findViewById(R.id.list_view);

        mAdapter = new StationListAdapter(getActivity(), mStations);
        mListView.setAdapter(mAdapter);
        mListView.setClickListener(this);

        mTitleTextView = (TextView) view.findViewById(R.id.title);

        return view;
    }

    public void setStations(List<Station> stations) {
        mStations = stations;
    }

    public void displayStations() {

        mListView.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        if (mAdapter != null && mStations != null) {
            mAdapter.setStations(mStations);
        }
    }

    public void displayProgressIndicator() {

        mListView.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        int position = viewHolder.getPosition();
        Station station = mStations.get(position);
        ((WearActivity) getActivity()).selectStation(station);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
