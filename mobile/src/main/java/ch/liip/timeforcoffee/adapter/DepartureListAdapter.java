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

import java.util.Arrays;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.DeparturesActivity;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.common.FontFitTextView;
import ch.liip.timeforcoffee.common.Typefaces;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;

public class DepartureListAdapter extends ArrayAdapter<Departure> {

    private List<Departure> mDepartures;
    private FavoritesDataSource mFavoritesDataSource;
    private Context mContext;

    private static final String[] linesWithSymbol = {"ICN", "EN", "ICN", "TGV", "RX", "EC", "IC", "SC", "CNL", "ICE", "IR"};

    private static class DepartureViewHolder {
        FontFitTextView lineNameTextView;
        TextView destinationTextView;
        TextView departureTextView;
        TextView scheduledTimeTextView;
        TextView realtimeTextView;
        TextView platformTextView;
        TextView accessibleTextView;
    }

    public DepartureListAdapter(Context context, List<Departure> departures, FavoritesDataSource favoritesDataSource) {
        super(context, R.layout.fragment_departure_list_row, departures);

        mDepartures = departures;
        mContext = context;
        mFavoritesDataSource = favoritesDataSource;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Departure departure = this.mDepartures.get(position);
        DepartureViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_departure_list_row, parent, false);

            viewHolder = new DepartureViewHolder();
            viewHolder.lineNameTextView = convertView.findViewById(R.id.name);
            viewHolder.destinationTextView = convertView.findViewById(R.id.to);
            viewHolder.departureTextView = convertView.findViewById(R.id.departure);
            viewHolder.scheduledTimeTextView = convertView.findViewById(R.id.scheduledtime);
            viewHolder.realtimeTextView = convertView.findViewById(R.id.realtime);
            viewHolder.platformTextView = convertView.findViewById(R.id.platform);
            viewHolder.accessibleTextView = convertView.findViewById(R.id.accessible);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (DepartureViewHolder) convertView.getTag();
        }

        setLineName(viewHolder.lineNameTextView, departure);
        viewHolder.destinationTextView.setText(departure.getDestinationName());
        viewHolder.departureTextView.setText(departure.departureInMinutes());

        if (departure.isLate()) { //realtime != schedule time
            viewHolder.realtimeTextView.setVisibility(View.VISIBLE);
            viewHolder.realtimeTextView.setText(departure.getDepartureRealtimeStr());
            viewHolder.scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            viewHolder.realtimeTextView.setVisibility(View.GONE);
            viewHolder.scheduledTimeTextView.setText(departure.getDepartureScheduledStr());
            viewHolder.scheduledTimeTextView.setPaintFlags(viewHolder.scheduledTimeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (departure.getPlatform() != null) {
            viewHolder.platformTextView.setText(String.format(getContext().getString(R.string.platform), departure.getPlatform()));
        }
        else {
            viewHolder.platformTextView.setText("");
        }

        if (departure.isAccessible()) {
            viewHolder.accessibleTextView.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.accessibleTextView.setVisibility(View.GONE);
        }

        viewHolder.lineNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = departure.getIsFavorite();
                String action, action1;

                if (isFavorite) {
                    mFavoritesDataSource.deleteFavoriteLine(mContext, departure);
                    action = mContext.getResources().getString(R.string.departure_action_remove);
                    action1 = mContext.getResources().getString(R.string.departure_action_remove_2);
                } else {
                    mFavoritesDataSource.insertFavoriteLine(mContext, departure);
                    action = mContext.getResources().getString(R.string.departure_action_add);
                    action1 = mContext.getResources().getString(R.string.departure_action_add_2);
                }

                departure.setIsFavorite(!isFavorite);
                String message = mContext.getResources().getString(R.string.departure_fav_message);
                String messageFormatted = String.format(message, departure.getName(), departure.getDestinationName(), action, action1);
                ((DeparturesActivity)getContext()).displayToastMessage(messageFormatted);
            }
        });

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

    public Departure getDeparture(int position) {
        return this.mDepartures.get(position);
    }

    public void setDepartures(List<Departure> departures) {
        this.mDepartures.clear();
        this.mDepartures.addAll(departures);
        notifyDataSetChanged();
    }
}
