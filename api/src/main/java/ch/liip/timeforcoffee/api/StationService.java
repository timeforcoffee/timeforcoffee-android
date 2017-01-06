package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.FetchStationsEvent;
import ch.liip.timeforcoffee.api.events.FetchZvvStationsEvent;
import ch.liip.timeforcoffee.api.events.StationsFetchedEvent;
import ch.liip.timeforcoffee.api.events.ZvvStationsFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by fsantschi on 11/03/15.
 */
public class StationService {
    private final EventBus eventBus;

    @Inject
    public StationService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchStationsEvent event) {
        eventBus.post(new FetchZvvStationsEvent(event.getSearchQuery()));
    }

    @Subscribe
    public void onEvent(ZvvStationsFetchedEvent event) {
        ArrayList<Station> stations = new ArrayList<Station>();
        for (ch.liip.timeforcoffee.zvv.Station zvvStation : event.getStations()) {
            stations.add(StationMapper.fromZvv(zvvStation));
        }
        eventBus.post(new StationsFetchedEvent(stations));
    }
}
