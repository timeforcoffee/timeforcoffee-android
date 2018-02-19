package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchOpenDataStationsLocationEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.OpenDataStationsLocationFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.StationsLocationFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchZvvStationsSearchEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.StationsSearchFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.ZvvStationsSearchFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.FetchStationsSearchOneEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.FetchZvvStationsSearchOneEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.StationsSearchOneFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.ZvvStationsSearchOneFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.api.models.Station;

public class StationService {

    private final EventBus eventBus;

    @Inject
    public StationService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchStationsSearchEvent event) {
        eventBus.post(new FetchZvvStationsSearchEvent(event.getSearchQuery()));
    }

    @Subscribe
    public void onEvent(FetchStationsSearchOneEvent event) {
        eventBus.post(new FetchZvvStationsSearchOneEvent(event.getSearchQuery()));
    }

    @Subscribe
    public void onEvent(FetchStationsLocationEvent event) {
        eventBus.post(new FetchOpenDataStationsLocationEvent(event.getQuery()));
    }

    @Subscribe
    public void onEvent(ZvvStationsSearchFetchedEvent event) {
        ArrayList<Station> stations = new ArrayList<>();
        for (ch.liip.timeforcoffee.zvv.Station zvvStation : event.getStations()) {
            stations.add(StationMapper.fromZvv(zvvStation));
        }

        eventBus.post(new StationsSearchFetchedEvent(stations));
    }

    @Subscribe
    public void onEvent(ZvvStationsSearchOneFetchedEvent event) {
        Station station = StationMapper.fromZvv(event.getStation());
        eventBus.post(new StationsSearchOneFetchedEvent(station));
    }

    @Subscribe
    public void onEvent(OpenDataStationsLocationFetchedEvent event) {
        ArrayList<Station> stations = new ArrayList<>();
        for (ch.liip.timeforcoffee.opendata.Location location : event.getLocations()) {
            Station station = StationMapper.fromLocation(location);
            if(station != null) {
                stations.add(station);
            }
        }

        eventBus.post(new StationsLocationFetchedEvent(stations));
    }
}
