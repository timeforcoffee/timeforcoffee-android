package ch.liip.timeforcoffee.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.MainActivity;
import ch.liip.timeforcoffee.activity.StationSearchActivity;
import ch.liip.timeforcoffee.adapter.StationListAdapter;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;


public class StationListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, StationListAdapter.Callbacks {

    public static final String ARG_SEARCH_MODE = "search_mode";
    private boolean mSearchMode;

    private FragmentActivity mActivity;
    private StationListAdapter mStationListAdapter;
    private RelativeLayout mNoStationsLayout;
    private RelativeLayout mLoadingPositionLayout;
    private RelativeLayout mEnterSearchLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Callbacks mCallbacks;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public interface Callbacks {
        void onRefresh();
        void onStationSelected(Station station);
        void onStationFavoriteToggled(Station station, boolean isFavorite);
    }

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
        ((TimeForCoffeeApplication) getActivity().getApplication()).inject(this);

        mActivity = getActivity();
        Bundle args = getArguments();

        if(args == null) {
            return;
        }

        mSearchMode = args.getBoolean(ARG_SEARCH_MODE);
        mStationListAdapter = new StationListAdapter(getActivity(), new ArrayList<Station>(), this);
        setListAdapter(mStationListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_list, container, false);

        mLoadingPositionLayout = rootView.findViewById(R.id.loadingPositionLayout);
        mNoStationsLayout = rootView.findViewById(R.id.noStationsLayout);
        mEnterSearchLayout = rootView.findViewById(R.id.enterSearchLayout);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        mLoadingPositionLayout.setVisibility(View.GONE);
        mNoStationsLayout.setVisibility(View.GONE);
        if (mSearchMode) {
            mEnterSearchLayout.setVisibility(View.VISIBLE);
        } else {
            mEnterSearchLayout.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivity instanceof MainActivity) {
            ((MainActivity)mActivity).performStationsUpdate();
        }
        else if(mActivity instanceof StationSearchActivity) {
            ((StationSearchActivity)mActivity).performStationsSearch();
        }
        onRefresh();
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
                swipeRefreshLayout.setRefreshing(false);
                if(mCallbacks != null) {
                    mCallbacks.onRefresh();
                }
            }
        }, 100);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        if(mCallbacks != null) {
            mCallbacks.onStationSelected(mStationListAdapter.getStation(position));
        }
    }

    @Override
    public void onStationFavoriteToggled(Station station, boolean isFavorite) {
        if(mCallbacks != null) {
            mCallbacks.onStationFavoriteToggled(station, isFavorite);
        }
    }

    public void setStations(List<Station> stations) {
        if(mSearchMode) {
            showNoStationsLayout(stations.size() == 0);
        }

        mEnterSearchLayout.setVisibility(View.GONE);
        mStationListAdapter.setStations(stations);
    }

    public void showLoadingPositionLayout(boolean show) {
        if(mLoadingPositionLayout != null) {
            if (show) {
                mLoadingPositionLayout.setVisibility(View.VISIBLE);
            }
            else {
                mLoadingPositionLayout.setVisibility(View.GONE);
            }
        }

    }

    public void showNoStationsLayout(boolean show) {
        if(mNoStationsLayout != null && mEnterSearchLayout != null) {
            mEnterSearchLayout.setVisibility(View.GONE);
            if (show) {
                mNoStationsLayout.setVisibility(View.VISIBLE);
            }
            else {
                mNoStationsLayout.setVisibility(View.GONE);
            }
        }
    }
}
