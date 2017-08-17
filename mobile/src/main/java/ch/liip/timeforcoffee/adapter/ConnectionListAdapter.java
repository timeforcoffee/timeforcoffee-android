package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Connection;

public class ConnectionListAdapter extends ArrayAdapter<Connection> {

    private List<Connection> mConnexions;
    private Context mContext;

    private static class ConnexionViewHolder {
        TextView stationTextView;
        TextView typeTextView;
        TextView timeTimeTextView;
        TextView departureTextView;
    }

    public ConnectionListAdapter(Context context, List<Connection> connexions) {
        super(context, R.layout.fragment_departure_list_row, connexions);

        mConnexions = connexions;
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ConnectionListAdapter.ConnexionViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_connection_list_row, parent, false);

            viewHolder = new ConnectionListAdapter.ConnexionViewHolder();
            viewHolder.stationTextView = (TextView) convertView.findViewById(R.id.station);
            viewHolder.typeTextView = (TextView) convertView.findViewById(R.id.type);
            viewHolder.timeTimeTextView = (TextView) convertView.findViewById(R.id.time);
            viewHolder.departureTextView = (TextView) convertView.findViewById(R.id.departure);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ConnectionListAdapter.ConnexionViewHolder) convertView.getTag();
        }

        final Connection connection = this.mConnexions.get(position);

        viewHolder.stationTextView.setText(connection.getName());
        // handle other properties

        return convertView;
    }

    public Connection getConnexion(int position) {
        return this.mConnexions.get(position);
    }

    public void setConnexions(List<Connection> connexions) {
        this.mConnexions.clear();
        this.mConnexions.addAll(connexions);
        notifyDataSetChanged();
    }
}
