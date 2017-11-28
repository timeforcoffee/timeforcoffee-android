package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.view.StationItemView;

import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class StationListAdapter extends WearableListView.Adapter {

    private List<Station> mStations;
    private Context mContext;

    public StationListAdapter(Context context, List<Station> stations) {
        mStations = stations;
        mContext = context;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WearableListView.ViewHolder(new StationItemView(mContext));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, final int position) {
        StationItemView view = (StationItemView) viewHolder.itemView;
        final Station item = mStations.get(position);
        TextView textView = (TextView) view.findViewById(R.id.name);
        textView.setText(item.getName());
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
}
