package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.events.FetchZvvStationboardEvent;
import ch.liip.timeforcoffee.api.events.ZvvStationboardFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;

public class DepartureService {

    private final EventBus eventBus;

    @Inject
    public DepartureService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchDeparturesEvent event) {
        eventBus.post(new FetchZvvStationboardEvent(event.getStationId()));
    }

    @Subscribe
    public void onEvent(ZvvStationboardFetchedEvent event) {
        ArrayList<Departure> departures = new ArrayList<>();
        for (ch.liip.timeforcoffee.zvv.Departure zvvDeparture : event.getDepartures()) {
            Departure departure = DepartureMapper.fromZvv(zvvDeparture);
            if(departure != null) {
                departures.add(departure);
            }
        }

        eventBus.post(new DeparturesFetchedEvent(departures));
    }
}
