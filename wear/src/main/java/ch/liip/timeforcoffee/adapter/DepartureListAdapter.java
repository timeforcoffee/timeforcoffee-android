package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.common.Typefaces;

import java.util.Arrays;
import java.util.List;

public class DepartureListAdapter extends RecyclerView.Adapter<DepartureListAdapter.ViewHolder> {

    private final List<Departure> mDepartures;

    private static final String[] linesWithSymbol = {"ICN", "EN", "ICN", "TGV", "RX", "EC", "IC", "SC", "CNL", "ICE", "IR"};

    public DepartureListAdapter(List<Departure> departures) {
        mDepartures = departures;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.departure_list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(mDepartures.get(position));
    }

    @Override
    public int getItemCount() {
        return mDepartures.size();
    }

    public void setDepartures(List<Departure> departures) {
        mDepartures.clear();
        mDepartures.addAll(departures);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lineNameTextView;
        TextView toTextView;
        TextView departureTextView;
        TextView scheduledTimeTextView;
        TextView realtimeTextView;
        TextView platformTextView;

        public ViewHolder(View view) {
            super(view);
            lineNameTextView = view.findViewById(R.id.name);
            toTextView = view.findViewById(R.id.to);
            departureTextView = view.findViewById(R.id.departure);
            scheduledTimeTextView = view.findViewById(R.id.scheduledtime);
            realtimeTextView = view.findViewById(R.id.realtime);
            platformTextView = view.findViewById(R.id.platform);
        }

        public void bind(Departure departure) {
            setLineName(lineNameTextView, departure);
            toTextView.setText(departure.getDestinationName());
            departureTextView.setText(departure.departureInMinutes());

            if (departure.isLate()) {
                realtimeTextView.setVisibility(View.VISIBLE);
                realtimeTextView.setText(departure.getDepartureRealtimeStr());
                scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
                scheduledTimeTextView.setPaintFlags(scheduledTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                realtimeTextView.setVisibility(View.GONE);
                scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
                scheduledTimeTextView.setPaintFlags(scheduledTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            if (departure.getPlatform() != null) {
                platformTextView.setVisibility(View.VISIBLE);
                platformTextView.setText(String.format(platformTextView.getContext().getString(R.string.platform), departure.getPlatform()));
            } else {
                platformTextView.setVisibility(View.GONE);
            }
        }

        private void setLineName(TextView textView, Departure departure) {
            String name = departure.getLine();
            textView.setText(name);

            try {
                if (Arrays.asList(linesWithSymbol).contains(name)) {
                    Typeface type = Typefaces.get(textView.getContext(), "trainsymbol");
                    textView.setTypeface(type);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundColor(Color.RED);
                } else {
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setTextColor(name.equals("RE") ? Color.RED : departure.getColorFg());
                    textView.setBackgroundColor(departure.getColorBg() == Color.WHITE ? textView.getContext().getResources().getColor(R.color.gray) : departure.getColorBg());
                }
            } catch (Exception ex) {
                // Do nothing
            }
        }
    }
}
