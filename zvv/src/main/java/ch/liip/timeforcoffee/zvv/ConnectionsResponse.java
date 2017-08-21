package ch.liip.timeforcoffee.zvv;

import java.util.List;

public class ConnectionsResponse {

    private List<List<CheckPoint>> passlist;

    public ConnectionsResponse() {}

    public List<CheckPoint> getConnections() {
        return passlist.get(0);
    }
}
