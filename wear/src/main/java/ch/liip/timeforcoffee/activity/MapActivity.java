package ch.liip.timeforcoffee.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import ch.liip.timeforcoffee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by nicolas on 17/01/16.
 */
public class MapActivity extends Activity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private MapFragment mMapFragment;
    private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;

    public static final String ARG_STATION_LONGITUDE = "station_longitude";
    public static final String ARG_STATION_LATITUDE = "station_latitude";
    public static final String ARG_STATION_NAME = "station_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.basic_wear_long_press_intro);
        mDismissOverlay.showIntroIfNecessary();

        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                mDismissOverlay.show();
            }
        });

        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        //GoogleMapOptions.ambientEnabled(true);

    }

    @Override
    public void onMapReady(GoogleMap map) {

        Location location = new Location("reverseGeocoded");
        location.setLatitude(getIntent().getDoubleExtra(MapActivity.ARG_STATION_LATITUDE, 0.0));
        location.setLongitude(getIntent().getDoubleExtra(MapActivity.ARG_STATION_LONGITUDE, 0.0));

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

        map.addMarker(new MarkerOptions().position(latLng).title(getIntent().getStringExtra(MapActivity.ARG_STATION_NAME)));
        map.setMyLocationEnabled(true);
        map.setOnMapLongClickListener(this);
    }

    public void onMapLongClick(LatLng point) {
        mDismissOverlay.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }


}
