package ch.liip.timeforcoffee.wear;

import android.content.Intent;
import android.util.Log;
import ch.liip.timeforcoffee.common.SerialisationUtilsGSON;
import ch.liip.timeforcoffee.common.SerializableLocation;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by nicolas on 21/12/15.
 */

public class WearService extends WearableListenerService {

    final String TAG = "timeforcoffee";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);
        if (messageEvent.getPath().compareTo("/location") == 0) {
            String serializedLocation = new String(messageEvent.getData());

            if (serializedLocation == null) {
                return;
            }

            SerializableLocation location = (SerializableLocation) SerialisationUtilsGSON.deserialize(SerializableLocation.class, serializedLocation);

            if (location != null) {
                Intent dataService = new Intent(this, DataService.class);
                dataService.setAction(DataService.GET_STATIONS_ACTION);
                dataService.putExtra(DataService.SOURCE_NODE_ID_EXTRA_PARAM, messageEvent.getSourceNodeId());
                dataService.putExtra(DataService.LATITUDE_EXTRA_PARAM, location.getLatitude());
                dataService.putExtra(DataService.LONGITUDE_EXTRA_PARAM, location.getLongitude());
                startService(dataService);
            }
        }
        if (messageEvent.getPath().compareTo("/station") == 0) {
            String stationId = new String(messageEvent.getData());
            if (stationId != null) {
                Intent dataService = new Intent(this, DataService.class);
                dataService.setAction(DataService.GET_STATION_BOARD_ACTION);
                dataService.putExtra(DataService.SOURCE_NODE_ID_EXTRA_PARAM, messageEvent.getSourceNodeId());
                dataService.putExtra(DataService.STATION_ID_EXTRA_PARAM, stationId);
                startService(dataService);
            }
        }
    }
}
