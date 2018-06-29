package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.common.Typefaces;

import java.util.Arrays;
import java.util.List;

public class DepartureListAdapter extends ArrayAdapter<Departure> {

    private List<Departure> mDepartures;
    private Context mContext;

    private static final String[] linesWithSymbol = {"ICN", "EN", "ICN", "TGV", "RX", "EC", "IC", "SC", "CNL", "ICE", "IR"};

    private static class DepartureViewHolder {
        TextView lineNameTextView;
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

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Departure departure = this.mDepartures.get(position);
        DepartureViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.departure_list_row, parent, false);

            viewHolder = new DepartureViewHolder();
            viewHolder.lineNameTextView = convertView.findViewById(R.id.name);
            viewHolder.toTextView = convertView.findViewById(R.id.to);
            viewHolder.departureTextView = convertView.findViewById(R.id.departure);
            viewHolder.scheduledTimeTextView = convertView.findViewById(R.id.scheduledtime);
            viewHolder.realtimeTextView = convertView.findViewById(R.id.realtime);
            viewHolder.platformTextView = convertView.findViewById(R.id.platform);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (DepartureViewHolder) convertView.getTag();
        }

        setLineName(viewHolder.lineNameTextView, departure);
        viewHolder.toTextView.setText(departure.getDestinationName());
        viewHolder.departureTextView.setText(departure.departureInMinutes());

        if (departure.isLate()) {
            viewHolder.realtimeTextView.setVisibility(View.VISIBLE);
            viewHolder.realtimeTextView.setText(departure.getDepartureRealtimeStr());
            viewHolder.scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(viewHolder.scheduledTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            viewHolder.realtimeTextView.setVisibility(View.GONE);
            viewHolder.scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(viewHolder.scheduledTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (departure.getPlatform() != null) {
            viewHolder.platformTextView.setVisibility(View.VISIBLE);
            viewHolder.platformTextView.setText(String.format(getContext().getString(R.string.platform), departure.getPlatform()));
        }
        else {
            viewHolder.platformTextView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setLineName(TextView textView, Departure departure) {
        String name = departure.getLine();
        textView.setText(name);

        try {
            if (Arrays.asList(linesWithSymbol).contains(name)) {
                Typeface type = Typefaces.get(mContext, "trainsymbol");
                textView.setTypeface(type);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.RED);
            }
            else {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextColor(name.equals("RE") ? Color.RED : departure.getColorFg());
                textView.setBackgroundColor(departure.getColorBg() == Color.WHITE ? mContext.getResources().getColor(R.color.gray) : departure.getColorBg());
            }
        }
        catch (Exception ex) {
            // Do nothing
        }
    }

    public void setDepartures(List<Departure> departures) {
        mDepartures.clear();
        mDepartures.addAll(departures);
        notifyDataSetChanged();
    }
}
