package ch.liip.timeforcoffee.fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.WalkingDistance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import io.nlopez.smartlocation.SmartLocation;


public class StationMapFragment extends Fragment implements OnMapReadyCallback {

    private ImageView gradientOverlay;
    private TextView titleTextView;
    private TextView distanceTextView;
    private MapFragment mapFragment;
    private GoogleMap map;
    private Station mStation;
    private ImageView mChevron;

    private float mLayoutHeight;
    private float mLayoutWidth;

    public StationMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mapFragment = MapFragment.newInstance();
        mapFragment.getMapAsync(this);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mapFragment);
        transaction.commit();

        View view = inflater.inflate(R.layout.fragment_station_map, container, false);
        mLayoutHeight = view.getHeight();
        mLayoutWidth = view.getWidth();

        gradientOverlay = (ImageView) view.findViewById(R.id.gradient_overlay);
        titleTextView = (TextView) view.findViewById(R.id.station_title);
        distanceTextView = (TextView) view.findViewById(R.id.station_distance);
        mChevron = (ImageView) view.findViewById(R.id.chevron);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng latLng = new LatLng(mStation.getLocation().getLatitude(), mStation.getLocation().getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

        map.addMarker(new MarkerOptions().position(latLng).title(mStation.getName()));

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        drawWalkingPath();
    }

    public void onMeasure() {

    }

    public void updateGradientOverlay(float alpha, int height) {
        ViewGroup.LayoutParams params = gradientOverlay.getLayoutParams();
        params.height = height;
        gradientOverlay.setLayoutParams(params);
        gradientOverlay.setAlpha(alpha);
        if (alpha < 1){
            mChevron.setBackgroundResource(R.drawable.chevron_up);
        }else {mChevron.setBackgroundResource(R.drawable.chevron_down);

        }
    }

    public void setStation(Station station) {
        mStation = station;
        titleTextView.setText(mStation.getName());
    }

    private void drawWalkingPath() {

        if (mStation == null) {
            return;
        }

        //compute walking distance
        mStation.setOnDistanceComputedListener(new Station.OnDistanceComputedListener() {
            @Override
            public void onDistanceComputed(WalkingDistance distance) {

                if (distance == null) {
                    return;
                }

                distanceTextView.setText(distance.getWalkingDistance());

                if (distance.getWalkingPath() != null) {
                    PolylineOptions polyoptions = new PolylineOptions();
                    polyoptions.color(getResources().getColor(R.color.dark_blue));
                    polyoptions.width(5);
                    polyoptions.addAll(distance.getWalkingPath().getPoints());
                    map.addPolyline(polyoptions);
                }
            }
        });

        Location currentLocation = SmartLocation.with(getActivity()).location().getLastLocation();
        WalkingDistance distance = mStation.getDistanceForDisplay(currentLocation);
        if (distance != null) {
            distanceTextView.setText(distance.getWalkingDistance());
         }
    }
}
