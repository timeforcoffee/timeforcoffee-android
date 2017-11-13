package ch.liip.timeforcoffee.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.adapter.StationListAdapter;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;


public class FavoritesListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_MODE = "fav_mode";
    public static final int ARG_MODE_STATIONS = 0;
    public static final int ARG_MODE_DEPARTURES = 1;

    private int mFavoriteMode;
    private Callbacks mCallbacks = sDummyCallbacks;

    private StationListAdapter mStationListAdapter;
    private DepartureListAdapter mDepartureListAdapter;
    private LinearLayout mNoFavoritesLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public interface Callbacks {
        void onFavoriteStationSelected(Station station);
        void onFavoriteDepartureSelected(Departure departure);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onFavoriteStationSelected(Station station) {
        }
        @Override
        public void onFavoriteDepartureSelected(Departure departure) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TimeForCoffeeApplication) getActivity().getApplication()).inject(this);

        Bundle args = getArguments();
        if(args == null) {
            return;
        }

        mFavoriteMode = args.getInt(ARG_MODE);
        if(mFavoriteMode == FavoritesListFragment.ARG_MODE_STATIONS) {
            mStationListAdapter = new StationListAdapter(getActivity(), new ArrayList<Station>(), favoritesDataSource);
            setListAdapter(mStationListAdapter);
        } else {
            mDepartureListAdapter = new DepartureListAdapter(getActivity(), new ArrayList<Departure>(), favoritesDataSource);
            setListAdapter(mDepartureListAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        mNoFavoritesLayout = (LinearLayout) rootView.findViewById(R.id.noFavoritesLayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 100);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        if(mFavoriteMode == FavoritesListFragment.ARG_MODE_STATIONS) {
            mCallbacks.onFavoriteStationSelected(mStationListAdapter.getStation(position));
        } else {
            mCallbacks.onFavoriteDepartureSelected(mDepartureListAdapter.getDeparture(position));
        }
    }

    public void showNoFavoritesLayout(boolean show) {
        if (show) {
            mNoFavoritesLayout.setVisibility(View.VISIBLE);
        } else {
            mNoFavoritesLayout.setVisibility(View.GONE);
        }
    }

    public void setStations(List<Station> stations) {
        showNoFavoritesLayout(stations.size() == 0);
        mStationListAdapter.setStations(stations);
    }

    public void setDepartures(List<Departure> departures) {
        showNoFavoritesLayout(departures.size() == 0);
        mDepartureListAdapter.setDepartures(departures);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }
}
