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
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.api.models.WalkingDistance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import io.nlopez.smartlocation.SmartLocation;


public class StationMapFragment extends Fragment implements OnMapReadyCallback {

    private MapFragment mapFragment;
    private GoogleMap map;
    private ImageView gradientOverlay;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView mChevron;

    private Station mStation;
    private Departure mDeparture;

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
        gradientOverlay = (ImageView) view.findViewById(R.id.gradient_overlay);
        titleTextView = (TextView) view.findViewById(R.id.journey_title);
        subtitleTextView = (TextView) view.findViewById(R.id.journey_subtitle);
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

        if(mDeparture == null) {
            drawWalkingPath();
        } else {
            drawTransportPath();
        }
    }


    public void setup(Station station) {
        mStation = station;
        titleTextView.setText(mStation.getName());
    }

    public void setup(Station station, Departure departure, String fromStr) {
        mStation = station;
        mDeparture = departure;

        titleTextView.setText(mDeparture.getDestinationName());
        subtitleTextView.setText(String.format("%s %s", fromStr, mStation.getName()));
    }

    public void updateGradientOverlay(float alpha, int height) {
        ViewGroup.LayoutParams params = gradientOverlay.getLayoutParams();
        params.height = height;
        gradientOverlay.setLayoutParams(params);
        gradientOverlay.setAlpha(alpha);
        if (alpha < 1) {
            mChevron.setBackgroundResource(R.drawable.chevron_up);
        } else {
            mChevron.setBackgroundResource(R.drawable.chevron_down);
        }
    }

    private void drawWalkingPath() {

        if (mStation == null) {
            return;
        }

        //compute walking distance
        mStation.setOnDistanceComputedListener(new Station.OnDistanceComputedListener() {
            @Override
            public void onDistanceComputed(WalkingDistance distance) {

                if (distance == null || !isAdded()) {
                    return;
                }

                subtitleTextView.setText(distance.getWalkingDistance());

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
            subtitleTextView.setText(distance.getWalkingDistance());
        }
    }

    private void drawTransportPath() {

        if (mStation == null || mDeparture == null) {
            return;
        }

        // handle this somehow
    }
}
