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

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.ConnectionsActivity;
import ch.liip.timeforcoffee.adapter.ConnectionListAdapter;
import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentActivity mActivity;
    private ConnectionListAdapter mConnectionListAdapter;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mConnectionListAdapter = new ConnectionListAdapter(getActivity(), new ArrayList<Connection>());
        setListAdapter(mConnectionListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connection_list, container, false);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivity instanceof ConnectionsActivity) {
            ((ConnectionsActivity)mActivity).performConnectionsUpdate();
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
        if (!(context instanceof ConnectionListFragment.Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (ConnectionListFragment.Callbacks) context;
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onConnectionSelected(mConnectionListAdapter.getConnexion(position));
    }

    public void setConnections(List<Connection> connections) {
        mConnectionListAdapter.setConnexions(connections);
    }
}
