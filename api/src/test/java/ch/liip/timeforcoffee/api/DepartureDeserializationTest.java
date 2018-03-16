package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.api.deserializers.ConnectionsDeserializer;
import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.zvv.ConnectionsResponse;
import ch.liip.timeforcoffee.zvv.Departure;
import ch.liip.timeforcoffee.zvv.StationboardMeta;
import ch.liip.timeforcoffee.zvv.StationboardResponse;

import static org.junit.Assert.assertEquals;

public class DepartureDeserializationTest {

    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void departuresDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("departures.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        StationboardResponse stationboardResponse = gson.fromJson(reader, StationboardResponse.class);

        StationboardMeta stationboardMeta = stationboardResponse.getMeta();
        assertEquals("8577725", stationboardMeta.getStationId());
        assertEquals("Bulle, gare", stationboardMeta.getStationName());

        List<Departure> departures = stationboardResponse.getDepartures();
        assertEquals(3, departures.size());

        Departure departure1 = departures.get(0);
        assertEquals("8593286", departure1.getId());
        assertEquals("201", departure1.getName());
        assertEquals("bus", departure1.getType());
        assertEquals("#000000", departure1.getColors().getFg());
        assertEquals("#FFFFFF", departure1.getColors().getBg());
        assertEquals("La Tour-de-Trême, Le Closalet", departure1.getTo());
        assertEquals(null, departure1.getPlatform());
        assertEquals("2018-03-09T15:44:00" , dateTimeFormatter.format(departure1.getDeparture().getScheduled()));
        assertEquals(null , departure1.getDeparture().getRealtime());
        assertEquals("2018-03-09T15:50:00" , dateTimeFormatter.format(departure1.getArrival().getScheduled()));
        assertEquals(null , departure1.getArrival().getRealtime());
        assertEquals(false , departure1.getAccessible());

        Departure departure2 = departures.get(1);
        assertEquals("8593291", departure2.getId());
        assertEquals("202", departure2.getName());
        assertEquals("bus", departure2.getType());
        assertEquals("#000000", departure2.getColors().getFg());
        assertEquals("#FFFFFF", departure2.getColors().getBg());
        assertEquals("Vuadens, gare", departure2.getTo());
        assertEquals(null, departure2.getPlatform());
        assertEquals("2018-03-09T15:44:00" , dateTimeFormatter.format(departure2.getDeparture().getScheduled()));
        assertEquals(null , departure2.getDeparture().getRealtime());
        assertEquals("2018-03-09T15:53:00" , dateTimeFormatter.format(departure2.getArrival().getScheduled()));
        assertEquals(null , departure2.getArrival().getRealtime());
        assertEquals(false , departure2.getAccessible());

        Departure departure3 = departures.get(2);
        assertEquals("8593289", departure3.getId());
        assertEquals("202", departure3.getName());
        assertEquals("bus", departure3.getType());
        assertEquals("#000000", departure3.getColors().getFg());
        assertEquals("#FFFFFF", departure3.getColors().getBg());
        assertEquals("Morlon, Eglise", departure3.getTo());
        assertEquals(null, departure3.getPlatform());
        assertEquals("2018-03-09T15:45:00" , dateTimeFormatter.format(departure3.getDeparture().getScheduled()));
        assertEquals(null , departure3.getDeparture().getRealtime());
        assertEquals("2018-03-09T15:58:00" , dateTimeFormatter.format(departure3.getArrival().getScheduled()));
        assertEquals(null , departure3.getArrival().getRealtime());
        assertEquals(false , departure3.getAccessible());
    }
}
