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
        TextView slashTextView;
        TextView departureTextView;
        TextView departureTimeTextView;
        TextView departureRealtimeTextView;
        TextView arrivalTextView;
        TextView arrivalTimeTextView;
        TextView arrivalRealtimeTextView;
        TextView minutesTextView;
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
            viewHolder.arrivalTextView = convertView.findViewById(R.id.arrival);
            viewHolder.arrivalTimeTextView = convertView.findViewById(R.id.arrival_time);
            viewHolder.arrivalRealtimeTextView = convertView.findViewById(R.id.arrival_realtime);
            viewHolder.slashTextView = convertView.findViewById(R.id.slash);
            viewHolder.departureTextView = convertView.findViewById(R.id.departure);
            viewHolder.departureTimeTextView = convertView.findViewById(R.id.departure_time);
            viewHolder.departureRealtimeTextView = convertView.findViewById(R.id.departure_realtime);
            viewHolder.minutesTextView = convertView.findViewById(R.id.minutes);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ConnectionListAdapter.ConnexionViewHolder) convertView.getTag();
        }

        Connection connection = this.mConnexions.get(position);
        viewHolder.stationTextView.setText(connection.getStationName());
        viewHolder.minutesTextView.setText(connection.getDepartureInMinutes());

        viewHolder.departureTimeTextView.setText(connection.getScheduledDepartureStr());

        if (connection.isDepartureLate()) {
            viewHolder.departureRealtimeTextView.setVisibility(View.VISIBLE);
            viewHolder.departureRealtimeTextView.setText(connection.getRealtimeDepartureStr());
            viewHolder.departureTimeTextView.setPaintFlags(viewHolder.departureTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            viewHolder.departureRealtimeTextView.setVisibility(View.GONE);
            viewHolder.departureTimeTextView.setPaintFlags(viewHolder.departureTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        viewHolder.arrivalTimeTextView.setText(connection.getScheduledArrivalStr());

        if (connection.isArrivalLate()) {
            viewHolder.arrivalRealtimeTextView.setVisibility(View.VISIBLE);
            viewHolder.arrivalRealtimeTextView.setText(connection.getRealtimeDepartureStr());
            viewHolder.arrivalTimeTextView.setPaintFlags(viewHolder.arrivalTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.arrivalRealtimeTextView.setVisibility(View.GONE);
            viewHolder.arrivalTimeTextView.setPaintFlags(viewHolder.arrivalTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        //don't show arrival times for first item
        if(position == 0) {
            viewHolder.arrivalTextView.setVisibility(View.GONE);
            viewHolder.arrivalTimeTextView.setVisibility(View.GONE);
            viewHolder.arrivalRealtimeTextView.setVisibility(View.GONE);
            viewHolder.slashTextView.setVisibility(View.GONE);
        } else if (position == getCount() - 1) {
            viewHolder.departureTextView.setVisibility(View.GONE);
            viewHolder.departureTimeTextView.setVisibility(View.GONE);
            viewHolder.departureRealtimeTextView.setVisibility(View.GONE);
            viewHolder.slashTextView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setConnexions(List<Connection> connexions) {
        this.mConnexions.clear();
        this.mConnexions.addAll(connexions);

        notifyDataSetChanged();
    }
}
