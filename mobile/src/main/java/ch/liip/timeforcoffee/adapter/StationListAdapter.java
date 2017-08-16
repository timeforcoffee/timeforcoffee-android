package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.WalkingDistance;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.widget.FavoriteButton;
import io.nlopez.smartlocation.SmartLocation;

import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class StationListAdapter extends ArrayAdapter<Station> {

    private List<Station> stations;
    private FavoritesDataSource favoritesDataSource;
    private Context context;

    private static class StationViewHolder {
        TextView nameTextView;
        TextView distanceTextView;
        FavoriteButton favoriteButton;
    }

    public StationListAdapter(Context context, List<Station> stations, FavoritesDataSource dataSource) {
        super(context, R.layout.fragment_station_list_row, stations);

        this.stations = stations;
        this.favoritesDataSource = dataSource;
        this.context = context;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final StationViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_station_list_row, parent, false);

            viewHolder = new StationViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.distanceTextView = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.favoriteButton = (FavoriteButton) convertView.findViewById(R.id.fav);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (StationViewHolder) convertView.getTag();
        }


        final Station currentStation = this.stations.get(position);

        viewHolder.nameTextView.setText(currentStation.getName());

        Location location = null;

        if (Build.FINGERPRINT.startsWith("generic")) { //emulator
            location = new Location("emulator");
            location.setLatitude(46.803);
            location.setLongitude(7.145);
        } else {
            location = SmartLocation.with(context).location().getLastLocation();
        }
        final Location currentLocation = location;

        currentStation.setOnDistanceComputedListener(new Station.OnDistanceComputedListener() {
            @Override
            public void onDistanceComputed(WalkingDistance distance) {
                if (distance != null) {
                    viewHolder.distanceTextView.setText(distance.getWalkingDistance());
                }
            }
        });

        WalkingDistance walkingDistance = currentStation.getDistanceForDisplay(currentLocation);
        if (walkingDistance != null) {
            viewHolder.distanceTextView.setText(walkingDistance.getWalkingDistance());
        }

        viewHolder.favoriteButton.setIsFavorite(currentStation.getIsFavorite());
        viewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean isFavorite = currentStation.getIsFavorite();
                if (isFavorite) {
                    //remove from favorites
                    favoritesDataSource.deleteFavoriteStation(currentStation);
                } else {
                    //add to favorites
                    favoritesDataSource.insertFavoriteStation(currentStation);
                }
                currentStation.setIsFavorite(!isFavorite);
            }

        });

        return convertView;
    }

    public Station getStation(int position) {
        return this.stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Station getItem(int position) {
        return this.stations.get(position);
    }

    @Override
    public int getCount() {
        return this.stations.size();
    }

    public void setStations(List<Station> stations) {
        this.stations.clear();
        this.stations.addAll(stations);
        notifyDataSetChanged();
    }
}
