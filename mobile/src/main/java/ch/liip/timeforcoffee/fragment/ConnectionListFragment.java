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

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.ConnexionListAdapter;
import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.presenter.ConnectionListPresenter;

public class ConnectionListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_DEPARTURE_NAME = "departure_name";
    public static final String ARG_DEPARTURE_TYPE = "departure_type";
    public static final String ARG_DEPARTURE_ACCESSIBLE = "departure_accessible";
    public static final String ARG_DEPARTURE_DESTINATION = "departure_destination";
    public static final String ARG_DEPARTURE_PLATFORM = "departure_platform";
    public static final String ARG_DEPARTURE_SCHEDULED = "departure_scheduled";
    public static final String ARG_DEPARTURE_REALTIME = "departure_realtime";
    public static final String ARG_DEPARTURE_COLOR_FG = "departure_color_fg";
    public static final String ARG_DEPARTURE_COLOR_BG = "departure_color_bg";
    public static final String ARG_DEPARTURE_IS_FAVORITE = "departure_is_favorite";

    private ConnectionListPresenter mPresenter;

    private ConnexionListAdapter mConnexionListAdapter;
    private RelativeLayout mProgressLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ConnectionListFragment.Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks {
        void onConnectionSelected(Connection connection);
        void onRefresh();
    }

    private static ConnectionListFragment.Callbacks sDummyCallbacks = new ConnectionListFragment.Callbacks() {
        @Override
        public void onConnectionSelected(Connection connection) {
        }

        @Override
        public void onRefresh() {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof DepartureListFragment.Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (ConnectionListFragment.Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ConnectionListPresenter(this);

        mConnexionListAdapter = new ConnexionListAdapter(getActivity(), new ArrayList<Connection>());
        setListAdapter(mConnexionListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departure_list, container, false); // handle xml

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

    public void showProgressLayout(boolean show) {
        if (show) {
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
        }
    }

    public void setConnections(List<Connection> connections) {
        mConnexionListAdapter.setConnexions(connections);
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
