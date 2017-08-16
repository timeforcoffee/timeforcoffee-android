package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.common.FontFitTextView;
import ch.liip.timeforcoffee.common.Typefaces;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
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

    public View getView(int position, View convertView, ViewGroup parent) {

        DepartureViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_departure_list_row, parent, false);

            viewHolder = new DepartureViewHolder();
            viewHolder.lineNameTextView = (FontFitTextView) convertView.findViewById(R.id.name);
            viewHolder.destinationTextView = (TextView) convertView.findViewById(R.id.to);
            viewHolder.departureTextView = (TextView) convertView.findViewById(R.id.departure);
            viewHolder.scheduledTimeTextView = (TextView) convertView.findViewById(R.id.scheduledtime);
            viewHolder.realtimeTextView = (TextView) convertView.findViewById(R.id.realtime);
            viewHolder.platformTextView = (TextView) convertView.findViewById(R.id.platform);
            viewHolder.accessibleTextView = (TextView) convertView.findViewById(R.id.accessible);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (DepartureViewHolder) convertView.getTag();
        }

        final Departure departure = this.mDepartures.get(position);

        setLineName(viewHolder.lineNameTextView, departure);
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
        }

        viewHolder.lineNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = departure.getIsFavorite();
                if (isFavorite) {
                    //remove from favorites
                    mFavoritesDataSource.deleteFavoriteLine(departure);
                } else {
                    //add to favorites
                    mFavoritesDataSource.insertFavoriteLine(departure);
                }

                departure.setIsFavorite(!isFavorite);
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
            } else {
                if (name.equals("RE")) {
                    textView.setTextColor(Color.RED);
                } else {
                    textView.setTextColor(departure.getColorFg());
                }

                if (departure.getColorBg() != Color.WHITE) {
                    textView.setBackgroundColor(departure.getColorBg());
                } else {
                    if (departure.getType() != null && departure.getType().equals("train")) {
                        textView.setBackgroundColor(Color.WHITE);
                    } else {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public Departure getDeparture(int position) {
        return this.mDepartures.get(position);
    }

    public void setDepartures(List<Departure> mDepartures) {
        this.mDepartures.clear();
        this.mDepartures.addAll(mDepartures);
        notifyDataSetChanged();
    }
}
