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
import ch.liip.timeforcoffee.common.FontFitTextView;

public class ConnexionListAdapter extends ArrayAdapter<Connection> {

    private List<Connection> mConnexions;
    private Context mContext;

    private static class ConnexionViewHolder {
        // handle model properties
        FontFitTextView lineNameTextView;
        TextView destinationTextView;
        TextView departureTextView;
        TextView scheduledTimeTextView;
        TextView realtimeTextView;
        TextView platformTextView;
        TextView accessibleTextView;
    }

    public ConnexionListAdapter(Context context, List<Connection> connexions) {
        super(context, R.layout.fragment_departure_list_row, connexions);

        mConnexions = connexions;
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ConnexionListAdapter.ConnexionViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_departure_list_row, parent, false); // handle cell xml

            viewHolder = new ConnexionListAdapter.ConnexionViewHolder();
            viewHolder.lineNameTextView = (FontFitTextView) convertView.findViewById(R.id.name);
            viewHolder.destinationTextView = (TextView) convertView.findViewById(R.id.to);
            viewHolder.departureTextView = (TextView) convertView.findViewById(R.id.departure);
            viewHolder.scheduledTimeTextView = (TextView) convertView.findViewById(R.id.scheduledtime);
            viewHolder.realtimeTextView = (TextView) convertView.findViewById(R.id.realtime);
            viewHolder.platformTextView = (TextView) convertView.findViewById(R.id.platform);
            viewHolder.accessibleTextView = (TextView) convertView.findViewById(R.id.accessible);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ConnexionListAdapter.ConnexionViewHolder) convertView.getTag();
        }

        final Connection departure = this.mConnexions.get(position);

        /*setLineName(viewHolder.lineNameTextView, departure);
        viewHolder.destinationTextView.setText(departure.getDestination());
        viewHolder.departureTextView.setText(departure.departureInMinutes());

        if (departure.isLate()) { //realtime != schedule time
            viewHolder.realtimeTextView.setVisibility(View.VISIBLE);
            viewHolder.realtimeTextView.setText(departure.getRealtimeStr());
            viewHolder.scheduledTimeTextView.setText(departure.getScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.realtimeTextView.setVisibility(View.GONE);
            viewHolder.scheduledTimeTextView.setText(departure.getScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(viewHolder.scheduledTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (departure.getPlatform() != null) {
            viewHolder.platformTextView.setText(String.format(getContext().getString(R.string.platform), departure.getPlatform()));
        } else {
            viewHolder.platformTextView.setText("");
        }

        if (departure.isAccessible()) {
            viewHolder.accessibleTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.accessibleTextView.setVisibility(View.GONE);
        }*/

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
