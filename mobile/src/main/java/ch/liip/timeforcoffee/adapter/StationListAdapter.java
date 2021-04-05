package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.api.models.WalkingDistance;
import ch.liip.timeforcoffee.widget.FavoriteButton;
import io.nlopez.smartlocation.SmartLocation;

public class StationListAdapter extends ArrayAdapter<Station> {

    private List<Station> mStations;

    private Context mContext;
    private Callbacks mCallbacks = new Callbacks() {
        @Override
        public void onStationFavoriteToggled(Station station, boolean isFavorite) { }
    };

    public interface Callbacks {
        void onStationFavoriteToggled(Station station, boolean isFavorite);
    }

    public StationListAdapter(Context context, List<Station> stations, Callbacks callbacks) {
        super(context, R.layout.fragment_station_list_row, stations);

        this.mContext = context;
        this.mStations = stations;
        this.mCallbacks = callbacks;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        final StationViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_station_list_row, parent, false);

            viewHolder = new StationViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.name);
            viewHolder.distanceTextView = convertView.findViewById(R.id.distance);
            viewHolder.favoriteButton = convertView.findViewById(R.id.fav);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (StationViewHolder) convertView.getTag();
        }

        final Station currentStation = this.mStations.get(position);

        viewHolder.nameTextView.setText(currentStation.getName());

        Location location = SmartLocation.with(mContext).location().getLastLocation();

        currentStation.setOnDistanceComputedListener(new Station.OnDistanceComputedListener() {
            @Override
            public void onDistanceComputed(WalkingDistance distance) {
                if (distance != null) {
                    viewHolder.distanceTextView.setText(distance.getWalkingDistance());
                }
            }
        });

        WalkingDistance walkingDistance = currentStation.getDistanceForDisplay(location);
        if (walkingDistance != null) {
            viewHolder.distanceTextView.setText(walkingDistance.getWalkingDistance());
        }

        viewHolder.favoriteButton.setIsFavorite(currentStation.getIsFavorite());
        viewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = !currentStation.getIsFavorite();
                currentStation.setIsFavorite(isFavorite);

                mCallbacks.onStationFavoriteToggled(currentStation, isFavorite);
            }
        });

        return convertView;
    }

    public Station getStation(int position) {
        return this.mStations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Station getItem(int position) {
        return this.mStations.get(position);
    }

    @Override
    public int getCount() {
        return this.mStations.size();
    }

    public void setStations(List<Station> stations) {
        this.mStations.clear();
        this.mStations.addAll(stations);
        notifyDataSetChanged();
    }

    private static class StationViewHolder {
        TextView nameTextView;
        TextView distanceTextView;
        FavoriteButton favoriteButton;
    }
}
