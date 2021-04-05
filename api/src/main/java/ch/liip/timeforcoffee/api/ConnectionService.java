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

        backendService.getConnections(
                String.valueOf(event.getStationId()),
                String.valueOf(event.getDestinationId()),
                event.getDepartureString(),
                event.getArrivalString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ch.liip.timeforcoffee.backend.Connections>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchConnectionsErrorEvent(e));
                    }

                    @Override
                    public void onNext(ch.liip.timeforcoffee.backend.Connections backendConnections) {
                        List<Connection> connections = new ArrayList<>();
                        List<List<ch.liip.timeforcoffee.backend.Connection>> backendConnectionsList = backendConnections.getConnections();
                        if (backendConnectionsList.size() > 0) {
                            for (ch.liip.timeforcoffee.backend.Connection backendConnection : backendConnectionsList.get(0)) {
                                connections.add(ConnectionMapper.fromBackend(backendConnection));
                            }
                        }

                        eventBus.post(new ConnectionsFetchedEvent(connections));
                    }
                });
    }
}
