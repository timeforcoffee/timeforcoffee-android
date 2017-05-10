package ch.liip.timeforcoffee.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.DepartureListAdapter;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.presenter.DepartureListPresenter;

import java.util.ArrayList;
import java.util.List;


public class DepartureListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_STATION_ID = "station_id";
    public static final String ARG__STATION_NAME = "station_name";
    public static final String ARG_STATION_DISTANCE = "station_distance";
    public static final String ARG_STATION_LONGITUDE = "station_longitude";
    public static final String ARG_STATION_LATITUDE = "station_latitude";
    public static final String ARG_IS_FAVORITE = "station_is_favorite";

    private DepartureListPresenter mPresenter;

    private DepartureListAdapter mDepartureListAdapter;
    private RelativeLayout mProgressLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks {
        public void onRefresh();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
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

        mDepartureListAdapter = new DepartureListAdapter(getActivity(), new ArrayList<Departure>());
        setListAdapter(mDepartureListAdapter);

        mPresenter = new DepartureListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_departure_list, container, false);
        mProgressLayout = (RelativeLayout) rootView.findViewById(R.id.progressLayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumeView();
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

    public void showProgresLayout(boolean show) {
        if (show) {
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
        }
    }

    public void setDepartures(List<Departure> departures) {
        departureListAdapter.setDepartures(departures);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPauseView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}