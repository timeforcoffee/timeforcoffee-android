package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Station;

import java.util.List;


public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.ViewHolder> {

    private final List<Station> mStations;

    public StationListAdapter(Context context, List<Station> stations) {
        mStations = stations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.station_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(mStations.get(position));
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    public void setStations(List<Station> stations) {
        mStations.clear();
        mStations.addAll(stations);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
        }

        public void bind(Station station) {
            mTextView.setText(station.getName());
        }
    }

}
