package ch.liip.timeforcoffee.api;

import android.util.Log;
import ch.liip.timeforcoffee.api.events.*;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fsantschi on 11/03/15.
 */
public class DepartureService {
    private final EventBus eventBus;

    @Inject
    public DepartureService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchDeparturesEvent event) {
        Map<String,String> crtMap = new HashMap<>();
        crtMap.put("id", event.getStationId());
        eventBus.post(new FetchOpenDataStationboardEvent(crtMap));
    }

    @Subscribe
    public void onEvent(ZvvStationboardFetchedEvent event) {
        ArrayList<Departure> departures = new ArrayList<Departure>();
        for (ch.liip.timeforcoffee.zvv.Departure zvvDeparture : event.getDepartures()) {
            departures.add(DepartureMapper.fromZvv(zvvDeparture));
        }
        eventBus.post(new DeparturesFetchedEvent(departures));
    }

    @Subscribe
    public void onEvent(OpenDataStationboardFetchedEvent event) {
        ArrayList<Departure> departures = new ArrayList<>();

        for(ch.liip.timeforcoffee.opendata.Journey journey : event.getStationboards()) {
            departures.add(DepartureMapper.fromOpenData(journey));
        }
        eventBus.post(new DeparturesFetchedEvent(departures));
    }


}
