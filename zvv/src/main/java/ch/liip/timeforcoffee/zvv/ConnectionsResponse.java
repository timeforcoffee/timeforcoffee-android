package ch.liip.timeforcoffee.zvv;

import java.util.List;

public class ConnectionsResponse {

    private List<List<CheckPoint>> passlist;

    public ConnectionsResponse() {}

    public List<CheckPoint> getConnections() {
        return passlist.get(0);
    }

    public static ConnectionsResponse fromCheckPointList(List<List<CheckPoint>> checkPointList) {
        ConnectionsResponse connectionResponse = new ConnectionsResponse();
        connectionResponse.passlist = checkPointList;
        return connectionResponse;
    }
}
