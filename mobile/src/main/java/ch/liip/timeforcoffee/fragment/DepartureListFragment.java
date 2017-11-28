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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;


public class DepartureListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private DepartureListAdapter mDepartureListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Callbacks mCallbacks = sDummyCallbacks;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public interface Callbacks {
        void onDepartureSelected(Departure departure);
        void onRefresh();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onDepartureSelected(Departure departure) {
        }

        @Override
        public void onRefresh() {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DepartureListFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TimeForCoffeeApplication) getActivity().getApplication()).inject(this);

        mDepartureListAdapter = new DepartureListAdapter(getActivity(), new ArrayList<Departure>(), favoritesDataSource);
        setListAdapter(mDepartureListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departure_list, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
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

    public void setDepartures(List<Departure> departures) {
        mDepartureListAdapter.setDepartures(departures);
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onDepartureSelected(mDepartureListAdapter.getDeparture(position));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }
}