package ch.liip.timeforcoffee.presenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.MainActivity;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.OpenDataApiService;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.api.events.FetchOpenDataLocationsEvent;
import ch.liip.timeforcoffee.api.events.StationsFetchedEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.helper.PermissionsChecker;
import ch.liip.timeforcoffee.widget.SnackBars;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicolas on 10/10/16.
 */
public class MainPresenter implements Presenter, OnLocationUpdatedListener {

    private MainActivity mActivity;

    private List<Station> mStations;
    private List<Station> mFavoriteStations;

    private Location mLastLocation;
    private boolean mIsCapturingLocation;
    private final int LOCATION_SMALLEST_DISPLACEMENT = 250;
    private final int LOCATION_INTERVAL = 10000;

    private String locationPermission = "android.permission.ACCESS_FINE_LOCATION";
    private PermissionsChecker permissionsChecker;

    final String TAG = "timeforcoffee";
    final int PERMISSION_REQUEST_CODE = 0;

    @Inject
    EventBus mEventBus;

    @Inject
    OpenDataApiService service;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

        permissionsChecker = new PermissionsChecker(activity);
    }

    public void onResumeView() {
        if (Build.FINGERPRINT.contains("generic")) { //emulator
            updateStationsWithLastPosition();
        } else if (!mIsCapturingLocation) {
            startLocation();
        }
    }

    public void onPauseView() {
        if (mIsCapturingLocation) {
            stopLocation();
        }
    }

    public void onRefreshView() {
        updateStationsWithLastPosition();
    }

    public void updateStationsWithLastPosition() {

        if (Build.FINGERPRINT.contains("generic")) { //emulator
            mLastLocation = new Location("emulator");
            mLastLocation.setLatitude(46.8017);
            mLastLocation.setLongitude(7.1456);
            //mLastLocation.setLatitude(52.5074592);
            //mLastLocation.setLongitude(13.2860644);
            //mLastLocation.setLatitude(40.705311);
            //mLastLocation.setLongitude(-74.2581929);
        }

        if (mLastLocation != null) {
            updateStations(mLastLocation);
        }
    }

    public void updateFavorites() {
        if(mStations == null || mStations.size() == 0) {
            return;
        }

        List<Station> favoriteStations = favoritesDataSource.getAllFavoriteStations(mActivity);
        for(Station station : mStations) {
            station.setIsFavorite(favoriteStations.contains(station));
        }

        mFavoriteStations = favoriteStations;
        mActivity.updateStations(mStations);
        mActivity.updateFavorites(mFavoriteStations);
    }

    private void startLocation() {
        if (!permissionsChecker.LacksPermission(locationPermission)) {

            if (!SmartLocation.with(mActivity).location().state().locationServicesEnabled()) {
                SnackBars.showLocalisationServiceOff(mActivity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                return;
            }

            mIsCapturingLocation = true;
            SmartLocation smartLocation = new SmartLocation.Builder(mActivity).logging(true).build();
            LocationParams locationParams = new LocationParams.Builder().setAccuracy(LocationAccuracy.MEDIUM).setDistance(LOCATION_SMALLEST_DISPLACEMENT).setInterval(LOCATION_INTERVAL).build();
            smartLocation.location().config(locationParams).start(this);

        } else {
            permissionsChecker.RequestPermission(mActivity, locationPermission, PERMISSION_REQUEST_CODE, mActivity.getResources().getString(R.string.permission_message));
        }
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
                    SnackBars.showLocalisationSettings(mActivity);
                }
            }
        }
    }

    private void stopLocation() {
        mIsCapturingLocation = false;
        SmartLocation.with(mActivity).location().stop();
    }

    @Override
    public void onLocationUpdated(Location location) {
        Log.i(TAG, "onLocationUpdated : lat = " + location.getLatitude() + " , long = " + location.getLongitude());
        mLastLocation = location;

        updateStations(location);
    }

    private void updateStations(Location location) {
        if (location != null) {
            Map<String, String> query = new HashMap<>();
            query.put("x", Double.toString(location.getLatitude()));
            query.put("y", Double.toString(location.getLongitude()));
            Log.i(TAG, "get stations for lat =  " + location.getLatitude() + " and long = " + location.getLongitude());
            mEventBus.post(new FetchOpenDataLocationsEvent(query));
        }
    }

    @Subscribe
    public void onStationsFetched(StationsFetchedEvent event) {
        mActivity.showProgressLayout(false);

        mStations = event.getStations();
        mActivity.updateStations(mStations);

        updateFavorites();
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        mActivity.showProgressLayout(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStationsWithLastPosition();
            }
        });
    }

    public void onDestroy() {
        mActivity = null;
        mEventBus.unregister(this);
    }
}
