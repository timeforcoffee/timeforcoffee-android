package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.connectionsEvents.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchConnectionsErrorEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.mappers.ConnectionMapper;
import ch.liip.timeforcoffee.api.models.Connection;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.backend.ConnectionsResponse;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConnectionService {

    private final EventBus eventBus;
    private final BackendService backendService;

    @Inject
    public ConnectionService(EventBus eventBus, BackendService backendService) {
        this.backendService = backendService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchConnectionsEvent event) {
        Map<String, String> query = new HashMap<>();
        query.put("station_id", event.getStationId());
        query.put("destination_id", event.getStationId());
        query.put("departure", event.getDepartureString());

        backendService.getConnections(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConnectionsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchConnectionsErrorEvent(e));
                    }

                    @Override
                    public void onNext(ConnectionsResponse connectionsResponse) {
                        List<Connection> connections = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Connection backendConnection : connectionsResponse.getConnections()) {
                            Connection connection = ConnectionMapper.fromBackend(backendConnection);
                            if(connection != null) {
                                connections.add(connection);
                            }
                        }

                        eventBus.post(new ConnectionsFetchedEvent(connections));
                    }
                });
    }
}
