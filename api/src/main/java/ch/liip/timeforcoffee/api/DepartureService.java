package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.departuresEvents.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchZvvDeparturesEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.ZvvDeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import ch.liip.timeforcoffee.api.models.Departure;

public class DepartureService {

    private final EventBus eventBus;
    private String stationId;

    @Inject
    public DepartureService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchDeparturesEvent event) {
        stationId = event.getStationId();
        eventBus.post(new FetchZvvDeparturesEvent(stationId));
    }

    @Subscribe
    public void onEvent(ZvvDeparturesFetchedEvent event) {
        ArrayList<Departure> departures = new ArrayList<>();
        int stationId = Integer.parseInt(this.stationId);

        for (ch.liip.timeforcoffee.zvv.Departure zvvDeparture : event.getDepartures()) {
            Departure departure = DepartureMapper.fromZvv(zvvDeparture, stationId);
            if(departure != null) {
                departures.add(departure);
            }
        }

        eventBus.post(new DeparturesFetchedEvent(departures));
    }
}
