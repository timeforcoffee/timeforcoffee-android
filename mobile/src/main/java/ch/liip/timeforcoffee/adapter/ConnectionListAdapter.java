package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionListAdapter extends ArrayAdapter<Connection> {

    private List<Connection> mConnexions;
    private Context mContext;

    private static class ConnexionViewHolder {
        TextView stationTextView;
        TextView timeLabelTextView;
        TextView timeTextView;
        TextView realtimeDepartureTextView;
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
            viewHolder.stationTextView = convertView.findViewById(R.id.station);
            viewHolder.timeLabelTextView = convertView.findViewById(R.id.time_label);
            viewHolder.timeTextView = convertView.findViewById(R.id.time);
            viewHolder.realtimeDepartureTextView = convertView.findViewById(R.id.realtime);
            viewHolder.departureTextView = convertView.findViewById(R.id.departure);


            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ConnectionListAdapter.ConnexionViewHolder) convertView.getTag();
        }

        Connection connection = this.mConnexions.get(position);
        viewHolder.stationTextView.setText(connection.getStationName());
        viewHolder.timeTextView.setText(connection.getScheduledDepartureStr());
        viewHolder.departureTextView.setText(connection.getDepartureInMinutes());

        if(position != mConnexions.size() - 1) {
            viewHolder.timeLabelTextView.setText(mContext.getResources().getString(R.string.connection_departure));
        }
        else {
            viewHolder.timeLabelTextView.setText(mContext.getResources().getString(R.string.connection_arrival));
        }

        if (connection.isLate()) {
            viewHolder.realtimeDepartureTextView.setVisibility(View.VISIBLE);
            viewHolder.realtimeDepartureTextView.setText(connection.getRealtimeDepartureStr());
            viewHolder.timeTextView.setPaintFlags(viewHolder.timeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            viewHolder.realtimeDepartureTextView.setVisibility(View.GONE);
            viewHolder.timeTextView.setPaintFlags(viewHolder.timeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        return convertView;
    }

    public void setConnexions(List<Connection> connexions) {
        this.mConnexions.clear();
        this.mConnexions.addAll(connexions);

        notifyDataSetChanged();
    }
}
