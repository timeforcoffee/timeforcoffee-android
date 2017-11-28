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

    @Inject
    public DepartureService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchDeparturesEvent event) {
        eventBus.post(new FetchZvvDeparturesEvent(event.getStationId()));
    }

    @Subscribe
    public void onEvent(ZvvDeparturesFetchedEvent event) {
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
