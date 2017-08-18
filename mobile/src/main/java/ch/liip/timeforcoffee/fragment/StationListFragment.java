package ch.liip.timeforcoffee.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.MainActivity;
import ch.liip.timeforcoffee.adapter.StationListAdapter;
import ch.liip.timeforcoffee.api.Station;


public class StationListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private StationListAdapter mStationListAdapter;
    private RelativeLayout mNoStationsLayout;
    private RelativeLayout mEnterSearchLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String ARG_SEARCH_MODE = "search_mode";

    private Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks {
        void onStationSelected(Station station);
        void onRefresh();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onStationSelected(Station station) {
        }

        @Override
        public void onRefresh() {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean searchMode = getActivity().getIntent().getBooleanExtra(ARG_SEARCH_MODE, false);

        mStationListAdapter = new StationListAdapter(getActivity(), new ArrayList<Station>(), ((MainActivity)getActivity()).getFavoriteDataSource());
        setListAdapter(mStationListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_list, container, false);

        mNoStationsLayout = (RelativeLayout) rootView.findViewById(R.id.noStationsLayout);
        mEnterSearchLayout = (RelativeLayout) rootView.findViewById(R.id.enterSearchLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) context;
    }

    public void showNoStationsLayout(boolean show) {
        mEnterSearchLayout.setVisibility(View.GONE);
        if (show) {
            mNoStationsLayout.setVisibility(View.VISIBLE);
        } else {
            mNoStationsLayout.setVisibility(View.GONE);
        }
    }

    public void setStations(List<Station> stations) {
        if(stations.size() > 0) {
            showNoStationsLayout(false);
        }

        mEnterSearchLayout.setVisibility(View.GONE);
        mStationListAdapter.setStations(stations);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onStationSelected(mStationListAdapter.getStation(position));
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCallbacks.onRefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 100);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }
}
