package ch.liip.timeforcoffee.fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.api.models.Connection;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import io.nlopez.smartlocation.SmartLocation;


public class StationMapFragment extends Fragment implements OnMapReadyCallback, OnMapLoadedCallback {

    private static final int MAP_ZOOM_PADDING = 75;
    private static final float MAP_ZOOM_DEFAULT = 16;

    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private final List<LatLng> mVisiblePoints = new ArrayList<>();

    private Station mStation;
    private List<Connection> mConnections;

    private ImageView mGradientOverlay;
    private TextView mTitleTextView;
    private TextView mSubtitleTextView;
    private ImageView mChevron;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onMapLoaded();
    }

    public StationMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_map, container, false);

        mGradientOverlay = view.findViewById(R.id.gradient_overlay);
        mTitleTextView = view.findViewById(R.id.journey_title);
        mSubtitleTextView = view.findViewById(R.id.journey_subtitle);
        mChevron = view.findViewById(R.id.chevron);

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment);
        transaction.commit();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        if(mStation != null) {
            drawWalkingPath();
        }
        else if(mConnections != null && mConnections.size() != 0) {
            drawTransportPath();
        }
    }

    @Override
    public void onMapLoaded() {
        calculateZoomForVisiblePoints();
        if(mCallbacks != null) {
            mCallbacks.onMapLoaded();
        }
    }

    public void setup(Station station) {
        mStation = station;
        mTitleTextView.setText(mStation.getName());

        loadMap();
    }

    public void setup(List<Connection> connections, Station departure, Departure destination) {
        mConnections = connections;

        mTitleTextView.setText(destination.getDestinationName());
        mSubtitleTextView.setText(String.format("%s %s", getResources().getString(R.string.connection_from), departure.getName()));
        mSubtitleTextView.setVisibility(View.VISIBLE);

        loadMap();
    }

    public void updateGradientOverlay(float alpha, int height) {
        ViewGroup.LayoutParams params = mGradientOverlay.getLayoutParams();
        params.height = height;

        mGradientOverlay.setLayoutParams(params);
        mGradientOverlay.setAlpha(alpha);

        if (alpha < 1) {
            mChevron.setBackgroundResource(R.drawable.chevron_up);
        }
        else {
            mChevron.setBackgroundResource(R.drawable.chevron_down);
        }
    }

    private void loadMap() {
        mMapFragment.getMapAsync(this);
    }

    private void drawWalkingPath() {
        LatLng stationLocation = new LatLng(mStation.getLocation().getLatitude(), mStation.getLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(stationLocation).title(mStation.getName()));
        mVisiblePoints.add(stationLocation);

        Location currentLocation = SmartLocation.with(getActivity()).location().getLastLocation();
        if (currentLocation != null) {
            LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mVisiblePoints.add(userLocation);
        }

        String distance = mStation.getDistanceForDisplay(currentLocation);
        if (distance != null) {
            mSubtitleTextView.setText(distance);
            mSubtitleTextView.setVisibility(View.VISIBLE);
        }
    }

    private void drawTransportPath() {
        List<LatLng> checkpoints = new ArrayList<>();
        BitmapDescriptor checkpointIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin);
        BitmapDescriptor destinationIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_flag);

        Connection departure = mConnections.get(0);
        Connection destination = mConnections.get(mConnections.size() - 1);
        mConnections.remove(departure);
        mConnections.remove(destination);

        // Departure
        LatLng departureLocation = new LatLng(departure.getStationLocation().getLatitude(), departure.getStationLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(departureLocation).title(departure.getStationName()));
        checkpoints.add(departureLocation);

        // Checkpoints
        for(Connection connection : mConnections) {
            LatLng location = new LatLng(connection.getStationLocation().getLatitude(), connection.getStationLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).icon(checkpointIcon).anchor(0.5f, 0.9f).title(connection.getStationName()));
            checkpoints.add(location);
        }

        // Destination
        LatLng destinationLocation = new LatLng(destination.getStationLocation().getLatitude(), destination.getStationLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(destinationLocation).icon(destinationIcon).anchor(0.2f, 0.9f).title(destination.getStationName()));
        checkpoints.add(destinationLocation);

        mVisiblePoints.addAll(checkpoints);
    }

    private void calculateZoomForVisiblePoints() {
        if (mVisiblePoints.size() == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mVisiblePoints.get(0), MAP_ZOOM_DEFAULT));
            return;
        }
        else if(mVisiblePoints.size() == 0) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng visiblePoint : mVisiblePoints) {
            builder.include(visiblePoint);
        }

        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_ZOOM_PADDING));
    }
}
