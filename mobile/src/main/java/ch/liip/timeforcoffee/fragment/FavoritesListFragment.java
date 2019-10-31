package ch.liip.timeforcoffee.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.adapter.StationListAdapter;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;


public class FavoritesListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, StationListAdapter.Callbacks, DepartureListAdapter.Callbacks {

    public static final String ARG_MODE = "favorite_mode";
    public static final int ARG_MODE_STATIONS = 0;
    public static final int ARG_MODE_DEPARTURES = 1;
    private int mFavoriteMode;

    private StationListAdapter mStationListAdapter;
    private DepartureListAdapter mDepartureListAdapter;
    private LinearLayout mNoFavoritesLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onFavoriteStationSelected(Station station);
        void onFavoriteDepartureSelected(Departure departure);
        void onStationFavoriteToggled(Station station, boolean isFavorite);
        void onDepartureFavoriteToggled(Departure station, boolean isFavorite);
    }

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
            if (mStationListAdapter == null) {
                mStationListAdapter = new StationListAdapter(getActivity(), new ArrayList<Station>(), this);
            }
            setListAdapter(mStationListAdapter);
        }
        else {
            mDepartureListAdapter = new DepartureListAdapter(getActivity(), new ArrayList<Departure>(), this);
            setListAdapter(mDepartureListAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        mNoFavoritesLayout = rootView.findViewById(R.id.noFavoritesLayout);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        showNoFavoritesLayout(mStationListAdapter == null || mStationListAdapter.getCount() == 0);

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

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
        if(mCallbacks != null) {
            if(mFavoriteMode == FavoritesListFragment.ARG_MODE_STATIONS) {
                mCallbacks.onFavoriteStationSelected(mStationListAdapter.getStation(position));
            }
            else {
                mCallbacks.onFavoriteDepartureSelected(mDepartureListAdapter.getDeparture(position));
            }
        }
    }

    @Override
    public void onStationFavoriteToggled(Station station, boolean isFavorite) {
        if(mCallbacks != null) {
            mCallbacks.onStationFavoriteToggled(station, isFavorite);
        }
        showNoFavoritesLayout(mStationListAdapter.getCount() == 0);
    }

    @Override
    public void onDepartureFavoriteToggled(Departure departure, boolean isFavorite) {
        if(mCallbacks != null) {
            mCallbacks.onDepartureFavoriteToggled(departure, isFavorite);
        }
    }

    public void setStations(List<Station> stations) {
        showNoFavoritesLayout(stations.size() == 0);
        mStationListAdapter.setStations(stations);
    }

    public void setDepartures(List<Departure> departures) {
//        showNoFavoritesLayout(departures.size() == 0);
        mDepartureListAdapter.setDepartures(departures);
    }

    private void showNoFavoritesLayout(boolean show) {
        if (show) {
            mNoFavoritesLayout.setVisibility(View.VISIBLE);
        } else {
            mNoFavoritesLayout.setVisibility(View.GONE);
        }
    }
}
