package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import ch.liip.timeforcoffee.zvv.CheckPoint;
import ch.liip.timeforcoffee.zvv.ConnectionsResponse;

import static org.junit.Assert.assertEquals;

public class ConnectionDeserializationTest {

    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    @Test
    public void stationsForLocationDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("connections.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        ConnectionsResponse connectionsResponse = gson.fromJson(reader, ConnectionsResponse.class);

        List<CheckPoint> stations = connectionsResponse.getConnections();
        assertEquals(12, stations.size());

        CheckPoint station1 = stations.get(0);
        assertEquals("8593280", station1.getId());
        assertEquals("Bulle, Place de la Gare", station1.getName());
        assertEquals((Double)46.617579, (Double) station1.getLocation().getLat());
        assertEquals((Double)7.055366, (Double) station1.getLocation().getLng());
        assertEquals("2018-03-09T15:45:00" , dateTimeFormatter.format(station1.getDeparture().getScheduled()));
        assertEquals(null , station1.getDeparture().getRealtime());
        assertEquals(null , station1.getArrival());

        CheckPoint station2 = stations.get(1);
        assertEquals("8593285", station2.getId());
        assertEquals("Bulle, Sionge", station2.getName());
        assertEquals((Double)46.620067, (Double) station2.getLocation().getLat());
        assertEquals((Double)7.05681, (Double) station2.getLocation().getLng());
        assertEquals("2018-03-09T15:46:00" , dateTimeFormatter.format(station2.getDeparture().getScheduled()));
        assertEquals(null , station2.getDeparture().getRealtime());
        assertEquals(null , station2.getArrival());

        CheckPoint station3 = stations.get(2);
        assertEquals("8504912", station3.getId());
        assertEquals("Bulle, Château-d'en-Bas", station3.getName());
        assertEquals((Double)46.622882, (Double) station3.getLocation().getLat());
        assertEquals((Double)7.056647, (Double) station3.getLocation().getLng());
        assertEquals("2018-03-09T15:47:00" , dateTimeFormatter.format(station3.getDeparture().getScheduled()));
        assertEquals(null , station3.getDeparture().getRealtime());
        assertEquals(null , station3.getArrival());

        CheckPoint station4 = stations.get(3);
        assertEquals("8593279", station4.getId());
        assertEquals("Bulle, Pierre-Alex", station4.getName());
        assertEquals((Double)46.622129, (Double) station4.getLocation().getLat());
        assertEquals((Double)7.060256, (Double) station4.getLocation().getLng());
        assertEquals("2018-03-09T15:47:00" , dateTimeFormatter.format(station4.getDeparture().getScheduled()));
        assertEquals(null , station4.getDeparture().getRealtime());
        assertEquals(null , station4.getArrival());

        CheckPoint station5 = stations.get(4);
        assertEquals("8593283", station5.getId());
        assertEquals("Bulle, Pré-Vert", station5.getName());
        assertEquals((Double)46.620917, (Double) station5.getLocation().getLat());
        assertEquals((Double)7.060839, (Double) station5.getLocation().getLng());
        assertEquals("2018-03-09T15:48:00" , dateTimeFormatter.format(station5.getDeparture().getScheduled()));
        assertEquals(null , station5.getDeparture().getRealtime());
        assertEquals(null , station5.getArrival());

        CheckPoint station6 = stations.get(5);
        assertEquals("8593485", station6.getId());
        assertEquals("Bulle, Stade", station6.getName());
        assertEquals((Double)46.622283, (Double) station6.getLocation().getLat());
        assertEquals((Double)7.065816, (Double) station6.getLocation().getLng());
        assertEquals("2018-03-09T15:49:00" , dateTimeFormatter.format(station6.getDeparture().getScheduled()));
        assertEquals(null , station6.getDeparture().getRealtime());
        assertEquals(null , station6.getArrival());

        CheckPoint station7 = stations.get(6);
        assertEquals("8504913", station7.getId());
        assertEquals("Bulle, Chalamala", station7.getName());
        assertEquals((Double)46.622417, (Double) station7.getLocation().getLat());
        assertEquals((Double)7.068466, (Double) station7.getLocation().getLng());
        assertEquals("2018-03-09T15:52:00" , dateTimeFormatter.format(station7.getDeparture().getScheduled()));
        assertEquals(null , station7.getDeparture().getRealtime());
        assertEquals(null , station7.getArrival());

        CheckPoint station8 = stations.get(7);
        assertEquals("8504914", station8.getId());
        assertEquals("Bulle, Jéricho", station8.getName());
        assertEquals((Double)46.622892, (Double) station8.getLocation().getLat());
        assertEquals((Double)7.076231, (Double) station8.getLocation().getLng());
        assertEquals("2018-03-09T15:53:00" , dateTimeFormatter.format(station8.getDeparture().getScheduled()));
        assertEquals(null , station8.getDeparture().getRealtime());
        assertEquals(null , station8.getArrival());

        CheckPoint station9 = stations.get(8);
        assertEquals("8504915", station9.getId());
        assertEquals("Morlon, La Croix", station9.getName());
        assertEquals((Double)46.623091, (Double) station9.getLocation().getLat());
        assertEquals((Double)7.079442, (Double) station9.getLocation().getLng());
        assertEquals("2018-03-09T15:54:00" , dateTimeFormatter.format(station9.getDeparture().getScheduled()));
        assertEquals(null , station9.getDeparture().getRealtime());
        assertEquals(null , station9.getArrival());

        CheckPoint station10 = stations.get(9);
        assertEquals("8588830", station10.getId());
        assertEquals("Morlon, En Perrey", station10.getName());
        assertEquals((Double)46.624544, (Double) station10.getLocation().getLat());
        assertEquals((Double)7.083663, (Double) station10.getLocation().getLng());
        assertEquals("2018-03-09T15:55:00" , dateTimeFormatter.format(station10.getDeparture().getScheduled()));
        assertEquals(null , station10.getDeparture().getRealtime());
        assertEquals(null , station10.getArrival());

        CheckPoint station11 = stations.get(10);
        assertEquals("8577739", station11.getId());
        assertEquals("Morlon, Centre", station11.getName());
        assertEquals((Double)46.625909, (Double) station11.getLocation().getLat());
        assertEquals((Double)7.085887, (Double) station11.getLocation().getLng());
        assertEquals("2018-03-09T15:56:00" , dateTimeFormatter.format(station11.getDeparture().getScheduled()));
        assertEquals(null , station11.getDeparture().getRealtime());
        assertEquals(null , station11.getArrival());

        CheckPoint station12 = stations.get(11);
        assertEquals("8593289", station12.getId());
        assertEquals("Morlon, Eglise", station12.getName());
        assertEquals((Double)46.627686, (Double) station12.getLocation().getLat());
        assertEquals((Double)7.087533, (Double) station12.getLocation().getLng());
        assertEquals("2018-03-09T15:58:00" , dateTimeFormatter.format(station12.getDeparture().getScheduled()));
        assertEquals(null , station12.getDeparture().getRealtime());
        assertEquals("2018-03-09T15:58:00" , dateTimeFormatter.format(station12.getDeparture().getScheduled()));
        assertEquals(null , station12.getArrival().getRealtime());
    }
}
