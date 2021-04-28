package ch.liip.timeforcoffee.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeWearApplication;
import ch.liip.timeforcoffee.activity.WearActivity;
import ch.liip.timeforcoffee.api.DepartureService;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.events.departuresEvents.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesErrorEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.StationsLocationFetchedEvent;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.PermissionsChecker;

public class WearPresenter implements Presenter {

    public final int PLAY_SERVICE_RESOLUTION_REQUEST_CODE = 123;

    private final String TAG = "timeforcoffee";
    private final String locationPermission = "android.permission.ACCESS_FINE_LOCATION";
    private final int PERMISSION_REQUEST_CODE = 0;

    private WearActivity mActivity;
    private final PermissionsChecker mPermissionsChecker;

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            loadStations(locationResult.getLastLocation());
        }
    };

    @Inject
    EventBus mEventBus;

    @Inject
    StationService stationService;

    @Inject
    DepartureService departureService;

    public WearPresenter(WearActivity activity) {
        mActivity = activity;

        ((TimeForCoffeeWearApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

        mPermissionsChecker = new PermissionsChecker(mActivity);
    }

    @Override
    public void onResumeView() {
        startLocation();
    }

    @Override
    public void onRefreshView() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(mActivity).getLastLocation()
                    .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mActivity.displayRefreshState();
                                loadStations(location);
                            }
                        }
                    });
        }
    }

    @Override
    public void onPauseView() {
        LocationServices.getFusedLocationProviderClient(mActivity).removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onDestroy() {
        mActivity = null;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (!(permission.equals(locationPermission))) {
                    continue;
                }

                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    startLocation();
                } else {
                    showError(R.string.permission_no_location);
                }
            }
        }
    }

    private void onStationsReceived(final List<Station> stations) {
        Log.d(TAG, "onStationsReceived");

        //User did not selected a stations. Select the first one in list
        if (stations != null && stations.size() > 0) {
            //Select first station in the list and load departures for it
            Station station = stations.get(0);
            mActivity.setStation(station);
            loadDepartures(station.getId());

            //Display station list
            mActivity.setStations(stations);
        } else {
            showError(R.string.title_no_stations);
        }
    }

    private void onDeparturesReceived(List<Departure> departures) {
        Log.d(TAG, "onDeparturesReceived");
        mActivity.setDepartures(departures);
    }

    private void startLocation() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            int LOCATION_SMALLEST_DISPLACEMENT = 250;
            int LOCATION_INTERVAL = 10000;
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(LOCATION_INTERVAL)
                    .setFastestInterval(LOCATION_INTERVAL)
                    .setSmallestDisplacement(LOCATION_SMALLEST_DISPLACEMENT);

            LocationServices.getFusedLocationProviderClient(mActivity).requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());

        } else {
            mPermissionsChecker.RequestPermission(mActivity, locationPermission, PERMISSION_REQUEST_CODE, mActivity.getResources().getString(R.string.permission_message));
        }
    }

    private void loadStations(Location location) {
        if (location != null) {
            Log.i(TAG, "get stations for lat =  " + location.getLatitude() + " and long = " + location.getLongitude());
            mEventBus.post(new FetchStationsLocationEvent(location.getLatitude(), location.getLongitude()));
        }
    }

    @Subscribe
    public void onStationsFetched(StationsLocationFetchedEvent event) {
        onStationsReceived(event.getStations());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchStationsLocationErrorEvent event) {
        showError(R.string.network_error);
    }

    private void loadDepartures(int stationId) {
        Log.d(TAG, "loadDepartures");
        mEventBus.post(new FetchDeparturesEvent(stationId));
    }

    @Subscribe
    public void onDeparturesFetchedEvent(DeparturesFetchedEvent event) {
        onDeparturesReceived(event.getDepartures());
    }

    @Subscribe
    public void onFetchErrorEvent(FetchDeparturesErrorEvent event) {
        showError(R.string.network_error);
    }

    private void showError(int messageStringResId) {
        Toast.makeText(mActivity, messageStringResId, Toast.LENGTH_LONG).show();
        mActivity.displayNoResult();
    }

    public void selectStation(Station station) {
        loadDepartures(station.getId());
    }
}
