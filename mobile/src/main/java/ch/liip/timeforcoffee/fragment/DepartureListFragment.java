package ch.liip.timeforcoffee.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.activity.MainActivity;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;


public class DepartureListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, DepartureListAdapter.Callbacks {

    private FragmentActivity mActivity;
    private DepartureListAdapter mDepartureListAdapter;
    private RelativeLayout mNoDesparturesLayout;
    private ProgressBar mLoadingDeparturesProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks {
        void onRefresh();
        void onDepartureSelected(Departure departure);
        void onDepartureFavoriteToggled(Departure departure, boolean isFavorite);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onDepartureSelected(Departure departure) { }

        @Override
        public void onRefresh() { }

        @Override
        public void onDepartureFavoriteToggled(Departure departure, boolean isFavorite) { }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DepartureListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TimeForCoffeeApplication) getActivity().getApplication()).inject(this);

        mActivity = getActivity();
        mDepartureListAdapter = new DepartureListAdapter(getActivity(), new ArrayList<Departure>(), this);
        setListAdapter(mDepartureListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departure_list, container, false);

        mLoadingDeparturesProgressBar = rootView.findViewById(R.id.loadingDeparturesSpinner);
        mNoDesparturesLayout = rootView.findViewById(R.id.noDeparturesLayout);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLoadingDeparturesProgressBar.setVisibility(View.GONE);
        mNoDesparturesLayout.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivity instanceof DeparturesActivity) {
            ((DeparturesActivity)mActivity).performDepartureUpdate();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCallbacks.onRefresh();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 100);
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
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onDepartureSelected(mDepartureListAdapter.getDeparture(position));
    }

    @Override
    public void onDepartureFavoriteToggled(Departure departure, boolean isFavorite) {
        mCallbacks.onDepartureFavoriteToggled(departure, isFavorite);
    }

    public void setDepartures(List<Departure> departures) {
        showNoDeparturesLayout(departures.size() == 0);
        mDepartureListAdapter.setDepartures(departures);
    }

    public void showLoadingDeparturesProgressBar(boolean show) {
        if(mLoadingDeparturesProgressBar != null) {
            if (show) {
                mLoadingDeparturesProgressBar.setVisibility(View.VISIBLE);
            } else {
                mLoadingDeparturesProgressBar.setVisibility(View.GONE);
            }
        }

    }

    public void showNoDeparturesLayout(boolean show) {
        if(mNoDesparturesLayout != null) {
            mNoDesparturesLayout.setVisibility(View.GONE);
            if (show) {
                mNoDesparturesLayout.setVisibility(View.VISIBLE);
            } else {
                mNoDesparturesLayout.setVisibility(View.GONE);
            }
        }
    }
}