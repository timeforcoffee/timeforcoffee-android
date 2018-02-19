package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.common.FontFitTextView;
import ch.liip.timeforcoffee.common.Typefaces;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class DepartureListAdapter extends ArrayAdapter<Departure> {

    private List<Departure> mDepartures;
    private Context mContext;

    private static final String[] linesWithSymbol = {"ICN", "EN", "ICN", "TGV", "RX", "EC", "IC", "SC", "CNL", "ICE", "IR"};

    private static class DepartureViewHolder {
        FontFitTextView lineNameTextView;
        TextView toTextView;
        TextView departureTextView;
        TextView scheduledTimeTextView;
        TextView realtimeTextView;
        TextView platformTextView;
    }

    public DepartureListAdapter(Context context, List<Departure> departures) {
        super(context, R.layout.departure_list_row, departures);

        mDepartures = departures;
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        DepartureViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.departure_list_row, parent, false);

            viewHolder = new DepartureViewHolder();
            viewHolder.lineNameTextView = (FontFitTextView) convertView.findViewById(R.id.name);
            viewHolder.toTextView = (TextView) convertView.findViewById(R.id.to);
            viewHolder.departureTextView = (TextView) convertView.findViewById(R.id.departure);
            viewHolder.scheduledTimeTextView = (TextView) convertView.findViewById(R.id.scheduledtime);
            viewHolder.realtimeTextView = (TextView) convertView.findViewById(R.id.realtime);
            viewHolder.platformTextView = (TextView) convertView.findViewById(R.id.platform);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (DepartureViewHolder) convertView.getTag();
        }

        Departure departure = this.mDepartures.get(position);

        setLineName(viewHolder.lineNameTextView, departure);
        viewHolder.toTextView.setText(departure.getDestinationName());
        viewHolder.departureTextView.setText(departure.departureInMinutes());

        if (departure.isLate()) {
            viewHolder.realtimeTextView.setVisibility(View.VISIBLE);
            viewHolder.realtimeTextView.setText(departure.getDepartureRealtimeStr());
            viewHolder.scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(viewHolder.scheduledTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.realtimeTextView.setVisibility(View.GONE);
            viewHolder.scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(viewHolder.scheduledTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (departure.getPlatform() != null) {
            viewHolder.platformTextView.setVisibility(View.VISIBLE);
            viewHolder.platformTextView.setText(String.format(getContext().getString(R.string.platform), departure.getPlatform()));
        } else {
            viewHolder.platformTextView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setLineName(TextView textView, Departure departure) {

        String name = departure.getName();
        textView.setText(name);

        try {
            if (Arrays.asList(linesWithSymbol).contains(name)) {
                Typeface type = Typefaces.get(mContext, "trainsymbol");
                textView.setTypeface(type);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.RED);

            } else {

                if (name.equals("RE")) {
                    textView.setTextColor(Color.RED);
                } else {
                    textView.setTextColor(departure.getColorFg());
                }

                if (departure.getColorBg() != Color.WHITE) {
                    textView.setBackgroundColor(departure.getColorBg());
                } else {
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                }
            }
        } catch (Exception ex) {
        }
    }

    public void setDepartures(List<Departure> departures) {
        mDepartures.clear();
        mDepartures.addAll(departures);
        notifyDataSetChanged();
    }
}
