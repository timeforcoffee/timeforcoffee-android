package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.events.FetchZvvStationboardEvent;
import ch.liip.timeforcoffee.api.events.ZvvStationboardFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.ArrayList;

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
        eventBus.post(new FetchZvvStationboardEvent(event.getStationId()));
    }

    @Subscribe
    public void onEvent(ZvvStationboardFetchedEvent event) {
        ArrayList<Departure> departures = new ArrayList<Departure>();
        for (ch.liip.timeforcoffee.zvv.Departure zvvDeparture : event.getDepartures()) {
            departures.add(DepartureMapper.fromZvv(zvvDeparture));
        }
        eventBus.post(new DeparturesFetchedEvent(departures));
    }


}
