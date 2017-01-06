package ch.liip.timeforcoffee.view;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;

/**
 * Created by nicolas on 22/12/15.
 */
public final class StationItemView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    final TextView mName;
    final ImageView mImage;

    public StationItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.station_item, this);
        mName = (TextView) findViewById(R.id.name);
        mImage = (ImageView) findViewById(R.id.place);
    }

    @Override
    public void onCenterPosition(boolean b) {
        mImage.setAlpha(1f);
        mImage.setAlpha(1f);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        mImage.setAlpha(0.6f);
        mImage.setAlpha(0.6f);
    }
}